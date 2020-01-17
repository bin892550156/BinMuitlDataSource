package bin.framework;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

public class BinMultiDataSource implements DataSource {


    private ConnectionFinder connectionFinder;

    public BinMultiDataSource(List<DataSourceCilentNode> dataSourceCilentNodeList) {// from [groupId]table
        connectionFinder=new ConnectionFinder(dataSourceCilentNodeList,null);
    }

    public BinMultiDataSource(List<DataSourceCilentNode> dataSourceCilentNodeList, DataSourceProp dataSourceProp) {// from [groupId]table
        connectionFinder=new ConnectionFinder(dataSourceCilentNodeList,dataSourceProp==null?new DataSourceProp():dataSourceProp);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new BinConnection(connectionFinder);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        //因为该DataSource是对应多个数据库系统的，所以这里的username和password已经没有意义，因为没法确定是username和password
        //是对应哪个数据库系统
        return new BinConnection(connectionFinder);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        //本类不是一个包装类，不提供解开方法，调用该方法会抛出 {@link SQLException}
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        //本不是一个包装类，不会为任何类进行包装，这里写死返回false
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }


}
