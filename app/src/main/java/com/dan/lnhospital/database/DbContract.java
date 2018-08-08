package com.dan.lnhospital.database;

public class DbContract
{

    public static class DbDetails
    {
       public static final String DBNAME = "Hospital.db";
       public static final int DBVERSION = 1;
    }
    public static final String TABLE_MESSAGE= "tablemessage";
    public static  final String COLUMN_ID="id" ;

    public static class MESSAGE
    {
        public static final String COLUMN_MESSAGE = "message";

    }
}
