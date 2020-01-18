#前言
该项目属于我抱着学习的心态去做的项目，并没有受到专业的测试，不值得在生产中使用，仅仅供读者参考。如果想要用类似的功能，建议还是使用当当网的sharding-jdbc，赋上该官网https://shardingsphere.apache.org/document/current/cn/overview/。
#简介
这是负载均衡的跨驱动的解耦的连接池，它提供：
1. 多个数据库查询操作的负载均衡
2. 保证多个同类的数据库的更新操作的事务
3. 跨数据库驱动,以及不同业务的数据库
4. 高解耦，用户只需配置一下数据源，其他数据库的操作都无需再与本框架耦合。
5. 自动检查失效的数据库连接池，并恢复该连接池的正常运行
6. 可在Spring中直接通过注入的方式即可使用DataSource，或者将DataSource配置在JPA、Hibernate或MyBatis中使用。
#具体流程
![BinMuiltDataSource具体执行流程.png](https://upload-images.jianshu.io/upload_images/18292929-f0dbdae099062f29.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#实例
```java
package bin.framework.test;

import bin.framework.BinMultiDataSource;
import bin.framework.DataSourceCilentNode;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Arrays;

public class JdbcSingleDataSourceTest {

    BinMultiDataSource binMultiDataSource;

    @Before
    public void init(){
        DataSourceCilentNode binTestDataSourceCilentNode =new DataSourceCilentNode();
        //groupId就是指负载均衡组ID,同一个groupId的DataSourceCilentNode应该其对应的数据库，表结构，表数据都相同，以防止负载均衡操作时导致
        //的因不一致而抛出异常
        binTestDataSourceCilentNode.setGroupId("1");
        binTestDataSourceCilentNode.setDriver("com.mysql.cj.jdbc.Driver");
        binTestDataSourceCilentNode.setUsername("root");
        binTestDataSourceCilentNode.setPassword("root");
        binTestDataSourceCilentNode.setUrl("jdbc:mysql://127.0.0.1:3306/bin_test?serverTimezone=UTC&characterEncoding=utf8");

        DataSourceCilentNode binTest2DataSourceCilentNode =new DataSourceCilentNode();
        binTest2DataSourceCilentNode.setGroupId("1");
        binTest2DataSourceCilentNode.setDriver("com.mysql.cj.jdbc.Driver");
        binTest2DataSourceCilentNode.setUsername("root");
        binTest2DataSourceCilentNode.setPassword("root");
        binTest2DataSourceCilentNode.setUrl("jdbc:mysql://127.0.0.1:3306/bin_test2?serverTimezone=UTC&characterEncoding=utf8");
        binMultiDataSource=new BinMultiDataSource(Arrays.asList(binTestDataSourceCilentNode, binTest2DataSourceCilentNode));

        DataSourceCilentNode ydddDataSourceCilentNode =new DataSourceCilentNode();
        ydddDataSourceCilentNode.setGroupId("2");
        ydddDataSourceCilentNode.setDriver("oracle.jdbc.driver.OracleDriver");
        ydddDataSourceCilentNode.setUsername("yddd");
        ydddDataSourceCilentNode.setPassword("yddd123");
      ydddDataSourceCilentNode.setUrl("jdbc:oracle:thin:@192.168.191.2:1621/orcl");
        binMultiDataSource=new BinMultiDataSource(Arrays.asList(binTestDataSourceCilentNode, ydddDataSourceCilentNode));
    }

    @Test
    public void test_noSpecifyGroupIdSQL() throws SQLException {
        Connection connection = binMultiDataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from dept");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            String name=resultSet.getString("name");
            System.out.println(String.format("id=%s,name=%s",id,name));
        }

        Connection ydddConnection=binMultiDataSource.getConnection();
        PreparedStatement ydddPreparedStatement = ydddConnection.prepareStatement("select * from t_b_user_foura");
        ResultSet ydddResultSet = ydddPreparedStatement.executeQuery();
        while (ydddResultSet.next()){
            String id = ydddResultSet.getString("ID");
            String userId=ydddResultSet.getString("USER_ID");
            String fouraUsername=ydddResultSet.getString("FOURA_USERNAME");
            System.out.println(String.format("id=%s,userId=%s,fouraUsername=%s",id,userId,fouraUsername));
        }
    }

    @Test
    public void test_specifyGroupIdSQL() throws SQLException {
        Connection connection = binMultiDataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from [1]dept");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            String name=resultSet.getString("name");
            System.out.println(String.format("id=%s,name=%s",id,name));
        }
    }

    @Test
    public void test_joinSQL() throws SQLException {
        Connection connection = binMultiDataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select u.id user_id,u.name user_name,d.id dept_id,d.name dept_name" +
                " from user u left join dept d on d.id=u.dept_id where u.id=?");
        preparedStatement.setString(1,"1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String userId = resultSet.getString("user_id");
            String userName=resultSet.getString("user_name");
            String deptId=resultSet.getString("dept_id");
            String deptName=resultSet.getString("dept_name");
            System.out.println(String.format("userId=%s,userName=%s,deptId=%s,deptName=%s",userId,userName,deptId,deptName));
        }
    }


    @Test
    public void test_insertSQL() throws SQLException {
        Connection connection=binMultiDataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (ID,NAME,AGE,DES_NAME,DEPT_ID) VALUE (?,?,?,?,?)");
        preparedStatement.setString(1,"15");
        preparedStatement.setString(2,"五六七2");
        preparedStatement.setString(3,"25");
        preparedStatement.setString(4,"刺客");
        preparedStatement.setString(5,"2");
        int i = preparedStatement.executeUpdate();
        connection.rollback();
        System.out.println("test_insert : "+i);
    }

    @Test
    public void test_updateSQL() throws SQLException{
        Connection connection=binMultiDataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE USER SET NAME=?,AGE=?,DES_NAME=?,DEPT_ID=? WHERE ID=?");
        preparedStatement.setString(1,"五六七-up");
        preparedStatement.setString(2,"25");
        preparedStatement.setString(3,"刺客");
        preparedStatement.setString(4,"2");
        preparedStatement.setString(5,"5");
        int i = preparedStatement.executeUpdate();
        connection.commit();
        System.out.println("test_insert : "+i);
    }

    @Test
    public void test_deleteSQL() throws SQLException {
        Connection connection=binMultiDataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USER WHERE ID= ?");
        preparedStatement.setString(1,"15");
        int i = preparedStatement.executeUpdate();
        connection.commit();
        System.out.println(("test_deleteSQL : "+i));
    }


    @Test
    public void test_procedureSpecifyGroupId() throws SQLException {
        Connection connection = binMultiDataSource.getConnection();
        CallableStatement callableStatement = connection.prepareCall("call [1]delete_matches(?)");
        callableStatement.setString(1,"5");
        int i = callableStatement.executeUpdate();
        System.out.println("test_procedureSpecifyGroupId : "+i);
    }

    @Test
    public void test_procedure() throws SQLException {
        Connection connection = binMultiDataSource.getConnection();
        CallableStatement callableStatement = connection.prepareCall("call delete_matches(?)");
        callableStatement.setString(1,"5");
        int i = callableStatement.executeUpdate();
        System.out.println("test_procedure : "+i);
    }
}

```
#源码下载
https://github.com/bin892550156/BinMuitlDataSource