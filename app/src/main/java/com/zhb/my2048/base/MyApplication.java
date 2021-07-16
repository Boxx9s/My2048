package com.zhb.my2048.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.zhb.my2048.RankistAIDL;
import com.zhb.my2048.util.SQLiteHelper;
import com.zhb.my2048.model.Score;
import com.zhb.my2048.model.SaveGame;

/**
 * @author zhb
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public RankistAIDL mRankistAIDL;
    public boolean isContinue;
    public String mPlayerName;
    public boolean isMute = new Boolean(true);
    public int GameMode = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initUtils();
        Intent intent = new Intent("com.example.my2048.service");
        intent.setPackage("com.example.my2048");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mRankistAIDL = RankistAIDL.Stub.asInterface(service);
                try {
                    isMute = mRankistAIDL.isMute();
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        }, BIND_AUTO_CREATE);
        mPlayerName = "";
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean Continue) {
        isContinue = Continue;
    }

    public void initUtils() {
        //
        SQLiteHelper.with(this).createTable(Score.class);
        SQLiteHelper.with(this).createTable(SaveGame.class);
    }
}
