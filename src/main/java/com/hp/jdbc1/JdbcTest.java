package com.hp.jdbc1;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcTest
{
    //  方式一
    @Test
    public void test1() throws Exception
    {
        //  获取Driver实现类
        Driver driver = new com.mysql.jdbc.Driver();

        /*
            jdbc:mysql :协议
            localhost  :ip地址
            3306       :默认mysql数据库端口号
            test       :test数据库
        */
        String url = "jdbc:mysql://localhost:3306/test";

        /*
            将账号密码设置到Properties中

        */
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        Connection connect = driver.connect(url, properties);

        System.out.println(connect);
    }

    //  方式二:方式一的迭代:在如下程序中不出现第三方的api，使程序具有更加好的移植性
    @Test
    public void test2() throws Exception
    {
        //  1.获取Driver实现类，利用反射
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        //  1.1获取Driver类实现对象
        Driver driver = (Driver) clazz.newInstance();

        //  2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //  3.提供需要连接的账号密码
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        Connection connect = driver.connect(url, properties);

        System.out.println(connect);
    }


    //  方式三：使用DriverManager替换Driver
    @Test
    public void test3() throws Exception
    {
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();


        String url = "jdbc:mysql://localhost:3306/test";
        String name = "root";
        String password = "root";

        //  1.注册驱动
        DriverManager.registerDriver(driver);

        //  2.获取连接
        Connection connection = DriverManager.getConnection(url, name, password);

        System.out.println(connection);
    }

    //  方式四:在方式三的前提上做优化，只需要获取驱动不需要注册驱动，再源码中驱动已经给我们注册好了
    @Test
    public void test4() throws Exception
    {
        //  1.  获取驱动
        Class.forName("com.mysql.jdbc.Driver");

        /*   com.mysql.jdbc.Driver自动给我们注册了启动

        static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }

        */

        //  提供连接的必要信息
        String url = "jdbc:mysql://localhost:3306/test";
        String name = "root";
        String password = "root";

        //  2.获取连接
        Connection connection = DriverManager.getConnection(url, name, password);

        System.out.println(connection);
    }


    /*
        方式五:将数据库连接需要的4个基本信息声明再配置文件当中，通过读取配置文件的方式来获取链接需要的数据
        好处：实现代码和数据分离,实现了解耦
             需要修改配置文件的信息，可以避免重新打包操作
     */
    @Test
    public  void test5() throws  Exception{
        //  1. 获取properties文件中的4个基本信息
        InputStream is = JdbcTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        System.out.println(is);
        Properties properties=new Properties();
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //  2.加载驱动
        Class.forName(driverClass);
        //  3.获取链接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);


    }

}
