package com.example.my2048.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Relin
 * on 2018-09-14.<br/>
 * 数据库打开工具，该工具是继承系统SDK的SQLiteOpenHelper类，
 * 可以帮助你打开或创建一个数据库，同时监听数据库的创建和升级。<br/>
 * The database open utility, the SQLiteOpenHelper class
 * that inherits the system SDK, can help you open or create
 * a database while listening for database creation and updates.
 */
public final class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private com.example.my2048.OnSQLiteOpenHelperListener helperListener;

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, com.example.my2048.OnSQLiteOpenHelperListener helperListener) {
        super(context, name, factory, version);
        this.helperListener = helperListener;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (helperListener != null) {
            helperListener.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (helperListener != null) {
            helperListener.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
