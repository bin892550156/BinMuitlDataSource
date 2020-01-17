package bin.framework;

import bin.framework.exception.BinMultiDataSourceException;
import bin.framework.multi.MultiConnection;
import bin.framework.strategy.LoadStrategy;
import bin.framework.strategy.RoundLoadStrategy;
import bin.framework.timer.RestoreConnectionPoolTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接查找器
 */
public class ConnectionFinder implements BinMetaConnection.BinMetaConnectionStateListener {

    private Logger logger= LoggerFactory.getLogger(ConnectionFinder.class);

    /**
     * gruopId - 对应的 BinMetaConnection集合
     */
    private final static Map<String,List<BinMetaConnection>> connectionPoolMap=new ConcurrentHashMap<>();

    /**
     * [groupId]tableName - tableName
     */
    private final static Map<String,String> tableNameGroupIdMap =new ConcurrentHashMap<>();

    /**
     * [groupId]procedureName - procedureName
     */
    private final static Map<String,String> procedureGroupIdMap=new ConcurrentHashMap<>();

    /**
     * sql - 对应的真实连接  缓存
     */
    private final static Map<String,FindResult> sqlConnectionCache=new ConcurrentHashMap<>();

    /**
     * 同一个groupId的数据库连接的负载策略
     */
    private LoadStrategy loadStrategy;

    /**
     * 表名查找器
     */
    private TableNameFinder tableNameFinder;

    /**
     * 恢复连接定时器
     */
    private RestoreConnectionPoolTimer restoreConnectionPoolTimer;

    /**
     * 数据源配置
     */
    private DataSourceProp dataSourceProp;

    /**
     * 实例化出一个ConnectionFinder对象
     * @param dataSourceCilentNodeList 数据源客户端节点集合
     * @param dataSourceProp 数据源配置
     */
    public ConnectionFinder(List<DataSourceCilentNode> dataSourceCilentNodeList, DataSourceProp dataSourceProp) {
        this.tableNameFinder=new TableNameFinder();
        this.dataSourceProp=dataSourceProp;
        loadMapData(dataSourceCilentNodeList);
    }

    /**
     * 将给定的数据源客户端集合的每个数据源客户端节点封装成BinMetaConnection对象，并添加到connectionPoolMap中。
     * 将每个BinMetaConnection对象的groupId,表名和存储过程名都分别整合到tableNameGroupIdMap和procedureGroupIdMap中
     *
     * @param dataSourceCilentNodeList 数据源客户端节点集合
     */
    private void loadMapData(List<DataSourceCilentNode> dataSourceCilentNodeList) {
        List<String> haveDealGroupIdFromTableNameList=new LinkedList<>();
        List<String> haveDealGroupIdFromProcedureList=new LinkedList<>();
        for(DataSourceCilentNode dataSourceCilentNode : dataSourceCilentNodeList){
            String groupId = dataSourceCilentNode.getGroupId();
            BinMetaConnection binMetaConnection =new BinMetaConnection(dataSourceCilentNode,this);
            List<BinMetaConnection> binMetaConnectionList = connectionPoolMap.get(groupId);
            if(binMetaConnectionList ==null){
                binMetaConnectionList =new ArrayList<>();
                connectionPoolMap.put(groupId, binMetaConnectionList);
            }
            binMetaConnectionList.add(binMetaConnection);

            if(!haveDealGroupIdFromTableNameList.contains(groupId)){
                List<String> tableNameList= binMetaConnection.getTableNameList();
                for (String tableName:tableNameList){
                    tableNameGroupIdMap.put(String.format("[%s]%s",groupId,tableName),groupId);
                }
                haveDealGroupIdFromTableNameList.add(groupId);
            }

            if(!haveDealGroupIdFromProcedureList.contains(groupId)){
                List<String> procedureList= binMetaConnection.getProcedureList();
                for (String procedure:procedureList){
                    procedureGroupIdMap.put(String.format("[%s]%s",groupId,procedure),groupId);
                }
                haveDealGroupIdFromProcedureList.add(groupId);
            }
        }
    }

    /**
     * 根据给定的sql找到对应的Connection
     * @param sql sql脚本
     */
    public Connection findConnection(String sql) throws SQLException {
        return toFindConnection(sql);
    }

