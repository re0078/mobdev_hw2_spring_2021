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
    private int maxRecords = 5;
    private BookmarkDBHelper bookmarkDBHelper;

    public static BookmarkRepository getInstance(Context context, int maxRecords) {
        BOOKMARK_REPOSITORY.bookmarkDBHelper = new BookmarkDBHelper(context);
        BOOKMARK_REPOSITORY.maxRecords = maxRecords;
        return BOOKMARK_REPOSITORY;
    }

    public List<Bookmark> getLimitedCoins(int offset) {
        SQLiteDatabase db = bookmarkDBHelper.getReadableDatabase();
        String[] columns = {_ID, COIN_ID, CURR_NAME, CURR_SYMBOL, PRICE_USD, H_CHANGE_PERCENT, D_CHANGE_PERCENT, W_CHANGE_PERCENT};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null,
                null, null, null, null);
        List<Bookmark> bookmarks = new ArrayList<>();
        while (cursor.moveToNext()) {
            bookmarks.add(readCoin(cursor));
        }
        cursor.close();
        return bookmarks;
    }

    private Bookmark readCoin(Cursor cursor) {
        Bookmark bookmark = new Bookmark();
        bookmark.setDbId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
        bookmark.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COIN_ID)));
        bookmark.setName(cursor.getString(cursor.getColumnIndexOrThrow(CURR_NAME)));
        bookmark.setSymbol(cursor.getString(cursor.getColumnIndexOrThrow(CURR_SYMBOL)));
        bookmark.setPriceUsd(cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_USD)));
        bookmark.setHChangePercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(H_CHANGE_PERCENT)));
        bookmark.setDChangePercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(D_CHANGE_PERCENT)));
        bookmark.setWChangePercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(W_CHANGE_PERCENT)));
        return bookmark;
    }

    public void putCoins(List<Bookmark> bookmarks) {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        bookmarks.forEach(bookmark -> db.insert(TABLE_NAME, null, setCoinValues(bookmark, false)));
        Log.d("db-TAG", "size: " + bookmarks.size());
    }

    public void updateCoins(List<Bookmark> bookmarks) {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        bookmarks.forEach(bookmark -> {
            String selection = COIN_ID + " = " + bookmark.getId();
            db.update(TABLE_NAME, setCoinValues(bookmark, true), selection, null);
        });
    }

    public void deleteCoins() {
        SQLiteDatabase db = bookmarkDBHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    private ContentValues setCoinValues(Bookmark bookmark, boolean isUpdate) {
        ContentValues values = new ContentValues();
        if (!isUpdate)
            values.put(COIN_ID, bookmark.getId());
        values.put(CURR_NAME, bookmark.getName());
        values.put(CURR_SYMBOL, bookmark.getSymbol());
        values.put(PRICE_USD, bookmark.getPriceUsd());
        values.put(H_CHANGE_PERCENT, bookmark.getHChangePercentage());
        values.put(D_CHANGE_PERCENT, bookmark.getDChangePercentage());
        values.put(W_CHANGE_PERCENT, bookmark.getWChangePercentage());
        return values;
    }
}
