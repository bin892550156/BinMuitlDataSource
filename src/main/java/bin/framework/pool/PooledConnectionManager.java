package bin.framework.pool;

import bin.framework.DataSourceCilentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 池连接管理器
 */
public class PooledConnectionManager extends AbstarctConnectionManager {

    Logger log = LoggerFactory.getLogger(PooledConnectionManager.class);

    /**
     * 当前连接池的状态信息
     */
    private final PoolState state = new PoolState(this);


    // OPTIONAL CONFIGURATION FIELDS
    /**
     * 最大活跃连接数
     */
    protected int poolMaximumActiveConnections = 10;
    /**
     * 最大空闲连接数
     */
    protected int poolMaximumIdleConnections = 5;

    /**
     * 最大可回收时间，即当达到最大活动链接数时，此时如果有程序获取连接，则检查最先使用的连接，看其是否超出了该时间，如果超出了该时间，则可以回收该连接。（默认20s）
     */
    protected int poolMaximumCheckoutTime = 20000;

    /**
     *  在无法获取连接时，线程需要等待的时间
     */
    protected int poolTimeToWait = 20000;

    /**
     * 这是一个关于坏连接容忍度的底层设置， 作用于每一个尝试从缓存池获取连接的线程。
     *  如果这个线程获取到的是一个坏的连接，那么这个数据源允许这个线程尝试重新获取一个新的连接但是这个重新尝试的次数不应该超过
     *  (最大空闲连接数poolMaximumIdleConnections)与poolMaximumLocalBadConnectionTolerance之和。
     */
    protected int poolMaximumLocalBadConnectionTolerance = 3;

    /**
     * 检查连接正确的语句，默认为"NO PING QUERY SET"，即没有，使用会导致抛异常
     */
    protected String poolPingQuery = "NO PING QUERY SET";

    /**
     * 是否开启ping检测，（默认：false）
     */
    protected boolean poolPingEnabled;

    /**
     * 设置ping检测时间间隔，通常用于检测超时连接（默认为0，即当开启检测后每次从连接词中获取连接以及放回连接池都需要检测）
     */
    protected int poolPingConnectionsNotUsedFor;

    /**
     * 预期的连接类型代码
     * <p>
     *     就是url+username+password组成的字符串所得到hashCode,
     *     详情请看 {@link PooledConnectionManager#assembleConnectionTypeCode(String, String, String)}
     * </p>
     */
    private int expectedConnectionTypeCode;

    /**
     * 是否不可用
     */
    private boolean isDeprecated;

    /**
     * 连接池管理器 监听器对象
     */
    private PooledConnectionManagerListener pooledConnectionManagerListener;
    /**
     * 连接池管理器状态监听器
     */
    public interface PooledConnectionManagerListener{

        /**
         * 当前连接池 不可用时
         */
        void onPooledDeprecated(PooledConnectionManager pooledConnectionManager);

    }

    public PooledConnectionManager(DataSourceCilentNode dataSourceCilentNode, PooledConnectionManagerListener pooledConnectionManagerListener) {
        super(dataSourceCilentNode.getConnectionPooledId(),
                dataSourceCilentNode.getDriver(),
                dataSourceCilentNode.getUrl(),
                dataSourceCilentNode.getUsername(),
                dataSourceCilentNode.getPassword(),
                dataSourceCilentNode.getDriverProperties());
        this.pooledConnectionManagerListener=pooledConnectionManagerListener;
        setPoolPingQuery(dataSourceCilentNode.getPingSql());
    }

