package com.hp.jdbc1.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;

public class C3p0Test
{

    //  方式一:硬编码 （不推荐）
    @Test
    public void test1() throws  Exception{
        ComboPooledDataSource cpds=new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("root");

        //设置初始的连接池中的连接数
        cpds.setInitialPoolSize(10);
        Connection connection = cpds.getConnection();
        System.out.println(connection);

    }

    //  方式二:使用配置文件（推荐）
    public void test2(){

    }
}
