/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package bin.framework.pool;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 池的连接代理类
 * @author Clinton Begin
 */
class PooledConnection implements InvocationHandler {

  private static final String CLOSE = "close";


  private static final Class<?>[] IFACES = new Class<?>[] { Connection.class };

  /**
   * (真实连接realConnection)的hashCode
   */
  private final int hashCode;
  /**
   * 连接池
   */
  private final PooledConnectionManager pooledConnectionManager;

  /**
   * 真实连接
   */
  private final Connection realConnection;

  /**
   * 代理连接
   */
  private final Connection proxyConnection;

  /**
   * 检出时间戳
   */
  private long checkoutTimestamp;

  /**
   * 创建时间戳
   */
  private long createdTimestamp;

  /**
   * 最后使用时间戳
   */
  private long lastUsedTimestamp;

  /**
   * 连接类型代码
   */
  private int connectionTypeCode;
  /**
   * 连接是否有效，默认一开始为true
   */
  private boolean valid;

  /**
   * Constructor for SimplePooledConnection that uses the Connection and PooledDataSource passed in.
   *
   * @param connection - the connection that is to be presented as a pooled connection
   * @param pooledConnectionManager - the dataSource that the connection is from
   */
  public PooledConnection(Connection connection, PooledConnectionManager pooledConnectionManager) {
    this.hashCode = connection.hashCode();
    this.realConnection = connection;
    this.pooledConnectionManager = pooledConnectionManager;
    this.createdTimestamp = System.currentTimeMillis();
    this.lastUsedTimestamp = System.currentTimeMillis();
    this.valid = true;
    this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
  }

  /**
   * Invalidates the connection.
   * 设置该连接无效
   */
  public void invalidate() {
    valid = false;
  }

  /**
   * Method to see if the connection is usable.
   * 检查连接是否可用
   * <p>
   *     先判断(成员变量valid,[是否连接有效])是否为true && 判断(成员变量realConnection,[真实连接])是否为空
   *     && 调用 {@link PooledConnectionManager#pingConnection(PooledConnection)}
   * </p>
   * @return True if the connection is usable
   */
  public boolean isValid() {
    return valid && realConnection != null && pooledConnectionManager.pingConnection(this);
  }

  /**
   * Getter for the *real* connection that this wraps.
   * 获取真实连接
   * @return The connection
   */
  public Connection getRealConnection() {
    return realConnection;
  }

  /**
   * Getter for the proxy for the connection.
   * 获取代理连接
   * @return The proxy
   */
  public Connection getProxyConnection() {
    return proxyConnection;
  }

  /**
   * Gets the hashcode of the real connection (or 0 if it is null).
   * 获取（真实连接realConnection)的hastCode
   * <p>
   *     如果（真实连接realConnection)为null,会直接返回0；否则，返回（真实连接realConnection)的 {@link Connection#hashCode()} 结果
   * </p>
   * @return The hashcode of the real connection (or 0 if it is null)
   */
  public int getRealHashCode() {
    return realConnection == null ? 0 : realConnection.hashCode();
  }

  /**
   * Getter for the connection type (based on url + user + password).
   * 获取(连接类型代码 {@link PooledConnection#connectionTypeCode})
   * <p>
   *     如果是由 {@link PooledDataSource} 构建出来的 {@link PooledConnection},
   *     {@link PooledConnection#connectionTypeCode} = {@link PooledDataSource#expectedConnectionTypeCode}
   * </p>
   * @return The connection type
   */
  public int getConnectionTypeCode() {
    return connectionTypeCode;
  }

  /**
   * Setter for the connection type.
   * 设置(连接类型代码connectionTypeCode)
   * @param connectionTypeCode - the connection type
   */
  public void setConnectionTypeCode(int connectionTypeCode) {
    this.connectionTypeCode = connectionTypeCode;
  }

  /**
   * Getter for the time that the connection was created.
   * 获取(创建连接时间戳createdTimestamp)
   * @return The creation timestamp
   */
  public long getCreatedTimestamp() {
    return createdTimestamp;
  }

  /**
   * Setter for the time that the connection was created.
   * 设置 (创建连接时间戳createdTimestamp)
   * @param createdTimestamp - the timestamp
   */
  public void setCreatedTimestamp(long createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }

  /**
   * Getter for the time that the connection was last used.
   * 获取(最后一次使用时间戳lastUsedTimestamp)
   * @return - the timestamp
   */
  public long getLastUsedTimestamp() {
    return lastUsedTimestamp;
  }

