package edu.sharif.mobdev_hw2_spring_2021.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static edu.sharif.mobdev_hw2_spring_2021.db.entity.BookmarkEntry.SQL_CREATE_ENTRIES;
import static edu.sharif.mobdev_hw2_spring_2021.db.entity.BookmarkEntry.SQL_DELETE_ENTRIES;

public class BookmarkDBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "rubylan_db";
    static final Integer DATABASE_VERSION = 1;

    public BookmarkDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}
