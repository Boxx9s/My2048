package com.example.my2048.base;

import android.app.Application;
import com.example.my2048.util.SQLiteHelper;
import com.example.my2048.model.Score;
import com.example.my2048.model.saveGame;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
    }

    public void initUtils() {
        //
        SQLiteHelper.with(this).createTable(Score.class);
        SQLiteHelper.with(this).createTable(saveGame.class);
    }
}
