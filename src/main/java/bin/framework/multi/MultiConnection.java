package bin.framework.multi;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MultiConnection implements Connection {

    private List<Connection> realConnections;


    public MultiConnection(List<Connection> realConnections) throws SQLException {
        this.realConnections = realConnections;
    }


    @Override
    public Statement createStatement() throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        List<CallableStatement> callableStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatements.add(callableStatement);
        }
        return new MultiCallableStatment(callableStatements,this);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getFirstRealConnection().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        for(Connection connection:realConnections){
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        Boolean firstResult=null;
        for(Connection connection: realConnections){
            boolean result=connection.getAutoCommit();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getAutoCommit()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void commit() throws SQLException {
        for(Connection connection:realConnections){
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        for(Connection connection:realConnections){
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        for(Connection connection:realConnections){
            connection.close();
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        Boolean firstResult=null;
        for(Connection connection: realConnections){
            boolean result=connection.isClosed();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  isClosed()  ");
            }
        }
        return firstResult;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        //因为默认认为MultiConnection的realConnection对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接返回queryConnection的DatabaseMetaData对象
        return getFirstRealConnection().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        for(Connection connection:realConnections){
            connection.setReadOnly(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        Boolean firstResult=null;
        for(Connection connection: realConnections){
            boolean result=connection.isReadOnly();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  isReadOnly()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        for(Connection connection:realConnections){
            connection.setCatalog(catalog);
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        String firstResult=null;
        for(Connection connection: realConnections){
            String result=connection.getCatalog();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getCatalog()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        for(Connection connection:realConnections){
            connection.setTransactionIsolation(level);
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        Integer firstResult=null;
        for(Connection connection: realConnections){
            int result=connection.getTransactionIsolation();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getTransactionIsolation()  ");
            }
        }
        return firstResult;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        //因为默认认为MultiConnection的realConnection对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接返回queryConnection的SQLWarning对象
        return getFirstRealConnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        for(Connection connection:realConnections){
            connection.clearWarnings();
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql,resultSetType,resultSetConcurrency);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        List<CallableStatement> callableStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            CallableStatement callableStatement = connection.prepareCall(sql,resultSetType,resultSetConcurrency);
            callableStatements.add(callableStatement);
        }
        return new MultiCallableStatment(callableStatements,this);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        Map<String,Class<?>> firstResult=null;
        for(Connection connection: realConnections){
            Map<String,Class<?>> result=connection.getTypeMap();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getTypeMap()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        for(Connection connection:realConnections){
            connection.setTypeMap(map);
        }
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        for(Connection connection:realConnections){
            connection.setHoldability(holdability);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        Integer firstResult=null;
        for(Connection connection: realConnections){
            int result=connection.getHoldability();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getHoldability()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        List<Savepoint> savepoints=new LinkedList<>();
        for(Connection connection:realConnections){
            savepoints.add(connection.setSavepoint());
        }
        return new MultiSavepoint(savepoints);
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        List<Savepoint> savepoints=new LinkedList<>();
        for(Connection connection:realConnections){
            savepoints.add(connection.setSavepoint(name));
        }
        return new MultiSavepoint(savepoints);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        for(Connection connection:realConnections){
            connection.rollback(savepoint);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        for(Connection connection:realConnections){
            connection.releaseSavepoint(savepoint);
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        List<CallableStatement> callableStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            CallableStatement callableStatement = connection.prepareCall(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
            callableStatements.add(callableStatement);
        }
        return new MultiCallableStatment(callableStatements,this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql,autoGeneratedKeys);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql,columnIndexes);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        List<PreparedStatement> preparedStatements=new LinkedList<>();
        for(Connection connection: realConnections){
            PreparedStatement preparedStatement = connection.prepareStatement(sql,columnNames);
            preparedStatements.add(preparedStatement);
        }
        return new MulitPreparedStatment(preparedStatements,this);
    }

    @Override
    public Clob createClob() throws SQLException {
        List<Clob> clobs=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            clobs.add(connection.createClob());
        }
        return new MultiClob(clobs);
    }

    @Override
    public Blob createBlob() throws SQLException {
        List<Blob> blobs=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            blobs.add(connection.createBlob());
        }
        return new MultiBlob(blobs);
    }

    @Override
    public NClob createNClob() throws SQLException {
        List<Clob> clobs=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            clobs.add(connection.createNClob());
        }
        return new MultiNClob(clobs);
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        List<SQLXML> sqlxmls=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            sqlxmls.add(connection.createSQLXML());
        }
        return new MultiSQLXML(sqlxmls);
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        Boolean firstResult=null;
        for(Connection connection: realConnections){
            boolean result=connection.isValid(timeout);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  isValid(int timeout)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        for (Connection connection:
                realConnections) {
            connection.setClientInfo(name,value);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        for (Connection connection:
                realConnections) {
            connection.setClientInfo(properties);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        String firstResult=null;
        for(Connection connection: realConnections){
            String result=connection.getClientInfo(name);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call getClientInfo(String name) ");
            }
        }
        return firstResult;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        Properties firstResult=null;
        for(Connection connection: realConnections){
            Properties result=connection.getClientInfo();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getClientInfo()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        List<Array> arrays=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            arrays.add(connection.createArrayOf(typeName,elements));
        }
        return new MultiArray(arrays);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        List<Struct> arrays=new LinkedList<>();
        for (Connection connection:
                realConnections) {
            arrays.add(connection.createStruct(typeName,attributes));
        }
        return new MultiStruct(arrays);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        for (Connection connection:
                realConnections) {
            connection.setSchema(schema);
        }
    }

    @Override
    public String getSchema() throws SQLException {
        String firstResult=null;
        for(Connection connection: realConnections){
            String result=connection.getSchema();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getSchema()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        for (Connection connection:
                realConnections) {
            connection.abort(executor);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        for (Connection connection:
                realConnections) {
            connection.setNetworkTimeout(executor,milliseconds);
        }
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        Integer firstResult=null;
        for(Connection connection: realConnections){
            int result=connection.getNetworkTimeout();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  getNetworkTimeout()  ");
            }
        }
        return firstResult;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        T firstResult=null;
        for(Connection connection: realConnections){
            T result=connection.unwrap(iface);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  unwrap(Class<T> iface)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        Boolean firstResult=null;
        for(Connection connection: realConnections){
            boolean result=connection.isWrapperFor(iface);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realConnections return result is inconsistent on call  isWrapperFor(Class<T> iface)  ");
            }
        }
        return firstResult;
    }

    public Connection getFirstRealConnection(){
        return getFirstRealConnection();
    }
}
