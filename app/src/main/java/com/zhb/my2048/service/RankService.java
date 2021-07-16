package com.zhb.my2048.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.IBinder;
import android.os.RemoteException;

import com.zhb.my2048.R;
import com.zhb.my2048.RankWidget;
import com.zhb.my2048.RankistAIDL;
import com.zhb.my2048.base.MyApplication;
import com.zhb.my2048.model.*;
import com.zhb.my2048.util.SQLiteHelper;


/**
 * @author zhb
 */
public class RankService extends Service {
    SharedPreferences sp;
    MyApplication mp;
    @Override
    public void onCreate(){
        super.onCreate();
    }

    public RankService() {}

    private void updateWidget(){
        Context context = RankService.this;
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName provider = new ComponentName(context, RankWidget.class);
        int[] ids = manager.getAppWidgetIds(provider);
        manager.notifyAppWidgetViewDataChanged(ids, R.id.rank_list);
    }
    private final RankistAIDL.Stub rank = new RankistAIDL.Stub() {
        @Override
        public void sendScore(String score, String player, long time) throws RemoteException {
            Score score1 = new Score();
            score1.setScore(score);
            score1.setPlayer(player);
            score1.setTime(time);
            SQLiteHelper.with(RankService.this).insert(score1);
            updateWidget();
        }

        @Override
        public void SaveGame(int id, boolean isOver, String text){
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

        @Override
        public void setTime(long time) throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            editor.putLong("time", time);
            editor.apply();
        }

        @Override
        public long getTime() throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            return sp.getLong("time",0);
        }

        @Override
        public void initScore() throws RemoteException {
            SQLiteHelper.with(RankService.this).deleteTable(Score.class.getSimpleName());
        }


        @Override
        public void setPauseScore(String pPauseScore) throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            editor.putString("pauseScore", pPauseScore);
            editor.apply();
        }

        @Override
        public String getPauseScore() throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            return sp.getString("pauseScore","0");
        }

        @Override
        public void setGameMode(int GameMode) throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
            editor.putInt("GameMode", GameMode);
            editor.apply();
        }

        @Override
        public int getGameMode() throws RemoteException {
            sp = getSharedPreferences("sp_score", MODE_PRIVATE);
            return sp.getInt("GameMode",0);
        }

    };
    @Override
    public IBinder onBind(Intent intent) {
        return rank;
    }
}