    /**
     * 根据给定的sql找到对应的Connection
     * @param sql sql脚本
     * @return 数据库连接
     */
    protected Connection toFindConnection(String sql) throws SQLException {
        FindResult findResult = toFindBinMetaConnections(sql);
        if(findResult.getGiEn().getSqlCommand().equals(SqlCommand.SELECT)){
            //根据策略选择从Connection元对象列表中选择合适的Connection元对象
            BinMetaConnection binMetaConnection = getLoadStrategy().choose(findResult.getGroupId(),findResult.getBinMetaConnections());
            if(logger.isDebugEnabled()){
                logger.debug("use connection is {} : {} ", binMetaConnection.getDataSourceCilentNode().getUrl().toString(),sql);
            }
            return binMetaConnection.getConnection();
        }else{
            List<BinMetaConnection> binMetaConnections =findResult.getBinMetaConnections();
            List<Connection> connections=new LinkedList<>();
            for (BinMetaConnection binMetaConnection :
                    binMetaConnections) {
                connections.add(binMetaConnection.getConnection());
            }
            return new MultiConnection(connections);
        }
    }

    /**
     * 根据给定的sql找到对应的BinMetaConnectio
     * @param sql sql脚本
     * @return 数据库连接
     */
    protected FindResult toFindBinMetaConnections(String sql){
        FindResult findResult= sqlConnectionCache.get(sql);
        if(findResult!=null){
            return findResult;
        }

        //查找对应的GroupId
        GiEn giEn =tableNameFinder.findGiTn(sql);
        String groupId= giEn.getGroupId();
        String executeName= giEn.getExecuteName();

        if(isEmpty(groupId)){
            if(giEn.isProcedure()){
                for(Map.Entry<String,String> entry: procedureGroupIdMap.entrySet()){
                    String entryTableName = entry.getKey();
                    String entryGroupId=entry.getValue();
                    if(entryTableName.endsWith(executeName)){
                        groupId=entryGroupId;
                        break;
                    }
                }
            }else{
                for(Map.Entry<String,String> entry: tableNameGroupIdMap.entrySet()){
                    String entryTableName = entry.getKey();
                    String entryGroupId=entry.getValue();
                    if(entryTableName.endsWith(executeName)){
                        groupId=entryGroupId;
                        break;
                    }
                }
            }
        }
        if(groupId==null) {
            String msg=String.format(" no found groupId from the configuration , the execute sql is '%s' ",sql);
            throw new BinMultiDataSourceException(msg);
        }
        //找到groupId对应的Connection元对象列表
        List<BinMetaConnection> binMetaConnections = connectionPoolMap.get(groupId);
        //找到可用的MetaConnectionInfo
        List<BinMetaConnection> activeBinMetaConnections =new ArrayList<>();
        for (BinMetaConnection binMetaConnection :
                binMetaConnections) {
            if(!binMetaConnection.isConnectionDeprecated())
                activeBinMetaConnections.add(binMetaConnection);
        }
        if(binMetaConnections ==null|| binMetaConnections.isEmpty()){
            throw new BinMultiDataSourceException(String.format("no found the correspond connection of sql ,plase check your dataSource configuration : %s",
                    sql));
        }
        //整合数据
        findResult=new FindResult();
        findResult.setGiEn(giEn);
        findResult.setBinMetaConnections(binMetaConnections);
        findResult.setGroupId(groupId);
        return findResult;
    }

    /**
     * 检查字符串是否空
     * @param str 字符串
     */
    protected boolean isEmpty(String str){
        return str==null || str.trim().isEmpty();
    }

    /**
     * 获取负载策略
     */
    public LoadStrategy getLoadStrategy() {
        if(loadStrategy ==null){
            loadStrategy =new RoundLoadStrategy();
        }
        return loadStrategy;
    }

    @Override
    public void onConnectionDeprecated(BinMetaConnection binMetaConnection) {
        //交给恢复连接池定时监听器恢复连接
        if(restoreConnectionPoolTimer==null) {
            restoreConnectionPoolTimer=new RestoreConnectionPoolTimer(dataSourceProp.getIntervalPeriodToRestoreConnectionPoolTimer());
            restoreConnectionPoolTimer.start();
        }
        restoreConnectionPoolTimer.addMetaConnectionInfo(binMetaConnection);
    }


    public void setLoadStrategy(LoadStrategy loadStrategy) {
        this.loadStrategy = loadStrategy;
    }

    /**
     * 查询结果封装
     */
    private static class FindResult{
        private String groupId;
        private List<BinMetaConnection> binMetaConnections;
        private GiEn giEn;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<BinMetaConnection> getBinMetaConnections() {
            return binMetaConnections;
        }

        public void setBinMetaConnections(List<BinMetaConnection> binMetaConnections) {
            this.binMetaConnections = binMetaConnections;
        }

        public GiEn getGiEn() {
            return giEn;
        }

        public void setGiEn(GiEn giEn) {
            this.giEn = giEn;
        }
    }
}
