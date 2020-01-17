package bin.framework.multi;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MultiCallableStatment implements CallableStatement {

    private MultiConnection multiConnection;

    private List<CallableStatement> realCallStatmentStatments;

    public MultiCallableStatment(List<CallableStatement> realCallStatmentStatments,
                                 MultiConnection multiConnection ){
        this.realCallStatmentStatments = realCallStatmentStatments;
        this.multiConnection=multiConnection;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        for (CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterIndex,sqlType);
        }
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        for (CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterIndex,sqlType,scale);
        }
    }

    @Override
    public boolean wasNull() throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.wasNull();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call wasNull() ");
            }
        }
        return firstResult;
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        String firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            String result=callableStatement.getString(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getString(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.getBoolean(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBoolean(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        Byte firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            byte result=callableStatement.getByte(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getByte(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        Short firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            short result=callableStatement.getShort(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getShort(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getInt(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getInt(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        Long firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            long result=callableStatement.getLong(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getLong(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        Float firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            float result=callableStatement.getFloat(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getFloat(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        Double firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            double result=callableStatement.getDouble(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult!=result){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDouble(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        BigDecimal firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            BigDecimal result=callableStatement.getBigDecimal(parameterIndex,scale);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBigDecimal(int parameterIndex, int scale)  ");
            }
        }
        return firstResult;
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        byte[] firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            byte[] result=callableStatement.getBytes(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if( Arrays.equals(firstResult,result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBytes(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        Date firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Date result=callableStatement.getDate(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDate(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        Time firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Time result=callableStatement.getTime(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTime(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        Timestamp firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Timestamp result=callableStatement.getTimestamp(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTimestamp(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        Object firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Object result=callableStatement.getObject(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getObject(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        BigDecimal firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            BigDecimal result=callableStatement.getBigDecimal(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBigDecimal(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        Object firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Object result=callableStatement.getObject(parameterIndex,map);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getObject(int parameterIndex,Map<String, Class<?>> map)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        Ref firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Ref result=callableStatement.getRef(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getRef(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        Blob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Blob result=callableStatement.getBlob(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBlob(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        Clob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Clob result=callableStatement.getClob(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getClob(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        Array firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Array result=callableStatement.getArray(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getArray(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        Date firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Date result=callableStatement.getDate(parameterIndex,cal);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDate(int parameterIndex, Calendar cal)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        Time firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Time result=callableStatement.getTime(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTime(int parameterIndex, Calendar cal)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        Timestamp firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Timestamp result=callableStatement.getTimestamp(parameterIndex,cal);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTimestamp(int parameterIndex, Calendar cal)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterIndex,sqlType,typeName);
        }
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterName,sqlType);
        }
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterName,sqlType,scale);
        }
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.registerOutParameter(parameterName,sqlType,typeName);
        }
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        URL firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            URL result=callableStatement.getURL(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getURL(int parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setURL(parameterName,val);
        }
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNull(parameterName,sqlType);
        }
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBoolean(parameterName,x);
        }
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setByte(parameterName,x);
        }
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setShort(parameterName,x);
        }
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setInt(parameterName,x);
        }
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setLong(parameterName,x);
        }
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setFloat(parameterName,x);
        }
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDouble(parameterName,x);
        }
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBigDecimal(parameterName,x);
        }
    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setString(parameterName,x);
        }
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBytes(parameterName,x);
        }
    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDate(parameterName,x);
        }
    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTime(parameterName,x);
        }
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTimestamp(parameterName,x);
        }
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterName,x,length);
        }
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterName,x,length);
        }
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterName,x,targetSqlType,scale);
        }
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterName,x,targetSqlType);
        }
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterName,x);
        }
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterName,reader,length);
        }
    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDate(parameterName,x,cal);
        }
    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTime(parameterName,x,cal);
        }
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTimestamp(parameterName,x,cal);
        }
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNull(parameterName,sqlType,typeName);
        }
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        String firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            String result=callableStatement.getString(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getString(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.getBoolean(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBoolean(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        Byte firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            byte result=callableStatement.getByte(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getByte(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        Short firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            short result=callableStatement.getShort(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getShort(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getInt(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getInt(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        Long firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            long result=callableStatement.getLong(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getLong(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        Float firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            float result=callableStatement.getFloat(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getFloat(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        Double firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Double result=callableStatement.getDouble(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDouble(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        byte[] firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            byte[] result=callableStatement.getBytes(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(Arrays.equals(firstResult,result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBytes(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        Date firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Date result=callableStatement.getDate(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDate(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        Time firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Time result=callableStatement.getTime(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTime(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        Timestamp firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Timestamp result=callableStatement.getTimestamp(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTimestamp(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        Object firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Object result=callableStatement.getObject(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getObject(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        BigDecimal firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            BigDecimal result=callableStatement.getBigDecimal(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBigDecimal(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        Object firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Object result=callableStatement.getObject(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getObject(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        Ref firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Ref result=callableStatement.getRef(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getRef(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        Blob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Blob result=callableStatement.getBlob(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getBlob(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        Clob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Clob result=callableStatement.getClob(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getClob(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        Array firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Array result=callableStatement.getArray(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getArray(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        Date firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Date result=callableStatement.getDate(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getDate(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        Time firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Time result=callableStatement.getTime(parameterName,cal);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTime(String parameterName, Calendar cal)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        Timestamp firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Timestamp result=callableStatement.getTimestamp(parameterName,cal);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getTimestamp(String parameterName, Calendar cal)  ");
            }
        }
        return firstResult;
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        URL firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            URL result=callableStatement.getURL(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getURL(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        RowId firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            RowId result=callableStatement.getRowId(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getRowId(String parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        RowId firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            RowId result=callableStatement.getRowId(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getRowId(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setRowId(parameterName,x);
        }
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNString(parameterName,value);
        }
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNCharacterStream(parameterName,value,length);
        }
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterName,value);
        }
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setClob(parameterName,reader,length);
        }
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterName,inputStream,length);
        }
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterName,reader,length);
        }
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        NClob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            NClob result=callableStatement.getNClob(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNClob(String parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        NClob firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            NClob result=callableStatement.getNClob(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNClob(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setSQLXML(parameterName,xmlObject);
        }
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        SQLXML firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            SQLXML result=callableStatement.getSQLXML(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getSQLXML(String parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        SQLXML firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            SQLXML result=callableStatement.getSQLXML(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getSQLXML(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        String firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            String result=callableStatement.getNString(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNString(String parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        String firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            String result=callableStatement.getNString(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNString(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        Reader firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Reader result=callableStatement.getNCharacterStream(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNCharacterStream(String parameterIndex)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        Reader firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Reader result=callableStatement.getNCharacterStream(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getNCharacterStream(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        Reader firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Reader result=callableStatement.getCharacterStream(parameterIndex);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getCharacterStream(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        Reader firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            Reader result=callableStatement.getCharacterStream(parameterName);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getCharacterStream(String parameterName)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterName,x);
        }
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setClob(parameterName,x);
        }
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterName,x,length);
        }
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterName,x,length);
        }
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterName,reader,length);
        }
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterName,x);
        }
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterName,x);
        }
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterName,reader);
        }
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNCharacterStream(parameterName,value);
        }
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setClob(parameterName,reader);
        }
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterName,inputStream);
        }
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterName,reader);
        }
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        T firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            T result=callableStatement.getObject(parameterIndex,type);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call  <T> T getObject(int parameterIndex, Class<T> type)   ");
            }
        }
        return firstResult;
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        T firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            T result=callableStatement.getObject(parameterName,type);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call  <T> T getObject(String parameterName, Class<T> type)   ");
            }
        }
        return firstResult;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
       return getFirstRealCallableStatement().executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.executeUpdate();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate()   ");
            }
        }
        return firstResult;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNull(parameterIndex,sqlType);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBoolean(parameterIndex,x);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setByte(parameterIndex,x);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setShort(parameterIndex,x);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setInt(parameterIndex,x);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setLong(parameterIndex,x);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setFloat(parameterIndex,x);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDouble(parameterIndex,x);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBigDecimal(parameterIndex,x);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setString(parameterIndex,x);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBytes(parameterIndex,x);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDate(parameterIndex,x);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTime(parameterIndex,x);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTimestamp(parameterIndex,x);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterIndex,x);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setUnicodeStream(parameterIndex,x,length);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterIndex,x,length);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.clearParameters();
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterIndex,x,targetSqlType);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterIndex,x);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.execute();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute()   ");
            }
        }
        return firstResult;
    }

    @Override
    public void addBatch() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.addBatch();
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterIndex,reader,length);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setRef(parameterIndex,x);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterIndex,x);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setClob(parameterIndex,x);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setArray(parameterIndex,x);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        //因为默认认为MultiCallableStatment的realCallableStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接使用getFirstRealCallableStatement()的MetaData对象
        return getFirstRealCallableStatement().getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setDate(parameterIndex,x,cal);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTime(parameterIndex,x,cal);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setTimestamp(parameterIndex,x,cal);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNull(parameterIndex,sqlType,typeName);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setURL(parameterIndex,x);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        //因为默认认为MultiCallableStatment的realCallableStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接去第一个CallableStatment的ParameterMetaData对象
        return realCallStatmentStatments.get(0).getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setRowId(parameterIndex,x);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNString(parameterIndex,value);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNCharacterStream(parameterIndex,value,length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterIndex,value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setClob(parameterIndex,reader,length);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterIndex,inputStream,length);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterIndex,reader,length);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setSQLXML(parameterIndex,xmlObject);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setObject(parameterIndex,x,targetSqlType,scaleOrLength);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterIndex,x,length);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterIndex,x,length);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterIndex,reader,length);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setAsciiStream(parameterIndex,x);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBinaryStream(parameterIndex,x);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCharacterStream(parameterIndex,reader);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNCharacterStream(parameterIndex,value);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNCharacterStream(parameterIndex,reader);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setBlob(parameterIndex,inputStream);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setNClob(parameterIndex,reader);
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return getFirstRealCallableStatement().executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.executeUpdate(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void close() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.close();
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getMaxFieldSize();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getMaxFieldSize()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setMaxFieldSize(max);
        }
    }

    @Override
    public int getMaxRows() throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getMaxRows();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getMaxRows()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setMaxRows(max);
        }
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setEscapeProcessing(enable);
        }
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getQueryTimeout();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getQueryTimeout()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setQueryTimeout(seconds);
        }
    }

    @Override
    public void cancel() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.cancel();
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        //因为默认认为MultiCallableStatment的realCallableStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以这里直接去第一个CallableStatment的SQLWarning对象
        return realCallStatmentStatments.get(0).getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.clearWarnings();
        }
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setCursorName(name);
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.execute(sql);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call execute(String sql)  ");
            }
        }
        return firstResult;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return getFirstRealCallableStatement().getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.getUpdateCount();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call getUpdateCount()  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return getFirstRealCallableStatement().getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        getFirstRealCallableStatement().setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return getFirstRealCallableStatement().getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        getFirstRealCallableStatement().setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
       return getFirstRealCallableStatement().getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return getFirstRealCallableStatement().getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return getFirstRealCallableStatement().getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.addBatch(sql);
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.clearBatch();
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        int[] firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int[] result=callableStatement.executeBatch();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!Arrays.equals(firstResult,result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeBatch()  ");
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
        return getFirstRealCallableStatement().getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        //因为默认认为MulitPreparedStatement的realPrepareStatment对应的数据库的驱动，数据库表结构，表数据，配置都是一致的，
        // 所以自动生成出来的主键也应该是一致才行。
        ResultSet firstResult=null;
        Object firstObj=null;
        Object defaultObj=new Object();
        for(PreparedStatement preparedStatement:realCallStatmentStatments){
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
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.executeUpdate(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, int autoGeneratedKeys)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.executeUpdate(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, int[] columnIndexes)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        Integer firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            int result=callableStatement.executeUpdate(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, String[] columnNames)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.execute(sql,autoGeneratedKeys);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, int autoGeneratedKey)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.execute(sql,columnIndexes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, int[] columnIndexes)  ");
            }
        }
        return firstResult;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.execute(sql,columnNames);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call executeUpdate(String sql, String[] columnNames)  ");
            }
        }
        return firstResult;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return getFirstRealCallableStatement().getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.isClosed();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isClosed()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.setPoolable(poolable);
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.isPoolable();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isPoolable()  ");
            }
        }
        return firstResult;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        for(CallableStatement callableStatement: realCallStatmentStatments){
            callableStatement.closeOnCompletion();
        }
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        Boolean firstResult=null;
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.isPoolable();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isCloseOnCompletion()  ");
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
        for(CallableStatement callableStatement: realCallStatmentStatments){
            boolean result=callableStatement.isPoolable();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of preparedStatement return result is inconsistent on call isWrapperFor()  ");
            }
        }
        return firstResult;
    }

    public CallableStatement getFirstRealCallableStatement(){
        return realCallStatmentStatments.get(0);
    }
}
