package bin.framework.pool;

import org.apache.ibatis.io.Resources;

import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public abstract class AbstarctConnectionManager {

    protected static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();

    /**
     * 连接池ID
     */
    private String connectionPoolId;
    /**
     * 当前驱动
     */
    private String driver;

    /**
     * 当前数据库url
     */
    private String url;

    /**
     * 当前数据库用户名
     */
    private String username;

    /**
     * 当前数据库密码
     */
    private String password;

    /**
     * 驱动属性
     */
    private Properties driverProperties;

    /**
     * 是否自动提交
     */
    private Boolean autoCommit;
    /**
     * 默认事务等级
     */
    private Integer defaultTransactionIsolationLevel;
    /**
     * 默认超时时长
     */
    private Integer defaultNetworkTimeout;

    /**
     * 驱动类加载器
     */
    private ClassLoader driverClassLoader;



    public AbstarctConnectionManager(String connectionPoolId, String driver, String url, String username, String password, Properties driverProperties) {
        this.connectionPoolId = connectionPoolId;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverProperties = driverProperties;
    }

    /**
     * 当类加载的时候，就从DriverManager中获取所有的驱动信息，放到当前维护的Map中,
     * 拿到的是项目内的所有数据库驱动，
     * https://www.jianshu.com/p/f5f677826715
     */
    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            registeredDrivers.put(driver.getClass().getName(), driver);
        }
    }

    /**
     * 获取连接
     */
    protected Connection getConnection() throws SQLException {
        Properties properties = mergeProp();
        initializeDriver(driver);
        Connection connection = DriverManager.getConnection(url, properties);
        configureConnection(connection);
        return connection;
    }

    private Properties mergeProp(){
        Properties props = new Properties();
        if (driverProperties != null) {
            props.putAll(driverProperties);
        }
        if (username != null) {
            props.setProperty("user", username);
        }
        if (password != null) {
            props.setProperty("password", password);
        }
        return props;
    }

    /**
     * 初始化驱动信息,只是对driver进行了是否注册到DriverManager和registerDrivers的判断，没有就是尝试注册，注册不成功就抛出异常。
     */
    private synchronized void initializeDriver(String driver) throws SQLException {
        // 如果没有包含在已注册Map中，则需要将该驱动加载进来
        if (!registeredDrivers.containsKey(driver)) {
            Class<?> driverType;
            try {
                // 加载数据库连接驱动
                if (driverClassLoader != null) {
                    driverType = Class.forName(driver, true, driverClassLoader);
                } else {
                    // Resources为MyBatis内置的资源工具类，该方法依次尝试从多个ClassLoader中获取Class类，
                    driverType = Resources.classForName(driver);
                }
                // DriverManager requires the driver to be loaded via the system ClassLoader.
                // http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
                // 创建驱动实例
                Driver driverInstance = (Driver)driverType.newInstance();
                // 注册到DriverManager中，用于创建数据库连接
                DriverManager.registerDriver(new DriverProxy(driverInstance));
                registeredDrivers.put(driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
            }
        }
    }

    /**
     * 设置是否自动提交，默认网络超时时长，默认事务隔离等级
     */
    private void configureConnection(Connection conn) throws SQLException {
        if (defaultNetworkTimeout != null) {
            conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), defaultNetworkTimeout);
        }
        if (autoCommit != null && autoCommit != conn.getAutoCommit()) {//如果autoCommit与conn.getAutoCommit不一致的情况下设置
            conn.setAutoCommit(autoCommit);
        }
        if (defaultTransactionIsolationLevel != null) {
            conn.setTransactionIsolation(defaultTransactionIsolationLevel);
        }
    }


    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Integer getDefaultTransactionIsolationLevel() {
        return defaultTransactionIsolationLevel;
    }

    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    }

    public Integer getDefaultNetworkTimeout() {
        return defaultNetworkTimeout;
    }

    public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
        this.defaultNetworkTimeout = defaultNetworkTimeout;
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getDriverProperties() {
        return driverProperties;
    }

    public void setDriverProperties(Properties driverProperties) {
        this.driverProperties = driverProperties;
    }

    public String getConnectionPoolId() {
        return connectionPoolId;
    }

    public void setConnectionPoolId(String connectionPoolId) {
        this.connectionPoolId = connectionPoolId;
    }

    /**
     * 驱动代理类，只是重写getParentLogger方法设置Logger
     */
    private static class DriverProxy implements Driver {
        private Driver driver;

        DriverProxy(Driver d) {
            this.driver = d;
        }

        @Override
        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        @Override
        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        @Override
        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        @Override
        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        /**
         * 设置java.util.Logger
         * @return
         */
        @Override
        public Logger getParentLogger() {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }
}