  /**
   * Setter for the time that the connection was last used.
   * 设置(最后一次使用时间戳lastUsedTimestamp)
   * @param lastUsedTimestamp - the timestamp
   */
  public void setLastUsedTimestamp(long lastUsedTimestamp) {
    this.lastUsedTimestamp = lastUsedTimestamp;
  }

  /**
   * Getter for the time since this connection was last used.
   * 获取自最后一次使用的时间后到当前时间的时间间隔
   * <p>
   *     获取自最后一次使用的时间后到当前时间的时间间隔 = 当前时间戳 - (最后一次使用时间戳lastUsedTimestamp)
   * </p>
   * @return - the time since the last use
   */
  public long getTimeElapsedSinceLastUse() {
    return System.currentTimeMillis() - lastUsedTimestamp;
  }

  /**
   * Getter for the age of the connection.
   * 获取连接存活的时长
   * <p>
   *     当前时间戳 - (创建连接时间戳createdTimestamp)
   * </p>
   * @return the age
   */
  public long getAge() {
    return System.currentTimeMillis() - createdTimestamp;
  }

  /**
   * Getter for the timestamp that this connection was checked out.
   * 获取（检出时间戳checkoutTimestamp）
   * @return the timestamp
   */
  public long getCheckoutTimestamp() {
    return checkoutTimestamp;
  }

  /**
   * Setter for the timestamp that this connection was checked out.
   * 设置（检出时间戳checkoutTimestamp）
   * @param timestamp the timestamp
   */
  public void setCheckoutTimestamp(long timestamp) {
    this.checkoutTimestamp = timestamp;
  }

  /**
   * Getter for the time that this connection has been checked out.
   * 获取已检出后的时长
   * <p>
   *     当前时间戳 - 检出时间戳checkoutTimestamp）
   * </p>
   * @return the time
   */
  public long getCheckoutTime() {
    return System.currentTimeMillis() - checkoutTimestamp;
  }

  /**
   * 返回(真实连接realConnection)的hashCode
   * @return
   */
  @Override
  public int hashCode() {
    return hashCode;
  }

  /**
   * Allows comparing this connection to another.
   * 判断连接是否相等
   * <ul>
   *     <li>
   *       如果(参数obj)是属于 {@link PooledConnection}的，会将(参数obj)强转为 {@link PooledConnection},
   *       然后获取 {@link PooledConnection#realConnection} 的 hashCode 与当前类的  {@link PooledConnection#realConnection}
   *       进行比较是否相等。
   *     </li>
   *     <li>
   *        如果(参数obj)是属于 {@link Connection}的，会直接去(参数obj)的 {@link Object#hashCode()}进行比较是否相等。
   *     </li>
   *     <li>
   *          否则：直接返回false
   *     </li>
   * </ul>
   * <p>
   * @param obj - the other connection to test for equality
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PooledConnection) {
      return realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode();
    } else if (obj instanceof Connection) {
      return hashCode == obj.hashCode();
    } else {
      return false;
    }
  }

  /**
   * Required for InvocationHandler implementation.
   * 对 {@link Connection} 的所有方法进行拦截。
   * <p>
   *     若是调用的是 {@link Connection#close()} ,不会直接 {@link Connection#close()} ,而是会交给
   *     {@link PooledConnectionManager#pushConnection(PooledConnection)} 处理,
   *     而调用 {@link Connection} 的其他方法，都会对 调用 {@link PooledConnection#checkConnection()} 进行检查该连接是否有效
   *     再对调用 {@link Connection#close()} 方法
   * </p>
   * @param proxy  - 目标对象
   * @param method - 被执行方法
   * @param args   - 被执行方法的参数
   * @see InvocationHandler#invoke(Object, Method, Object[])
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName();
    //判断是不是调用 {@link Connection#close()} 方法，这里 hashCode判断能够加快判断脚本
    //因为当hashCode不相等的情况下，equal一定不相等；但是hashCode相等的情况下，equal未必相等
    if (CLOSE.hashCode() == methodName.hashCode() && CLOSE.equals(methodName)) {
      pooledConnectionManager.pushConnection(this);
      return null;
    }
    try {
      //调用当前类的所有方法，都会检查连接是否有效，若无效会抛出异常
      if (!Object.class.equals(method.getDeclaringClass())) {
        // issue #579 toString() should never fail
        // throw an SQLException instead of a Runtime
        checkConnection();
      }
      return method.invoke(realConnection, args);
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }

  }

  /**
   * 检查连接是否有效
   * <p>
   *     判断 {@link PooledConnection#valid} ,若false，抛出 {@link SQLException}
   * </p>
   */
  private void checkConnection() throws SQLException {
    if (!valid) {
      throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
    }
  }

}
