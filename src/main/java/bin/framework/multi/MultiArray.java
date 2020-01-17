package bin.framework.multi;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MultiArray implements Array {

    private List<Array> realArrays;

    public MultiArray( List<Array> realArrays) {
        this.realArrays=realArrays;
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        String firstResult=null;
        for(Array array: realArrays){
            String result=array.getBaseTypeName();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call  getBaseTypeName()  ");
            }
        }
        return firstResult;
    }

    @Override
    public int getBaseType() throws SQLException {
        Integer firstResult=null;
        for(Array array: realArrays){
            int result=array.getBaseType();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call  getBaseType()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getArray() throws SQLException {
        Integer firstResult=null;
        for(Array array: realArrays){
            int result=array.getBaseType();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call  getArray()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        Object firstResult=null;
        for(Array array: realArrays){
            Object result=array.getArray(map);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call  getArray(Map<String, Class<?>> map) ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getArray(long index, int count) throws SQLException {
        Object firstResult=null;
        for(Array array: realArrays){
            Object result=array.getArray(index,count);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call getArray(long index, int count) ");
            }
        }
        return firstResult;
    }

    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
        Object firstResult=null;
        for(Array array: realArrays){
            Object result=array.getArray(index,count,map);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realArrays return result is inconsistent on call getArray(long index, int count, Map<String, Class<?>> map) ");
            }
        }
        return firstResult;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        //这个没办法做出多个。。就暂时直接去第一个
        return realArrays.get(0).getResultSet();
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
        //这个没办法做出多个。。就暂时直接去第一个
        return realArrays.get(0).getResultSet();
    }

    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
        //这个没办法做出多个。。就暂时直接去第一个
        return realArrays.get(0).getResultSet(index, count);
    }

    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
        //这个没办法做出多个。。就暂时直接去第一个
        return realArrays.get(0).getResultSet(index, count, map);
    }

    @Override
    public void free() throws SQLException {
        for(Array array: realArrays){
            array.free();
        }
    }
}
