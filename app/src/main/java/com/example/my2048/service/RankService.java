package com.example.my2048.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.my2048.MainActivity;
import com.example.my2048.RankistAIDL;
import com.example.my2048.base.MyApplication;
import com.example.my2048.model.*;
import com.example.my2048.util.SQLiteHelper;


/**
 * @author zhb
 */
public class RankService extends Service {
    private RankistAIDL mRank;
    MainActivity M;
    SharedPreferences sp;
    MyApplication mp;
    @Override
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
        public void Savegame(int id, boolean isOver, String text){
            String sql = "update " + SaveGame.class.getSimpleName() + " set text = '" +
                    text + "', isOver = '" + isOver + "' where id = " + id;
            SQLiteHelper.with(RankService.this).update(sql);
        }

        @Override
        public void initSaveGame(){
            SQLiteHelper.with(RankService.this).deleteTable(SaveGame.class);
        }
        @Override
        public void setBestScore(String score){
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            editor.putString("bestScore",score);
            editor.apply();
        }
        @Override
        public String getBestScore(){
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            return sp.getString("bestScore", "0");
        }

        @Override
        public boolean isMute() throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            return sp.getBoolean("mute", true);
        }

        @Override
        public void setMute(boolean isMute) throws RemoteException {
            mp = (MyApplication)getApplication();
            mp.isMute = isMute;
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("mute", isMute);
            editor.apply();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return rank;
    }
}