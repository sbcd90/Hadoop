package com.sap.i076326;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HiveApi {
    private final String driverName = "org.apache.hive.jdbc.HiveDriver";
    private final Connection con;

    public HiveApi() throws ClassNotFoundException, java.sql.SQLException {
        Class.forName(driverName);
        con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "");
    }

    public boolean showTables(String tableName) throws java.sql.SQLException {
        Statement stmt = con.createStatement();
        String statement = "show tables like '" + tableName + "'";
        ResultSet rs = stmt.executeQuery(statement);

        if(rs.next())
            return true;
        return false;
    }

    public boolean createTable(String tableName, HashMap<String,String> columns) throws java.sql.SQLException {
        String colString = "";
        for(Map.Entry item: columns.entrySet()) {
            colString = colString + " " + item.getKey() + " " + item.getValue() + ",";
        }
        colString = colString.substring(0, colString.length() - 1);

        Statement stmt = con.createStatement();
        return stmt.execute("create table " + tableName + "( " + colString + " )");
    }

    public boolean deleteTable(String tableName) throws java.sql.SQLException {
        Statement stmt = con.createStatement();
        return stmt.execute("drop table " + tableName);
    }

    public boolean insertIntoTable(String tableName, ArrayList<String> values) throws java.sql.SQLException {
        String colValString = "";
        for(String value: values) {
            colValString = colValString + value + ",";
        }
        colValString = colValString.substring(0, colValString.length() - 1);

        Statement stmt = con.createStatement();
        String statement = "insert into " + tableName + " values(" + colValString + ")";
        return stmt.execute(statement);
    }

    public boolean deleteFromTable(String tableName, String keyName, String keyVal) throws java.sql.SQLException {
        Statement stmt = con.createStatement();
        String statement = "delete from " + tableName + " where " + keyName + " = " + keyVal;
        return stmt.execute(statement);
    }

    //should be made generic, currently works only with Integer
    public ArrayList<Integer> readFromTable(String tableName, String keyName, String keyVal) throws java.sql.SQLException {
        Statement stmt = con.createStatement();
        String statement = "select offset, length from " + tableName + " where " + keyName + " = " + keyVal;
        ResultSet rs = stmt.executeQuery(statement);

        ArrayList<Integer> resultSet = new ArrayList<Integer>();
        while(rs.next()) {
            resultSet.add(rs.getInt("offset"));
            resultSet.add(rs.getInt("length"));
        }
        return resultSet;
    }
}