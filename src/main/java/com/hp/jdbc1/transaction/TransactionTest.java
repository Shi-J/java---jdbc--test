package com.hp.jdbc1.transaction;

import com.hp.jdbc1.util.JdbcUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionTest
{
    @Test
    public void  test1 (){
        Connection connection = null;
        try
        {
            //  1.获取连接
             connection = JdbcUtil.getConnection();
            //  事务 ： zs 用户余额减100， ls用户余额加100

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //  2.设置事务DML事务不自动提交
            connection.setAutoCommit(false);

            //  3.开启事务操作
            String sql1 = "update dome4 set balance = balance -100 where name = ?";
            JdbcUtil.updateTx(connection,sql1,"zs");

            //  在此过程中模拟中途发送了错误
            System.out.println(10/ 0);


            String sql2 = "update dome4 set balance = balance +100 where name = ?";
            JdbcUtil.updateTx(connection,sql2,"ls");

            System.out.println("转账成功");
            //  4.事务处理完成提交事务
            connection.commit();

        }
        catch (Exception e)
        {
            //   如果出现异常回滚操作
            try
            {
                connection.rollback();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        finally
        {
            //  修改数据库自动提交事务，方便数据库连接池正常使用
            try
            {
                connection.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            JdbcUtil.closes(connection,null);
        }


    }

    @Test
    public void test2(){
        String sql1 = "update dome4 set balance = balance -100 where name = ?";
        JdbcUtil.update(sql1,"zs");

        //  在此过程中模拟中途发送了错误
        System.out.println(10/ 0);


        String sql2 = "update dome4 set balance = balance +100 where name = ?";
        JdbcUtil.update(sql2,"ls");
    }
}
