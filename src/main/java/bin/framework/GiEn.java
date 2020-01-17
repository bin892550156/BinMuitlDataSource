package bin.framework;

/**
 * 查找对应连接配置的依据 封装类
 */
public class GiEn {
    /**
     * 执行名，可能是 表名，又或者存储过程名
     */
    private String executeName;
    /**
     * 分组ID
     */
    private String groupId;
    /**
     * SQL命令类型
     */
    private SqlCommand sqlCommand;

    /**
     * 是否是存储过程
     */
    private boolean isProcedure;

    /**
     *
     * @param giEnExpression 表达式，可以是'[groupId]tableName'，又或者是'tableName'
     */
    public GiEn(String giEnExpression){
        int bracketStartIndex = giEnExpression.indexOf('[');
        if(bracketStartIndex!=-1){
            int bracketEndIndex=giEnExpression.indexOf(']',bracketStartIndex);
            groupId=giEnExpression.substring(bracketStartIndex+1,bracketEndIndex);
            executeName =giEnExpression.replace(String.format("[%s]",groupId),"");
        }else{
            executeName =giEnExpression;
        }
    }

    public SqlCommand getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(SqlCommand sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public boolean isProcedure() {
        return isProcedure;
    }

    public void setProcedure(boolean procedure) {
        isProcedure = procedure;
    }

    public String getGiTnExpression(){
        return String.format("[%s]%s",groupId, executeName);
    }

    public String getExecuteName() {
        return executeName;
    }

    public void setExecuteName(String executeName) {
        this.executeName = executeName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "GiTn{" +
                "tableName='" + executeName + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}