package edu.sharif.mobdev_hw2_spring_2021.db.entity;

import android.provider.BaseColumns;

public class BookmarkEntry implements BaseColumns {
    public static final String TABLE_NAME = "bookmark_table";
    public static final String MARK_NAME = "name";
    public static final String LON_VAL = "longitude";
    public static final String LAT_VAL = "latitude";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
            "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MARK_NAME + " VARCHAR(32) UNIQUE," +
            LON_VAL + " DOUBLE," +
            LAT_VAL + " DOUBLE" +
            ")";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
