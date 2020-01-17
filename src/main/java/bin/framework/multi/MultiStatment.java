package bin.framework.multi;


import java.sql.*;
import java.util.List;

public class MultiStatment implements Statement {

    private List<Statement> realStatements;

    private Connection connection;

    public MultiStatment(List<Statement> realStatements,Connection connection) {
        this.realStatements = realStatements;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return realStatements.get(0).executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.executeUpdate(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call executeUpdate(String sql)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void close() throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.close();
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.getMaxFieldSize();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call getMaxFieldSize()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.setMaxFieldSize(max);
        }
    }

    @Override
    public int getMaxRows() throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.getMaxRows();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call getMaxRows()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.setMaxRows(max);
        }
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.setEscapeProcessing(enable);
        }
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.getQueryTimeout();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call getQueryTimeout()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        getFirstRealStatement().setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.cancel();
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        //因为默认认为MultiStatment的realStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接去第一个Statment的SQLWarning对象
        return realStatements.get(0).getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.clearWarnings();
        }
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.clearWarnings();
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.execute(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call execute(String sql)  ");
            }
        }
        return firstResult;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        //因为默认认为MultiStatment的realStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接去第一个Statment的ResultSet对象
        return realStatements.get(0).getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.getUpdateCount();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call getUpdateCount()  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return realStatements.get(0).getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        realStatements.get(0).setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return   realStatements.get(0).getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        realStatements.get(0).setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return realStatements.get(0).getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return getFirstRealStatement().getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return getFirstRealStatement().getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.addBatch(sql);
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.clearBatch();
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        int[] firstResult=null;
        for(Statement statement: realStatements){
            int[] result=statement.executeBatch();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call executeBatch()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return getFirstRealStatement().getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        //因为默认认为MulitPreparedStatement的realPrepareStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以自动生成出来的主键也应该是一致才行。
        ResultSet firstResult=null;
        Object firstObj=null;
        Object defaultObj=new Object();
        for(Statement statement: realStatements){
            ResultSet result=statement.getGeneratedKeys();
            if(result.next()){
                Object object = result.getObject(1);
                if(firstObj==null) {
                    firstObj=object;
                    firstResult=result;
                    continue;
                }
                if( !firstObj.equals(object)){
                    throw new SQLException(" list of realStatements return result is inconsistent on call getGeneratedKeys() ");
                }
            }else{
                firstObj=defaultObj;
                firstResult=result;
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.executeUpdate(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call executeUpdate(String sql, int autoGeneratedKeys)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.executeUpdate(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call executeUpdate(String sql, int[] columnIndexes)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        Integer firstResult=null;
        for(Statement statement: realStatements){
            int result=statement.executeUpdate(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call executeUpdate(String sql, String[] columnNames)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.execute(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call execute(String sql, int autoGeneratedKeys)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.execute(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call execute(String sql, int[] columnIndexes)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.execute(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call execute(String sql, String[] columnNames)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return getFirstRealStatement().getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.isClosed();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call isClosed() ");
            }
        }
        return firstResult;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.setPoolable(poolable);
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.isPoolable();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call isPoolable() ");
            }
        }
        return firstResult;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        for (Statement statement:
                realStatements) {
            statement.closeOnCompletion();
        }
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.isCloseOnCompletion();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call isCloseOnCompletion() ");
            }
        }
        return firstResult;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        T firstResult=null;
        for(Statement statement: realStatements){
            T result=statement.unwrap(iface);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call unwrap(Class<T> iface) ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        Boolean firstResult=null;
        for(Statement statement: realStatements){
            boolean result=statement.isWrapperFor(iface);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStatements return result is inconsistent on call isWrapperFor(Class<T> iface) ");
            }
        }
        return firstResult;
    }

    public Statement getFirstRealStatement(){
        return realStatements.get(0);
    }
}
