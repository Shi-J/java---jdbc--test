package com.hp.jdbc1.util;

import com.hp.jdbc1.JdbcTest;
import com.hp.jdbc1.model.Dome1;

import javax.xml.transform.Result;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;

public class JdbcUtil
{
    public static Connection getConnection() throws Exception{

            //  1. 获取properties文件中的4个基本信息
            InputStream is = JdbcTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties=new Properties();
            properties.load(is);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            //  2.加载驱动
            Class.forName(driverClass);
            //  3.获取链接
            Connection  connection = DriverManager.getConnection(url, user, password);


        return connection;
    }

    //  修改关闭资源
    public static void closes(Connection connection, PreparedStatement ps){

        try
        {
            if( ps != null)
                ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if(connection!= null)
                connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    //  查询关闭资源
    public static void closes(Connection connection, PreparedStatement ps, ResultSet rs){



            try
            {
                if(rs != null)
                rs.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        try
        {
            if( ps != null)
                ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if(connection!= null)
                connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    //  通用增删改
    public static  void update(String sql, Object ... args) {
        Connection connection = null ;
        PreparedStatement ps = null;
        try
        {
            //  1.获取数据库连接
             connection = getConnection();
            //  2.获取PreparedStatement
             ps = connection.prepareStatement(sql);
            //  3.填充占位符,
            for (int i = 0; i <args.length ; i++)
            {
                ps.setObject((i+1), args[i]);
            }
            //  4.执行
            ps.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  5.关闭
            closes(connection , ps);
        }


    }

    //  通用的查询
    public static Dome1 query(String sql, Object ... args)throws  Exception{
        Connection connection = null ;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            //  1.获取数据库连接
            connection = getConnection();
            //  2.获取PreparedStatement
            ps = connection.prepareStatement(sql);
            //  3.填充占位符,
            for (int i = 0; i <args.length ; i++)
            {
                ps.setObject((i+1), args[i]);
            }
            //  4.得到查询的结果集
             rs =ps.executeQuery();
            //  5.得到结果集的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //  通过元数据获取其中的列数
            int columnCount = metaData.getColumnCount();
            if(rs.next()){ //   判断是否还有下一条数据
                //  创建对象
                Dome1 dome1=new Dome1();

                for (int i = 0; i < columnCount ; i++)
                {
                    Object columnValue  = rs.getObject(i + 1);

                    //  获取每个数据的列明
                    String columnName = metaData.getColumnName(i+1);

                    //  通过反射获取javabean中的字段名
                    Field declaredField = Dome1.class.getDeclaredField(columnName);
                    declaredField.setAccessible(true); //   设置属性如果是private修饰的也能够赋值
                    declaredField.set(dome1,columnValue);    //  给属性设置值

                    return dome1;
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  5.关闭
            closes(connection , ps,rs);
        }

        return null;
    }




    //  和事务有关的通用增删改
    public static  void updateTx( Connection connection ,String sql, Object ... args) {
        PreparedStatement ps = null;
        try
        {
            //  1.获取PreparedStatement
            ps = connection.prepareStatement(sql);
            //  2.填充占位符,
            for (int i = 0; i <args.length ; i++)
            {
                ps.setObject((i+1), args[i]);
            }
            //  3.执行
            ps.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  4.关闭
            closes(null , ps);
        }


    }

}
