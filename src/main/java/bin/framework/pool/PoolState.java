package bin.framework.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接池状态
 * <p>
 *     用于维护活动连接，空闲连接，以及统计一些连接信息
 * </p>
 * @author Clinton Begin
 */
public class PoolState {

  /**
   * 数据源
   */
  protected PooledConnectionManager pooledConnectionManager;

  /**
   * 空闲连接集合
   */
  protected final List<PooledConnection> idleConnections = new ArrayList<>();
  /**
   * 当前活动连接
   */
  protected final List<PooledConnection> activeConnections = new ArrayList<>();
  /**
   * 请求次数
   */
  protected long requestCount = 0;
  /**
   * 请求获得连接所需时间
   */
  protected long accumulatedRequestTime = 0;
  /**
   * 统计连接使用时间
   */
  protected long accumulatedCheckoutTime = 0;
  /**
   * 统计过期回收连接数
   */
  protected long claimedOverdueConnectionCount = 0;
  /**
   * 统计连接过期使用时间
   */
  protected long accumulatedCheckoutTimeOfOverdueConnections = 0;
  /**
   * 统计获取连接需要等待的时间
   */
  protected long accumulatedWaitTime = 0;
  /**
   * 统计获取连接需要等待的次数
   */
  protected long hadToWaitCount = 0;
  /**
   * 统计无效连接个数
   */
  protected long badConnectionCount = 0;

  public PoolState(PooledConnectionManager pooledConnectionManager) {
    this.pooledConnectionManager = pooledConnectionManager;
  }

  /**
   * 获取请求次数
   */
  public synchronized long getRequestCount() {
    return requestCount;
  }

  /**
   * 获取平均请求时间
   * <p>
   *     平均请求时间 = (统计连接使用时间accumulatedRequestTime) / (请求次数requestCount)
   * </p>
   * @return
   */
  public synchronized long getAverageRequestTime() {
    return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
  }

  /**
   * 获取平均等待时间
   * <p>
   *    平均等待时间 = (统计获取连接需要等待的时间accumulatedWaitTime)/(请求次数requestCount)
   * </p>
   * @return
   */
  public synchronized long getAverageWaitTime() {
    return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;

  }

  /**
   * 获取 统计获取连接需要等待的次数
   */
  public synchronized long getHadToWaitCount() {
    return hadToWaitCount;
  }

  /**
   * 获取 统计无效连接个数
   */
  public synchronized long getBadConnectionCount() {
    return badConnectionCount;
  }

  /**
   * 获取 统计过期回收连接数
   */
  public synchronized long getClaimedOverdueConnectionCount() {
    return claimedOverdueConnectionCount;
  }

  /**
   * 获取 统计连接过期使用时间
   */
  public synchronized long getAverageOverdueCheckoutTime() {
    return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
  }

  /**
   * 获取平均连接使用的时间
   * <p>
   *     平均连接使用的时间 = (统计连接使用时间accumulatedCheckoutTime) / (请求次数requestCount)
   * </p>
   */
  public synchronized long getAverageCheckoutTime() {
    return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
  }

  /**
   * 获取 当前空闲连接数
   * <p>
   *     直接调用 (当前空闲连接idleConnections) 的 {@link List#size()} 方法
   * </p>
   */
  public synchronized int getIdleConnectionCount() {
    return idleConnections.size();
  }

  /**
   * 获取 当前活动连接数
   * <p>
   *     直接调用 (当前活动连接activeConnections) 的 {@link List#size()} 方法
   * </p>
   */
  public synchronized int getActiveConnectionCount() {
    return activeConnections.size();
  }

  @Override
  public synchronized String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("\n===CONFINGURATION==============================================");
    builder.append("\n connectionPoolId               ").append(pooledConnectionManager.getConnectionPoolId());
    builder.append("\n jdbcDriver                     ").append(pooledConnectionManager.getDriver());
    builder.append("\n jdbcUrl                        ").append(pooledConnectionManager.getUrl());
    builder.append("\n jdbcUsername                   ").append(pooledConnectionManager.getUsername());
    builder.append("\n jdbcPassword                   ").append(pooledConnectionManager.getPassword() == null ? "NULL" : "************");
    builder.append("\n poolMaxActiveConnections       ").append(pooledConnectionManager.poolMaximumActiveConnections);
    builder.append("\n poolMaxIdleConnections         ").append(pooledConnectionManager.poolMaximumIdleConnections);
    builder.append("\n poolMaxCheckoutTime            ").append(pooledConnectionManager.poolMaximumCheckoutTime);
    builder.append("\n poolTimeToWait                 ").append(pooledConnectionManager.poolTimeToWait);
    builder.append("\n poolPingEnabled                ").append(pooledConnectionManager.poolPingEnabled);
    builder.append("\n poolPingQuery                  ").append(pooledConnectionManager.poolPingQuery);
    builder.append("\n poolPingConnectionsNotUsedFor  ").append(pooledConnectionManager.poolPingConnectionsNotUsedFor);
    builder.append("\n ---STATUS-----------------------------------------------------");
    builder.append("\n activeConnections              ").append(getActiveConnectionCount());
    builder.append("\n idleConnections                ").append(getIdleConnectionCount());
    builder.append("\n requestCount                   ").append(getRequestCount());
    builder.append("\n averageRequestTime             ").append(getAverageRequestTime());
    builder.append("\n averageCheckoutTime            ").append(getAverageCheckoutTime());
    builder.append("\n claimedOverdue                 ").append(getClaimedOverdueConnectionCount());
    builder.append("\n averageOverdueCheckoutTime     ").append(getAverageOverdueCheckoutTime());
    builder.append("\n hadToWait                      ").append(getHadToWaitCount());
    builder.append("\n averageWaitTime                ").append(getAverageWaitTime());
    builder.append("\n badConnectionCount             ").append(getBadConnectionCount());
    builder.append("\n===============================================================");
    return builder.toString();
  }

}
