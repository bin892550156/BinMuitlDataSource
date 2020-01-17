package bin.framework;

import bin.framework.exception.BinMultiDataSourceException;
import bin.framework.pool.PooledConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 元连接封装类
 * <p>存储着连接器和数据源客户端节点的封装类</p>
 */
public class BinMetaConnection implements PooledConnectionManager.PooledConnectionManagerListener{

    /**
     * 对应数据库的所有表名
     */
    private List<String> tableNameList;
    /**
     * 对应数据库的所有存储过程名
     */
    private List<String> procedureList;

    /**
     * 池连接管理器
      */
    private PooledConnectionManager pooledConnectionManager;

    /**
     * 数据源客户端节点
     */
    private DataSourceCilentNode dataSourceCilentNode;

    /**
     * 连接池是否已经无效
     */
    private boolean isConnectionDeprecated;

    /**
     * 元连接信息状态监听器
     */
    public interface BinMetaConnectionStateListener {
        /**
         * 当前连接池无效时回调
         * @param binMetaConnection
         */
        void onConnectionDeprecated(BinMetaConnection binMetaConnection);
    }

    private BinMetaConnectionStateListener binMetaConnectionStateListener;

    /**
     * 实例化一个BinMetaConnection，并根据dataSourceCilentNode所提供的配置信息，加载对应库的所有
     * 表和存储过程
     * @param dataSourceCilentNode 数据源客户端节点
     * @param binMetaConnectionStateListener 元连接监听器
     */
    BinMetaConnection(DataSourceCilentNode dataSourceCilentNode, BinMetaConnectionStateListener binMetaConnectionStateListener){
        this.dataSourceCilentNode = dataSourceCilentNode;
        this.binMetaConnectionStateListener = binMetaConnectionStateListener;
        pooledConnectionManager=new PooledConnectionManager(dataSourceCilentNode,this);
        loadTableName();
        loadProcedure();
    }

    /**
     * 加载对应库的所有存储过程
     */
    private void loadProcedure() {
        String sql= dataSourceCilentNode.getSelectProcedureSql();
        procedureList=new LinkedList<>();
        try{
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String procedureName = resultSet.getString("PROCEDURE_NAME").toUpperCase();
                procedureList.add(procedureName);
            }
        }catch (SQLException e){
            String exceptionMsg=String.format(" get all procedure name throw SQLException, connectionPooledId ='%s' ", dataSourceCilentNode.getConnectionPooledId());
            throw new BinMultiDataSourceException(exceptionMsg,e);
        }
    }

    /**
     * 加载对应库的所有表名
     */
    private void loadTableName()  {
        String sql= dataSourceCilentNode.getSelectTableSql();
        tableNameList=new LinkedList<>();
        try{
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String tableName = resultSet.getString("TABLE_NAME").toUpperCase();
                tableNameList.add(tableName);
            }
        }catch (SQLException e){
            String exceptionMsg=String.format(" get all table name throw SQLException, connectionPooledId ='%s' ", dataSourceCilentNode.getConnectionPooledId());
            throw new BinMultiDataSourceException(exceptionMsg,e);
        }

    }

    public Connection getConnection() throws SQLException {
        return pooledConnectionManager.popConnection();
    }

    @Override
    public void onPooledDeprecated(PooledConnectionManager pooledConnectionManager) {
        isConnectionDeprecated=true;
        binMetaConnectionStateListener.onConnectionDeprecated(this);
    }

    public boolean isConnectionDeprecated() {
        return isConnectionDeprecated;
    }

    public void setConnectionDeprecated(boolean connectionDeprecated) {
        pooledConnectionManager.setDeprecated(connectionDeprecated);
        isConnectionDeprecated = connectionDeprecated;
    }

    public DataSourceCilentNode getDataSourceCilentNode() {
        return dataSourceCilentNode;
    }

    public void setDataSourceCilentNode(DataSourceCilentNode dataSourceCilentNode) {
        this.dataSourceCilentNode = dataSourceCilentNode;
    }

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public List<String> getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(List<String> procedureList) {
        this.procedureList = procedureList;
    }


}
