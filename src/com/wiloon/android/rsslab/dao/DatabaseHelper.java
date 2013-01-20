package com.wiloon.android.rsslab.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.wiloon.android.rsslab.common.AppConstant;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/1/12
 * Time: 4:55 PM
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        if (instance == null) {
            instance = new DatabaseHelper(context, name, cursorFactory, version);
        }
        return instance;
    }

    public static DatabaseHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tag
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_TAG +
                " (id TEXT, tagSortId TEXT, label TEXT, unReadCount INTEGER DEFAULT 0, newestItemTimestamp TEXT);");

        //feed
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_FEED +
                " (id TEXT, title TEXT, sortId TEXT, firstItemMSec TEXT, unReadCount INTEGER DEFAULT 0, " +
                "newestItemTimestamp TEXT);");

        //feed tag mapping
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_FEED_TAGS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, feedId TEXT, tagId TEXT);");

        //unread count
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_UNREAD_COUNT +
                " (id TEXT, count INTEGER, timeStamp INTEGER);");

        //article
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_ARTICLE +
                " (id TEXT, timestampUsec TEXT, articleId TEXT,  href TEXT, feedId TEXT, title TEXT, content TEXT, " +
                "readStatus TEXT, imgSyncStatus TEXT, onlineStatus TEXT);");

        //article img mapping
        db.execSQL("CREATE TABLE " + AppConstant.TABLE_NAME_ARTICLE_IMAGE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "articleId TEXT, imgFilePath TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}