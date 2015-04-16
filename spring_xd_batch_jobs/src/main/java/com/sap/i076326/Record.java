package com.sap.i076326;

public class Record {
    private String str1;
    private String str2;

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr1() {
        System.out.println(str1);
        return str1;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr2() {
        System.out.println(str2);
        return str2;
    }

    public String getConcat() {
        return str1 + str2;
    }
}