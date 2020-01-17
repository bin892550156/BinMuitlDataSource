package bin.framework;

import bin.framework.exception.BinMultiDataSourceException;
import bin.framework.util.BinAssertUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * 连接代理
 */
public class BinConnection implements Connection {

    /**
     * 连接查找器
     */
    private ConnectionFinder connectionFinder;
    /**
     * 真正执行的数据库连接，该对象只会在带有SQL参数的方法调用时创建。
     */
    private Connection realConnection;

    /**
     * 延迟执行方法的列表，用于realConnection还未创建的时候，对一些方法（如{@link #setAutoCommit(boolean)}）
     * 调用整合成Task对象，待realConnection成功实例化后，重新调用。
     */
    private List<Task> tasks=new ArrayList<>();

    /**
     * 实例化一个新的 BinConnection对象
     * @param connectionFinder 数据连接查询
     */
    public BinConnection(ConnectionFinder connectionFinder) {
        this.connectionFinder = connectionFinder;
    }

    @Override
    public Statement createStatement() throws SQLException {
        BinStatement binStatement=new BinStatement(connectionFinder);
        realConnection= binStatement.getConnection();
        return binStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql));
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareCall(adjustSql(sql));
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        getRealConnection(sql);
        return realConnection.nativeSQL(adjustSql(sql));
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if(realConnection!=null){
            realConnection.setAutoCommit(autoCommit);
            return;
        }
        tasks.add(new Task("setAutoCommit",autoCommit));
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getAutoCommit();
        }
        Object paramObj = getTaskParamObjFromTaskList("setAutoCommit",new Object[]{Boolean.class}, 0);
        return paramObj==null?false: (boolean) paramObj;//一般都是false
    }

    @Override
    public void commit() throws SQLException {
        if(realConnection!=null){
            realConnection.commit();
            return;
        }
        tasks.add(new Task("commit"));
    }

    @Override
    public void rollback() throws SQLException {
        if(realConnection!=null){
            realConnection.rollback();
            return;
        }
        tasks.add(new Task("rollback"));
    }

    @Override
    public void close() throws SQLException {
        if(realConnection!=null){
            realConnection.close();
            return;
        }
        tasks.add(new Task("close"));
    }

    @Override
    public boolean isClosed() throws SQLException {
        if(realConnection!=null) {
            return realConnection.isClosed();
        }
        Object paramObj = getTaskParamObjFromTaskList("close",new Object[]{}, -1);
        return paramObj!=null&&((Integer)paramObj)==-1?true:false;//一般都是false
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        assertNullConnection();
        return realConnection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        if(realConnection!=null){
            realConnection.setReadOnly(readOnly);
            return;
        }
        tasks.add(new Task("setReadOnly",readOnly));
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        if(realConnection!=null) {
            return realConnection.isReadOnly();
        }
        Object paramObj = getTaskParamObjFromTaskList("setReadOnly", new Object[]{Boolean.class},0);
        return paramObj==null?false: (boolean) paramObj;//一般都是false
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        if(realConnection!=null){
            realConnection.setCatalog(catalog);
            return;
        }
        tasks.add(new Task("setCatalog",catalog));
    }

    @Override
    public String getCatalog() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getCatalog();
        }
        Object paramObj = getTaskParamObjFromTaskList("setCatalog", new Object[]{String.class},0);
        return paramObj==null?null: (String) paramObj;//根据API规范
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        if(realConnection!=null){
            realConnection.setTransactionIsolation(level);
            return;
        }
        tasks.add(new Task("setTransactionIsolation",level));
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        assertNullConnection();
        return realConnection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        assertNullConnection();
        return realConnection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        if(realConnection!=null){
            realConnection.clearWarnings();
            return;
        }
        tasks.add(new Task("clearWarnings"));
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        assertNullConnection();
        return realConnection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql),resultSetType,resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareCall(adjustSql(sql),resultSetType,resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getTypeMap();
        }
        Object paramObj = getTaskParamObjFromTaskList("close", new Object[]{Map.class},0);
        return paramObj!=null?(Map<String, Class<?>>)paramObj:new HashMap<>();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        if(realConnection!=null){
            realConnection.setTypeMap(map);
            return;
        }
        tasks.add(new Task("setTypeMap",map));
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        if(realConnection!=null){
            realConnection.setHoldability(holdability);
            return;
        }
        tasks.add(new Task("setHoldability",holdability));
    }

    @Override
    public int getHoldability() throws SQLException {
        assertNullConnection();
        return realConnection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        assertNullConnection();
        return realConnection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        assertNullConnection();
        return realConnection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        assertNullConnection();
        realConnection.rollback();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        assertNullConnection();
        realConnection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        assertNullConnection();
        return realConnection.createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql),resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareCall(adjustSql(sql),resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql),autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql),columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        getRealConnection(sql);
        return realConnection.prepareStatement(adjustSql(sql),columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        assertNullConnection();
        return realConnection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        assertNullConnection();
        return realConnection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        assertNullConnection();
         return realConnection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        assertNullConnection();
        return realConnection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        assertNullConnection();
        return realConnection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        if(realConnection!=null){
            realConnection.setClientInfo(name,value);
            return;
        }
        tasks.add(new Task("setClientInfo",name,value));
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        if(realConnection!=null){
            realConnection.setClientInfo(properties);
            return;
        }
        tasks.add(new Task("setClientInfo",properties));
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        if(realConnection!=null) {
            return realConnection.getClientInfo(name);
        }
        Object paramObj = getTaskParamObjFromTaskList("setClientInfo", new Object[]{String.class,String.class},1);
        return paramObj==null?null: (String) paramObj;//一般都是false
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getClientInfo();
        }
        Object paramObj = getTaskParamObjFromTaskList("setClientInfo", new Object[]{Properties.class},0);
        return paramObj==null?null: (Properties) paramObj;//一般都是false
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        assertNullConnection();
        return realConnection.createArrayOf(typeName,elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        assertNullConnection();
        return realConnection.createStruct(typeName,attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        if(realConnection!=null){
            realConnection.setSchema(schema);
            return;
        }
        tasks.add(new Task("setSchema",schema));
    }

    @Override
    public String getSchema() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getSchema();
        }
        Object paramObj = getTaskParamObjFromTaskList("setSchema", new Object[]{String.class},0);
        return paramObj==null?null: (String) paramObj;//一般都是false
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        if(realConnection!=null){
            realConnection.abort(executor);
            return;
        }
        tasks.add(new Task("setSchema",executor));
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        if(realConnection!=null){
            realConnection.setNetworkTimeout(executor,milliseconds);
            return;
        }
        tasks.add(new Task("setNetworkTimeout",executor,milliseconds));
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        if(realConnection!=null) {
            return realConnection.getNetworkTimeout();
        }
        Object paramObj = getTaskParamObjFromTaskList("setNetworkTimeout", new Object[]{Executor.class,Integer.class},1);
        return paramObj!=null? (int) paramObj :0;//根据API设置的默认值
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        assertNullConnection();
        return realConnection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        assertNullConnection();
        return realConnection.isWrapperFor(iface);
    }

    /**
     * 根据给定的 {@code sql} 获取真正处理的 {@link Connection}
     * @param sql sql脚本
     */
    private void getRealConnection(String sql){
        if(realConnection==null) {
            try {
                realConnection= connectionFinder.findConnection(sql);
            } catch (SQLException e) {
                throw new BinMultiDataSourceException(" Get real connection is Fail : ",e);
            }
            //执行任务
            for (Task task :
                    tasks) {
                task.doTask(this);
            }
        }
    }

    /**
     * 对 {@link #realConnection} 为 {@code null} 的情况进行断言，并加以描述
     */
    private void assertNullConnection(){
        BinAssertUtil.isNull(realConnection,"The java.sql.Connection object that is actually processed is null. This object is obtained by parsing sql." +
                "You can make the framework assign values to this object by calling a method with SQL parameters in java.sql.Connection");
    }

    /**
     * 调整sql,对带有 '[groupId]'的sql进行去除
     */
    private String adjustSql(String sql){
        int midBracketStartIndex=sql.indexOf("[");
        if(midBracketStartIndex!=-1){
            StringBuilder sb=new StringBuilder(sql);
            int midBracketEndIndex=sb.indexOf("]",midBracketStartIndex);
            sb.delete(midBracketStartIndex,midBracketEndIndex+1);
            return sb.toString();
        }
        return sql;
    }

    /**
     * 获取Task对象的参数对象从{@link #tasks}
     * @param methodName 方法名
     * @param paramType 参数类型
     * @param paramIndex 参数索引，当为-1时，表示返回对象
     * @return 对应paramIndex的参数对象
     */
    private Object getTaskParamObjFromTaskList(String methodName,Object[] paramType, int paramIndex){
        Task toFindTask=new Task(methodName,paramType);
        int i = tasks.indexOf(toFindTask);
        if(i!=-1){
            if(paramIndex==-1) return -1;
            Task task = tasks.get(i);
            return task.getParamObjs()[paramIndex];
        }
        return null;
    }

    /**
     * 调用方法时整合处理的任务
     */
    private static class Task{
        /**
         * 方法名
         */
        private String methodName;
        /**
         * 传入的参数对象
         */
        private Object[] paramObjs;

        /**
         * 创建一个新的Task对象
         * @param methodName 方法名
         * @param paramObjs 传入的参数对象
         */
        private Task(String methodName, Object... paramObjs) {
            this.methodName = methodName;
            this.paramObjs = paramObjs;
        }

        /**
         * 执行任务
         * @param obj BinConnection实例
         */
        private void doTask(Object obj){
            Class<?> aClass = obj.getClass();
            Class[] paramTypes=getParamTypeFromParamObj();
            try {
                Method method = aClass.getMethod(methodName, paramTypes);
                method.invoke(obj,paramObjs);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                //ignore
                e.printStackTrace();
            }
        }

        /**
         * 获取传入的参数对象的类型
         * @return 参数对象类型
         */
        private Class[] getParamTypeFromParamObj(){
            Class[] classes=new Class[paramObjs.length];
            for (int i = 0; i < paramObjs.length; i++) {
                Object obj = paramObjs[i];
                Class<?> aClass = obj.getClass();
                if(aClass==Boolean.class){
                    aClass=boolean.class;
                }else if(aClass==Integer.class){
                    aClass=int.class;
                }else if(aClass==Double.class){
                    aClass=double.class;
                }else if(aClass==Float.class){
                    aClass=float.class;
                }else if(aClass==Long.class){
                    aClass=long.class;
                }else if(aClass==Character.class){
                    aClass=char.class;
                }
                classes[i]=aClass;
            }
            return classes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Task task = (Task) o;
            return Objects.equals(methodName, task.methodName) &&
                    Arrays.equals(paramObjs, task.paramObjs);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(methodName);
            result = 31 * result + Arrays.hashCode(paramObjs);
            return result;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Object[] getParamObjs() {
            return paramObjs;
        }

        public void setParamObjs(Object[] paramObjs) {
            this.paramObjs = paramObjs;
        }
    }
}
