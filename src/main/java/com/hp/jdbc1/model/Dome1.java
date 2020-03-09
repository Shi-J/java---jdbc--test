package com.hp.jdbc1.model;

public class Dome1
{
    private String name;
    private String password;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Dome1{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Dome1()
    {
    }

    public Dome1(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