    /**
     * 创建连接以及分配连接，当有空闲连接时则直接使用，否则再根据是否达到了设置的峰值，来决定是否需要创建新的连接等。
     */
    public Connection popConnection() throws SQLException {
        boolean countedWait = false;
        PooledConnection conn=null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;
        while (conn == null) {
            synchronized (state) {
                if (!state.idleConnections.isEmpty()) {// 有空闲连接，直接获取
                    // 池中有可用连接
                    conn = state.idleConnections.remove(0);
                    if (log.isDebugEnabled()) {
                        log.debug(" {} checked out connection {} from pool . ",getUrl(),conn.getRealHashCode());
                    }
                } else {// 空闲连接不足
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        //当前活动连接数 < 最大活动连接数，直接建立新的连接，并封装代理
                        conn = new PooledConnection(getConnection(), this);
                        if (log.isDebugEnabled()) {
                            log.debug(" {} create connection {} . ",getUrl(),conn.getRealHashCode());
                        }
                    }else{
                        // 超出最大活动连接数，不能创建连接
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        // 获取使用时间最长的活动连接，并计算使用的时间
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        // 超出了最大可回收时间，直接回收该连接，
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            // Can claim overdue connection
                            //回收过期次数增加
                            state.claimedOverdueConnectionCount++;
                            // 统计过期回收时间增加
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            // 统计使用时间增加
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            // 将连接从活动队列中移除
                            state.activeConnections.remove(oldestActiveConnection);
                            // 如果不是自动提交事务，则将其回滚，因为可能存在一些操作
                            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                try {
                                    oldestActiveConnection.getRealConnection().rollback();
                                } catch (SQLException e) {
                                  /*
                                     只是调试日志消息然后继续执行下面的语句像什么也没发生一样。
                                     包装一个坏连接的连接和一个新的池连接，这将有助于不中断当前执行中线程和给当前线程一个
                                     加入争端另一个有效或者好的数据库连接。在这个循环的结束，坏的连接会被设置为null。
                                   */
                                  if(log.isDebugEnabled()){
                                      log.debug("Bad connection. Could not roll back");
                                  }
                                }
                            }
                            // 使用新的代理封装，可以使得不会被原有的影响
                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                            //将旧的代理连接创建连接时间戳加到新的代理连接中
                            conn.setCreatedTimestamp(oldestActiveConnection.getCreatedTimestamp());
                            //将旧的代理连接最后一次使用时间戳加到新的代理连接中
                            conn.setLastUsedTimestamp(oldestActiveConnection.getLastUsedTimestamp());
                            // 将旧的的代理连接设置为无效
                            oldestActiveConnection.invalidate();
                            if (log.isDebugEnabled()) {
                                log.debug("Claimed overdue connection " + conn.getRealHashCode() + ".");
                            }
                        }else {
                            // Must wait
                            try {
                                // 增加获取连接需要等待的次数
                                if (!countedWait) {
                                    state.hadToWaitCount++;
                                    countedWait = true;
                                }
                                if (log.isDebugEnabled()) {
                                    log.debug("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                }
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);// 等待
                                // 增加获取连接的等待时间
                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                // 被中断，退出尝试以及等待
                                break;
                            }
                        }
                    }
                }
                if (conn != null) {
                    // ping 服务器并检查是否有效
                    if (conn.isValid()) {
                        // 连接为非自动提交事务，则将其回滚，可能存在一些未提交操作，并且防止影响下一次使用
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }
                        // 根据URL,用户名以及密码计算出一个Hash，用于标识此次连接
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(getUrl(), getUsername(), getPassword()));
                        // 设置当前连接开始使用时间
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        // 设置最后一次使用时间
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        // 加入活动队列中
                        state.activeConnections.add(conn);
                        // 统计请求次数
                        state.requestCount++;
                        // 统计获取连接所需时间
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        //无效连接
                        if (log.isDebugEnabled()) {
                            log.debug("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection.");
                        }
                        // 统计无效连接个数
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        //置空连接，因为已经连接无效了，所以再调用关闭连接方法也不会有反应，所以只需要交给GC回收
                        conn = null;
                        // 如果这个线程获取到的是一个坏的连接，那么这个数据源允许这个线程尝试重新获取一个新的连接但是这个重新尝试的次数不应该超过
                        // (最大空闲连接数poolMaximumIdleConnections)与(坏连接容忍度poolMaximumLocalBadConnectionTolerance)之和。
                        if (localBadConnectionCount > (poolMaximumIdleConnections + poolMaximumLocalBadConnectionTolerance)) {
                            if (log.isDebugEnabled()) {
                                log.debug("PooledDataSource: Could not get a good connection to the database.");
                            }
                            isDeprecated=true;
                            forceCloseAll();
                            pooledConnectionManagerListener.onPooledDeprecated(this);
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }

                    }
                }
            }
        }

        // 从上面的循环退出，如果为null，则一定出现异常情况了
        if (conn == null) {
            if (log.isDebugEnabled()) {
                log.debug("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
            }
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return conn.getProxyConnection();
    }

    /**
     * 根据当前状态来决定放入空闲链表中还是释放掉。
     */
    protected void pushConnection(PooledConnection conn) throws SQLException {

        synchronized (state) {
            //从活动连接中移除该连接
            state.activeConnections.remove(conn);
            if (conn.isValid()) {
                //如果当前线程的空间连接列表 < 最大空闲连接数 && 当前的连接是否变更过（即用户名，密码，Url等变更）
                if (state.idleConnections.size() < poolMaximumIdleConnections && conn.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    //统计连接的检出时长
                    state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    //没有自动提交的话，先回滚，防止影响下一次使用
                    if (!conn.getRealConnection().getAutoCommit()) {
                        conn.getRealConnection().rollback();
                    }
                    // 重新创建一个代理连接来封装，可以使得当前使用的连接不会被原有的代理连接影响
                    PooledConnection newConn = new PooledConnection(conn.getRealConnection(), this);
                    // 放回空闲链表中
                    state.idleConnections.add(newConn);
                    //将原来的代理连接创建时间赋值给新的代理连接,以保持最开始的连接的信息记录
                    newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
                    //将原来的代理连接最后一次连接赋值给新的代理连接,以保持最开始的连接的信息记录
                    newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
                    //将原有的代理连接设置为无效，也使得原代理连接不再发送心跳
                    conn.invalidate();
                    if (log.isDebugEnabled()) {
                        log.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
                    }
                    // 通知等待获取连接的线程（不去判断是否真的有线程在等待）
                    state.notifyAll();
                } else {
                    //统计连接的检出时长
                    state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    //没有自动提交的话，先回滚，防止影响下一次使用
                    if (!conn.getRealConnection().getAutoCommit()) {
                        conn.getRealConnection().rollback();
                    }
                    // 超出空闲连接限制，则直接释放当前连接
                    conn.getRealConnection().close();
                    if (log.isDebugEnabled()) {
                        log.debug("Closed connection " + conn.getRealHashCode() + ".");
                    }
                    // 将原有的代理连接设置为无效
                    conn.invalidate();
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("A bad connection (" + conn.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                }
                // 连接无效，则统计无效连接个数
                state.badConnectionCount++;
            }
        }
    }

    /**
     * 检出连接是否仍然可用
     * <p>
     *     先检查真实连接是否已将关闭并将结果赋值给(变量result)，关闭就直接返回(变量result);否则判断是否启用心跳，没有启用也
     *     直接返回(变量result);否则根据(ping的检查时间间隔poolPingConnectionsNotUsedFor)判断是否可触发检查心跳的逻辑。
     *     心跳逻辑主要通过(参数conn)执行(成员变量poolPingQuery)的SQL脚本，如果执行期间没有发生异常，就认为该连接是可以用的。
     * </p>
     * @param conn - 要检查的连接
     * @return 如果连接仍然可用，则为true
     */
    public boolean pingConnection(PooledConnection conn) {
        boolean result = true;

        //检出连接是否关闭
        try {
            result = !conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
            }
            result = false;
        }

        if (result) {
            if (poolPingEnabled) {
                //ping的检查时间间隔>=0 && 该连接自最后一次使用的时间后到当前时间的时间间隔 > ping的检查时间间隔
                if (poolPingConnectionsNotUsedFor >= 0 && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Testing connection " + conn.getRealHashCode() + " ...");
                        }
                        Connection realConn = conn.getRealConnection();
                        //将poolPingQuery的SQL脚本通过该连接进行发送，poolPingQuery默认的SQL并不是一个可执行SQL所以调用是会报错的，
                        // 所以要对poolPingQuery进行修改，由于poolPingQuery的SQL脚本只是用于心跳，所以用对数据库最不耗时，资源最小
                        //的SQL脚本。
                        try (Statement statement = realConn.createStatement()) {
                            statement.executeQuery(poolPingQuery).close();
                        }
                        //如果在连接没有明确自动提交的情况下，默认还是回退的，因为这样能保证数据库不会因为心跳的脚本到时影响到业务逻辑。
                        //正式因为默认回退，所以poolPingQuery如果是insert,delete,update也不会真正的修改到数据库数据
                        if (!realConn.getAutoCommit()) {
                            realConn.rollback();
                        }
                        result = true;
                        if (log.isDebugEnabled()) {
                            log.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
                        }
                    } catch (Exception e) {
                        log.warn("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                        try {
                            //在出现异常的情况下，会抛出关闭真实连接。哪怕poolPingQuery执行抛出的异常，也会关闭连接，
                            //所以poolPingQuery必须保证是正确的，否则真实连接关闭。
                            conn.getRealConnection().close();
                        } catch (Exception e2) {
                            //ignore
                        }
                        result = false;
                        if (log.isDebugEnabled()) {
                            log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 构造 (预期的连接类型代码assembleConnectionTypeCode)
     * <p>
     *     拼装(参数url)+(参数username)+(参数password)得到一个字符串，并调用该字符串的{@link String#hashCode()}取到
     *     其hashCode返回出去。
     * </p>
     */
    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    /**
     * Closes all active and idle connections in the pool.
     * 关闭连接池中所有的活动连接和空闲连接
     */
    public void forceCloseAll() {
        synchronized (state) {
            expectedConnectionTypeCode = assembleConnectionTypeCode(getUrl(), getUsername(), getPassword());
            //对活动的连接逐一关闭
            //这里的循环很巧妙，非常值得我去学习。
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    conn.invalidate();//设置无效
                    // 如果不是自动提交事务，则将其回滚，因为可能存在一些操作
                    Connection realConn = conn.getRealConnection();
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    realConn.close();//这里是关闭真实连接，并没有不是代理连接。
                } catch (Exception e) {
                    // ignore
                }
            }
            for (int i = state.idleConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    conn.invalidate();//设置无效
                    // 如果不是自动提交事务，则将其回滚，因为可能存在一些操作
                    Connection realConn = conn.getRealConnection();
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    realConn.close();//这里是关闭真实连接，并没有不是代理连接。
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("PooledDataSource forcefully closed/removed all connections.");
        }
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }

    public PoolState getState() {
        return state;
    }

    public int getPoolMaximumActiveConnections() {
        return poolMaximumActiveConnections;
    }

    public int getPoolMaximumIdleConnections() {
        return poolMaximumIdleConnections;
    }

    public int getPoolMaximumCheckoutTime() {
        return poolMaximumCheckoutTime;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public int getPoolMaximumLocalBadConnectionTolerance() {
        return poolMaximumLocalBadConnectionTolerance;
    }

    public String getPoolPingQuery() {
        return poolPingQuery;
    }

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    /**
     * 设置自动提交
     * <p>
     *     直接调用 {@link PooledConnectionManager#setAutoCommit(Boolean)} ,并且调用 {@link PooledConnectionManager#forceCloseAll()},
     *     虽然我认为关闭连接池中的所有连接也行，因为可以调用所有连接的{@link PooledConnectionManager#setAutoCommit(Boolean)}
     *     但是既然写了，肯定有作者的用意吧，毕竟对应更改配置的事情，还是关闭重新来过好一点
     * </p>
     */
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        setAutoCommit(defaultAutoCommit);
        forceCloseAll();
    }

    /**
     * 设置事务隔离级别
     * <p>
     *     直接调用 {@link PooledConnectionManager#setDefaultTransactionIsolationLevel(Integer)} ,
     *     并且调用 {@link PooledConnectionManager#forceCloseAll()},
     * </p>
     */
    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        super.setDefaultTransactionIsolationLevel(defaultTransactionIsolationLevel);
        forceCloseAll();
    }

    /**
     * 设置驱动属性
     * <p>
     *     直接调用 {@link PooledConnectionManager#setDriverProperties(Properties)} ,
     *     并且调用 {@link PooledConnectionManager#forceCloseAll()},
     * </p>
     */
    public void setDriverProperties(Properties driverProps) {
        super.setDriverProperties(driverProps);
        forceCloseAll();
    }

    /**
     * Sets the default network timeout value to wait for the database operation to complete. See {@link Connection#setNetworkTimeout(java.util.concurrent.Executor, int)}
     * 设置网络连接超时时长
     * <p>
     *     直接调用 {@link PooledConnectionManager#setDefaultNetworkTimeout(Integer)} ,
     *      并且调用 {@link PooledConnectionManager#forceCloseAll()},
     * </p>
     * @param milliseconds
     *          The time in milliseconds to wait for the database operation to complete.
     * @since 3.5.2
     */
    public void setDefaultNetworkTimeout(Integer milliseconds) {
        super.setDefaultNetworkTimeout(milliseconds);
        forceCloseAll();
    }

    /**
     * The maximum number of active connections.
     * 设置池的最大活动连接数
     * @param poolMaximumActiveConnections The maximum number of active connections
     */
    public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
        this.poolMaximumActiveConnections = poolMaximumActiveConnections;
        forceCloseAll();
    }

    /**
     * The maximum number of idle connections.
     * 设置池的最大空闲连接数
     * @param poolMaximumIdleConnections The maximum number of idle connections
     */
    public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
        this.poolMaximumIdleConnections = poolMaximumIdleConnections;
        forceCloseAll();
    }

    /**
     * The maximum number of tolerance for bad connection happens in one thread
     * which are applying for new {@link PooledConnection}.
     * 设置 坏连接容忍度poolMaximumLocalBadConnectionTolerance
     * @param poolMaximumLocalBadConnectionTolerance
     * max tolerance for bad connection happens in one thread
     *
     * @since 3.4.5
     */
    public void setPoolMaximumLocalBadConnectionTolerance(
            int poolMaximumLocalBadConnectionTolerance) {
        this.poolMaximumLocalBadConnectionTolerance = poolMaximumLocalBadConnectionTolerance;
    }

    /**
     * The maximum time a connection can be used before it *may* be
     * given away again.
     * 设置最大可回收时间
     */
    public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
        this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
        forceCloseAll();
    }

    /**
     * The time to wait before retrying to get a connection.
     * 设置 在无法获取连接时，线程需要等待的时间
     * @param poolTimeToWait The time to wait
     */
    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
        forceCloseAll();
    }

    /**
     * The query to be used to check a connection.
     * 设置 （心跳执行的SQL语句poolPingQuery）
     * @param poolPingQuery The query
     */
    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
        forceCloseAll();
    }

    /**
     * Determines if the ping query should be used.
     * 设置 （心跳是否启用poolPingEnabled）
     * @param poolPingEnabled True if we need to check a connection before using it
     */
    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
        forceCloseAll();
    }

    /**
     * If a connection has not been used in this many milliseconds, ping the
     * database to make sure the connection is still good.
     * 设置 （心跳的时间间隔poolPingConnectionsNotUsedFor）
     * @param milliseconds the number of milliseconds of inactivity that will trigger a ping
     */
    public void setPoolPingConnectionsNotUsedFor(int milliseconds) {
        this.poolPingConnectionsNotUsedFor = milliseconds;
        forceCloseAll();
    }

}
