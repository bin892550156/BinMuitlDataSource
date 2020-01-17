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
        ydddDataSourceCilentNode.setUsername("yidongdudao");
        ydddDataSourceCilentNode.setPassword("Ppzx147.");
        ydddDataSourceCilentNode.setUrl("jdbc:oracle:thin:@172.16.19.125:1621/orcl");
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
