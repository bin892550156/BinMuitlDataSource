package bin.framework.multi;

import bin.framework.exception.BinMultiDataSourceException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MulitPreparedStatment implements PreparedStatement {

    private MultiConnection multiConnection;

    private List<PreparedStatement> realPrepareStatments;


    public MulitPreparedStatment(List<PreparedStatement> realPrepareStatments,
                                 MultiConnection multiConnection){
        this.realPrepareStatments =realPrepareStatments;
        this.multiConnection=multiConnection;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return getFirstRealPreparedStatement().executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement: realPrepareStatments){
            int result=preparedStatement.executeUpdate();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new BinMultiDataSourceException("  list of preparedStatement return result is inconsistent on call executeUpdate() ");
            }
        }
        return firstResult;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNull(parameterIndex,sqlType);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBoolean(parameterIndex,x);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setByte(parameterIndex,x);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setShort(parameterIndex,x);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setInt(parameterIndex,x);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setLong(parameterIndex,x);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setFloat(parameterIndex,x);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setDouble(parameterIndex,x);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBigDecimal(parameterIndex,x);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setString(parameterIndex,x);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBytes(parameterIndex,x);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setDate(parameterIndex,x);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setTime(parameterIndex,x);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setTimestamp(parameterIndex,x);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setAsciiStream(parameterIndex,x,length);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setUnicodeStream(parameterIndex,x,length);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBinaryStream(parameterIndex,x,length);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.clearParameters();
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.clearParameters();
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setObject(parameterIndex,x);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.execute();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute() ");
            }
        }
        return firstResult;
    }

    @Override
    public void addBatch() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.addBatch();
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setCharacterStream(parameterIndex,reader,length);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setRef(parameterIndex,x);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBlob(parameterIndex,x);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setClob(parameterIndex,x);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setArray(parameterIndex,x);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
       return getFirstRealPreparedStatement().getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setDate(parameterIndex,x);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setTime(parameterIndex,x,cal);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setTimestamp(parameterIndex,x,cal);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNull(parameterIndex,sqlType,typeName);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setURL(parameterIndex,x);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return getFirstRealPreparedStatement().getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setRowId(parameterIndex,x);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNString(parameterIndex,value);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNCharacterStream(parameterIndex,value,length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNClob(parameterIndex,value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setClob(parameterIndex,reader,length);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBlob(parameterIndex,inputStream,length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNClob(parameterIndex,reader,length);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setSQLXML(parameterIndex,xmlObject);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setObject(parameterIndex,x);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setAsciiStream(parameterIndex,x);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBinaryStream(parameterIndex,x);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setCharacterStream(parameterIndex,reader,length);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setAsciiStream(parameterIndex,x);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBinaryStream(parameterIndex,x);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setCharacterStream(parameterIndex,reader);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNCharacterStream(parameterIndex,value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setClob(parameterIndex,reader);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setBlob(parameterIndex,inputStream);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setNClob(parameterIndex,reader);
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return  getFirstRealPreparedStatement().executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.executeUpdate(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate() ");
            }
        }
        return firstResult;
    }

    @Override
    public void close() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.close();
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.getMaxFieldSize();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getMaxRows() ");
            }
        }
        return firstResult;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        getFirstRealPreparedStatement().setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return getFirstRealPreparedStatement().getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        getFirstRealPreparedStatement().setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.getQueryTimeout();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getQueryTimeout() ");
            }
        }
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return getFirstRealPreparedStatement().getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        getFirstRealPreparedStatement().setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.cancel();
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        //因为默认认为MulitPreparedStatement的realPrepareStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接去queryPreparedStatement的SQLWarning对象
        return getFirstRealPreparedStatement().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.clearWarnings();
        }
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setCursorName(name);
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.execute(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isCloseOnCompletion() ");
            }
        }
        return firstResult;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return getFirstRealPreparedStatement().getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.getUpdateCount();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getUpdateCount() ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return getFirstRealPreparedStatement().getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        getFirstRealPreparedStatement().setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return getFirstRealPreparedStatement().getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        getFirstRealPreparedStatement().setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return getFirstRealPreparedStatement().getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return getFirstRealPreparedStatement().getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return getFirstRealPreparedStatement().getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.addBatch(sql);
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.clearBatch();
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        int[] firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int[] result=preparedStatement.executeBatch();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(Arrays.equals(firstResult,result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeBatch() ");
            }
        }
        return firstResult;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return multiConnection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
       return getFirstRealPreparedStatement().getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        //因为默认认为MulitPreparedStatement的realPrepareStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以自动生成出来的主键也应该是一致才行。
        ResultSet firstResult=null;
        Object firstObj=null;
        Object defaultObj=new Object();
        for(PreparedStatement preparedStatement:realPrepareStatments){
            ResultSet result=preparedStatement.getGeneratedKeys();
            if(result.next()){
                Object object = result.getObject(1);
                if(firstObj==null) {
                    firstObj=object;
                    firstResult=result;
                    continue;
                }
                if( !firstObj.equals(object)){
                    throw new SQLException(" list of preparedStatement return result is inconsistent on call getGeneratedKeys() ");
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
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.executeUpdate(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, String[] autoGeneratedKeys) ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.executeUpdate(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, String[] columnIndexes) ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        Integer firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            int result=preparedStatement.executeUpdate(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, String[] columnNames) ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.execute(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute(String sql, int autoGeneratedKeys) ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.execute(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute(String sql, int[] columnIndexes) ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.execute(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute(String sql, String[] columnNames) ");
            }
        }
        return firstResult;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return getFirstRealPreparedStatement().getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.isClosed();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=result){
                throw new SQLException(" this list of preparedStatement return result is inconsistent on call isClosed() ");
            }
        }
        return firstResult;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.setPoolable(poolable);
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.isPoolable();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isPoolable() ");
            }
        }
        return firstResult;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        for(PreparedStatement preparedStatement:realPrepareStatments){
            preparedStatement.closeOnCompletion();
        }
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.isCloseOnCompletion();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isCloseOnCompletion() ");
            }
        }
        return firstResult;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(" MulitPreparedStatment no support  unwrap(Class<T> iface) ");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        Boolean firstResult=null;
        for(PreparedStatement preparedStatement:realPrepareStatments){
            boolean result=preparedStatement.isWrapperFor(iface);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult!=null && firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isCloseOnCompletion() ");
            }
        }
        return firstResult;
    }

    public PreparedStatement getFirstRealPreparedStatement(){
        return realPrepareStatments.get(0);
    }
}
