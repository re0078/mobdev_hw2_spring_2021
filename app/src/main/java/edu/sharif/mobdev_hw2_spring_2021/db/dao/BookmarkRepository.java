package edu.sharif.mobdev_hw2_spring_2021.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static android.provider.BaseColumns._ID;
import static edu.sharif.mobdev_hw2_spring_2021.db.entity.BookmarkEntry.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookmarkRepository {
    private static final BookmarkRepository BOOKMARK_REPOSITORY = new BookmarkRepository();
    private BookmarkDBHelper bookmarkDBHelper;

    public static BookmarkRepository getInstance(Context context) {
        BOOKMARK_REPOSITORY.bookmarkDBHelper = new BookmarkDBHelper(context);
        return BOOKMARK_REPOSITORY;
    }

    public List<Bookmark> getBookmarks() {
        SQLiteDatabase db = bookmarkDBHelper.getReadableDatabase();
        String[] columns = {_ID, MARK_NAME, LON_VAL, LAT_VAL};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null,
                null, null, null, null);
        List<Bookmark> bookmarks = new ArrayList<>();
        while (cursor.moveToNext()) {
            bookmarks.add(readBookmark(cursor));
        }
        cursor.close();
        return bookmarks;
    }

    private Bookmark readBookmark(Cursor cursor) {
        Bookmark bookmark = new Bookmark();
        bookmark.setDbId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
        bookmark.setName(cursor.getString(cursor.getColumnIndexOrThrow(MARK_NAME)));
        bookmark.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(LON_VAL)));
        bookmark.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(LAT_VAL)));
        return bookmark;
    }

    public void putBookmark(Bookmark bookmark) {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        db.insert(TABLE_NAME, null, saveBookmark(bookmark, false));
    }

    public void updateBookmark(Bookmark bookmark) {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        String selection = MARK_NAME + " = " + bookmark.getName();
        db.update(TABLE_NAME, saveBookmark(bookmark, true), selection, null);
    }

    public void deleteBookmark(Bookmark bookmark) {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        String selection = MARK_NAME + " = " + bookmark.getName();
        db.delete(TABLE_NAME, selection, null);
    }

    private ContentValues saveBookmark(Bookmark bookmark, boolean isUpdate) {
        ContentValues values = new ContentValues();
        if (!isUpdate)
            values.put(MARK_NAME, bookmark.getName());
        values.put(LON_VAL, bookmark.getLongitude());
        values.put(LAT_VAL, bookmark.getLatitude());
        return values;
    }
}
