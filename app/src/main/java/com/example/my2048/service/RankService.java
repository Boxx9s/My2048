package com.example.my2048.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import com.example.my2048.MainActivity;
import com.example.my2048.RankistAIDL;
import com.example.my2048.model.*;
import com.example.my2048.util.SQLiteHelper;

import org.w3c.dom.Text;

public class RankService extends Service {
    private RankistAIDL mRank;
    MainActivity M;

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
            SQLiteHelper.with(RankService.this).insert(score1);
        }
        @Override
        public String getScore() throws RemoteException {
            return "123";
        }
        SaveGame mSaveGame = new SaveGame();
        public void Savegame(int id, boolean isOver, String text){
            mSaveGame.setId(id);
            mSaveGame.setOver(isOver);
            mSaveGame.setText(text);
            String sql = "update " + SaveGame.class.getSimpleName() + " set text = '" +
                    text + "', isOver = '" + isOver + "' where id = " + id;
            SQLiteHelper.with(RankService.this).update(sql);
        }
        public void initSaveGame(){
            SQLiteHelper.with(RankService.this).deleteTable(SaveGame.class);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return rank;
    }
}