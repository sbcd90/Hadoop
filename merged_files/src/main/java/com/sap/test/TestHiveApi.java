package com.sap.test;

import com.sap.i076326.HiveApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TestHiveApi {
    private static HiveApi hiveApi;

    public TestHiveApi() {
        try{
            hiveApi = new HiveApi();
        }catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testCreateTable() {
        HashMap map = new HashMap<String, String>();
        map.put("id", "int");
        map.put("name", "string");
        try{
            hiveApi.createTable("first4", map);
        }catch(java.sql.SQLException e) {

        }
    }

    public void testDeleteTable() {
        try{
            hiveApi.deleteTable("first4");
        }catch (java.sql.SQLException e) {

        }
    }

    public void testInsertIntoTable() {
        ArrayList<String> values = new ArrayList<String>();
        values.add("1");
        values.add("\"Subho\"");

        try{
            hiveApi.insertIntoTable("first4", values);
        }catch (java.sql.SQLException e) {

        }
    }

    public void testReadFromTable() {
        try{
            ArrayList<Integer> resultSet = hiveApi.readFromTable("first4", "id", "1");
            for(Integer result: resultSet) {
                System.out.println(result);
            }
        }catch(java.sql.SQLException e){

        }
    }

    public void testShowTables() {
        try{
            System.out.println(hiveApi.showTables("first4"));
        }catch(java.sql.SQLException e){

        }
    }
    public static void main(String[] args) {
        TestHiveApi hive = new TestHiveApi();
        hive.testDeleteTable();
        hive.testCreateTable();
        hive.testInsertIntoTable();
        hive.testReadFromTable();
        hive.testShowTables();
    }
}