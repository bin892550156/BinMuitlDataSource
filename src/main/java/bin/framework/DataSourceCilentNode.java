package bin.framework;

import bin.framework.exception.BinMultiDataSourceException;

import java.util.Properties;

/**
 * 数据源客户端节点
 * <p>
 *     提供数据库连接的库信息
 * </p>
 */
public class DataSourceCilentNode {

    /**
     * MySQL驱动
     */
    public final static String MYSQL_DRIVER_CLASS="com.mysql.cj.jdbc.Driver";
    /**
     * Oracle驱动
     */
    public final static String ORACLE_DRIVER_CLASS="oracle.jdbc.driver.OracleDriver";

    /**
     * 连接池ID，默认是用户+url
     */
    private String connectionPooledId;

    /**
     * 数据库Url
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动
     */
    private String driver;

    /**
     * 查询数据库中的表SQL脚本，如果未设置，会根据驱动找到对应的SQL脚本，目前仅提供了MySQL和Oracle
     */
    private String selectTableSql;

    /**
     * 查询数据库中的存储过程SQL脚本，如果未设置，会根据驱动找到对应的SQL脚本，目前仅提供了MySQL和Oracle
     */
    private String selectProcedureSql;

    /**
     * 用于心跳检测连接是否可用的sql
     */
    private String pingSql;

    /**
     * 指负载均衡组ID,同一个groupId的DataSourceCilentNode应该其对应的数据库，表结构，表数据都相同，以防止负载均衡操作时导致
     * 的因不一致而抛出异常
     */
    private String groupId;

    /**
     * 数据库的自定义属性
     */
    public Properties driverProperties;

    public String getPingSql() {
        if(isEmpty(pingSql)){
            pingSql="SELECT 1 FROM DUAL";
        }
        return pingSql;
    }

    public String getSelectTableSql() {
        if(isEmpty(selectTableSql)){
            if(MYSQL_DRIVER_CLASS.equals(driver)){
                String dbName=getDbName();
                selectTableSql=String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='%s'", dbName);
            }else if(ORACLE_DRIVER_CLASS.equals(driver)){
                selectTableSql="SELECT TABLE_NAME FROM USER_TAB_COMMENTS";
            }else{
                throw new BinMultiDataSourceException(
                        String.format("no found select table sql from %s, plase invoke DataSourceConfig#setSelectTableSql to set.",toString()));
            }
        }
        return selectTableSql;
    }

    public String getSelectProcedureSql() {
        if(isEmpty(selectProcedureSql)){
            if(MYSQL_DRIVER_CLASS.equals(driver)){
                String dbName=getDbName();
                selectProcedureSql=String.format("SELECT NAME PROCEDURE_NAME FROM MYSQL.PROC WHERE DB = '%s' AND TYPE = 'PROCEDURE'", dbName);
            }else if(ORACLE_DRIVER_CLASS.equals(driver)){
                selectProcedureSql="SELECT DISTINCT NAME PROCEDURE_NAME FROM USER_SOURCE WHERE TYPE = 'PROCEDURE'";
            }else{
                throw new BinMultiDataSourceException(
                        String.format("no found select procedure sql from %s, plase invoke DataSourceConfig#setSelectProcedureSql to set.",toString()));
            }
        }
        return selectProcedureSql;
    }

    public String getConnectionPooledId() {
        if(connectionPooledId==null){
            connectionPooledId=String.format("[%s@%s]",username,url);
        }
        return connectionPooledId;
    }

    private boolean isEmpty(String str){
        return str==null || str.isEmpty();
    }

    /**
     * 获取数据库名
     */
    private String getDbName() {
        char[] urlCharArr=url.toCharArray();
        int length = urlCharArr.length;
        int questionMarkIndex=-1;
        int inclinedRodIndex=-1;
        for (int i = length-1; i >0; i--) {
            char c = urlCharArr[i];
            switch (c){
                case '?':
                    questionMarkIndex=i;
                    break;
                case '/':
                    if(inclinedRodIndex==-1)
                        inclinedRodIndex=i;
                    break;
            }
        }
        if(questionMarkIndex==-1) questionMarkIndex=length-1;
        String dbName = url.substring(inclinedRodIndex+1,questionMarkIndex);
        return dbName;
    }

    public void setPingSql(String pingSql) {
        this.pingSql = pingSql;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setConnectionPooledId(String connectionPooledId) {
        this.connectionPooledId = connectionPooledId;
    }

    public void setSelectProcedureSql(String selectProcedureSql) {
        this.selectProcedureSql = selectProcedureSql;
    }

    public void setSelectTableSql(String selectTableSql) {
        this.selectTableSql = selectTableSql;
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

    public void addDriverProp(String name,String value){
        if(this.driverProperties==null){
            this.driverProperties=new Properties();
        }
        this.driverProperties.setProperty(name,value);
    }

    @Override
    public String toString() {
        return "DataSourceConfig{" +
                "connectionPooledId='" + connectionPooledId + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driver='" + driver + '\'' +
                ", selectTableSql='" + selectTableSql + '\'' +
                ", selectProcedureSql='" + selectProcedureSql + '\'' +
                ", pingSql='" + pingSql + '\'' +
                ", groupId='" + groupId + '\'' +
                ", driverProperties=" + driverProperties +
                '}';
    }
}
