package com.example.my2048.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.my2048.RankistAIDL;
import com.example.my2048.model.Score;
import com.example.my2048.util.SQLiteHelper;

public class RankService extends Service {
    private RankistAIDL mRank;


    public void onCreate(){
        super.onCreate();
    }

    public RankService() {}


    private final RankistAIDL.Stub rank = new RankistAIDL.Stub() {
        @Override
        public void sendScore(String score, String player) throws RemoteException {
            Score score1 = new Score();
            score1.setMscore(score);
            score1.setPlayer(player);
            SQLiteHelper.with(this).insert(score1);
        }
        @Override
        public String getScore() throws RemoteException {
            return "123";
        }

    };


    @Override
    public IBinder onBind(Intent intent) {
        return rank;
    }
}