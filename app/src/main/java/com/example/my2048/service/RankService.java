package com.example.my2048.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.my2048.RankistAIDL;

public class RankService extends Service {
    public RankService() {
    }
    private final RankistAIDL.Stub rank = new RankistAIDL.Stub() {
        @Override
        public String getScore() throws RemoteException {
            return "123";
        }

        @Override
        public void sendScore(String score) throws RemoteException {

        }
    };

    public void onCreate(){
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return rank;
    }
}