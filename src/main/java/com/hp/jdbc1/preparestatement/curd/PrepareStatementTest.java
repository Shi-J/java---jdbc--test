package com.hp.jdbc1.preparestatement.curd;

import com.hp.jdbc1.JdbcTest;
import com.hp.jdbc1.model.Dome1;
import com.hp.jdbc1.util.JdbcUtil;
import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class PrepareStatementTest
{

    //  添加操作
    @Test
    public void test5()
    {
        InputStream is = null;
        PreparedStatement ps = null;
        Connection connection = null;
        try
        {
            //  1. 获取properties文件中的4个基本信息
            is = JdbcTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            //  2.加载驱动
            Class.forName(driverClass);
            //  3.获取链接
            connection = DriverManager.getConnection(url, user, password);

            //  4.获取预编译sql  ?代表预编译sql
            String sql = "INSERT INTO dome1(NAME,PASSWORD) VALUES(?,?)";
            ps = connection.prepareStatement(sql);

            //  5.填充占位符
            ps.setString(1, "李四");
            ps.setString(2, "123456");

            //  6.执行操作
            ps.execute();

            System.out.println("添加成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            //  7.关闭资源

            try
            {
                if (is != null)
                    is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (ps != null)
                    ps.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }

    }

    //  更新操作
    @Test
    public void test2()
    {
        Connection connection = null;
        PreparedStatement ps = null;
        try
        {
            //  1.获取数据库连接
            connection = JdbcUtil.getConnection();
            //  2.获取preparestatement
            String sql = "update dome1 set name =? where name='zs' ";
            ps = connection.prepareStatement(sql);

            ps.setString(1, "张大三");
            //  3.执行
            ps.execute();

            System.out.println("更新成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  4.关闭
            JdbcUtil.closes(connection, ps);
        }

    }


    //  删除操作
    @Test
    public void test3()
    {
        Connection connection = null;
        PreparedStatement ps = null;
        try
        {
            //  1.获取数据库连接
            connection = JdbcUtil.getConnection();
            //  2.获取preparestatement
            String sql = "delete  from dome1 where name = ? ";
            ps = connection.prepareStatement(sql);

            ps.setString(1, "李四");
            //  3.执行
            ps.execute();

            System.out.println("删除成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  4.关闭
            JdbcUtil.closes(connection, ps);
        }

    }

    //  测试通用增删改
    @Test
    public void test4()
    {
        String sql = "  INSERT INTO dome1(NAME,PASSWORD) VALUES(?,?);";
        try
        {
            JdbcUtil.update(sql, "王五", "123457");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //  查询
    @Test
    public void test6()
    {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            //  1.获取数据库连接
            connection = JdbcUtil.getConnection();

            //  2.获取preparestatement
            String sql = "select name ,password  from dome1 where name=?";
            ps = connection.prepareStatement(sql);

            // 设置占位符
            ps.setObject(1, "张大三");

            //  3.执行并返回结果集
            rs = ps.executeQuery();

            //  处理结果集
            if (rs.next())
            { //   next方法，查询是否有数据如果有就为true 如果没有为false
                //  获取当前查询的数据字段
                String name = rs.getString(1);
                String password = rs.getString(2);

                Dome1 dome1 = new Dome1(name, password);
                System.out.println(dome1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  4.关闭资源
            JdbcUtil.closes(connection, ps, rs);

        }


    }

    //  测试通用查询
    @Test
    public void test7() throws Exception
    {
        String sql = "select name,password from dome1 where name= ?";
        Dome1 dome1 = JdbcUtil.query(sql, "王五");
        System.out.println(dome1);

    }

    @Test
    public void blobTest()
    {
        Connection connection = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try
        {
            //  1.获取数据库连接
            connection = JdbcUtil.getConnection();
            String sql = "insert into dome2(img,id) values(?,?)";
            //  2.得到PreparedStatement
            ps = connection.prepareStatement(sql);
            //  3.填充占位符
            fis = new FileInputStream(new File("img1.png"));
            ps.setBlob(1, fis);
            ps.setInt(2, 2);
            //  4.执行
            ps.execute();

            System.out.println("添加成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  5. 关闭流
            JdbcUtil.closes(connection, ps);

            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }

    @Test
    public void blobTest2()
    {
        Connection connection = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        InputStream binaryStream = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try
        {
            //  1.获取数据库连接
            connection = JdbcUtil.getConnection();
            String sql = "select img,id from dome2 where id=?";
            //  2.得到PreparedStatement
            ps = connection.prepareStatement(sql);
            //  3.填充占位符
            ps.setInt(1, 2);
            //  4.执行
            rs = ps.executeQuery();
            //  遍历
            if (rs.next())
            {
                Blob blob = rs.getBlob("img");
                int id = rs.getInt("id");

                //  将Blob文件下载下来
                binaryStream = blob.getBinaryStream();

                fos = new FileOutputStream(new File("img2.png"));
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = binaryStream.read()) != -1)
                {
                    fos.write(bytes, 0, len);
                }


            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  5. 关闭资源
            JdbcUtil.closes(connection, ps, rs);
            try
            {
                if (binaryStream != null)
                    binaryStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test8()
    {
        long l = System.currentTimeMillis();
        Connection connection = null;
        PreparedStatement ps = null;
        try
        {
            connection = JdbcUtil.getConnection();
            String sql = "insert into dome3(name) values(?)";
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 2000; i++)
            {
                ps.setObject(1, "name" + (i + 1));
                ps.execute();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            JdbcUtil.closes(connection, ps);
        }

        System.out.println("执行时间" + (System.currentTimeMillis() - l));


    }


    @Test
    public void test9()
    {
        long l = System.currentTimeMillis();
        Connection connection = null;
        PreparedStatement ps = null;
        try
        {
            connection = JdbcUtil.getConnection();
            String sql = "insert into dome3(name) values(?)";
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 20000; i++)
            {
                ps.setObject(1, "name" + (i + 1));

                //  "攒sql"
                ps.addBatch();

                if ((i % 500) == 0)
                {
                    //  执行sql
                    ps.execute();

                    //  清空Batch
                    ps.clearBatch();
                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            JdbcUtil.closes(connection, ps);
        }

        System.out.println("执行时间" + (System.currentTimeMillis() - l));


    }


    @Test
    public void test10()
    {
        long l = System.currentTimeMillis();
        Connection connection = null;
        PreparedStatement ps = null;
        try
        {
            connection = JdbcUtil.getConnection();
            //  MYSQL数据库本身会自动提交数据，设置不自动提交
            connection.setAutoCommit(false);
            String sql = "insert into dome3(name) values(?)";
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < 20000; i++)
            {
                ps.setObject(1, "name" + (i + 1));

                //  "攒sql"
                ps.addBatch();

                if ((i % 500) == 0)
                {
                    //  执行sql
                    ps.execute();

                    //  清空Batch
                    ps.clearBatch();
                }



            }

            //  最后在提交数据
            connection.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            JdbcUtil.closes(connection, ps);
        }

        System.out.println("执行时间" + (System.currentTimeMillis() - l));


    }


}
