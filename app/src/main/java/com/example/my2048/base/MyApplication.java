package com.example.my2048.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.my2048.RankistAIDL;
import com.example.my2048.service.RankService;
import com.example.my2048.util.SQLiteHelper;
import com.example.my2048.model.Score;
import com.example.my2048.model.SaveGame;

public class MyApplication extends Application {
    public RankistAIDL aidl ;
    public boolean isContiune = false;
    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
        Intent intent = new Intent("com.example.my2048.service");
        intent.setPackage("com.example.my2048");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                    aidl = RankistAIDL.Stub.asInterface(service);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        },BIND_AUTO_CREATE);
    }

    public boolean isContiune() {
        return isContiune;
    }

    public void setContiune(boolean contiune) {
        isContiune = contiune;
    }

    public void initUtils() {
        //
        SQLiteHelper.with(this).createTable(Score.class);
        SQLiteHelper.with(this).createTable(SaveGame.class);
    }
}
