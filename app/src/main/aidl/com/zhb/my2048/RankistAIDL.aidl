// RankistAIDL.aidl
package com.zhb.my2048;
// Declare any non-default types here with import statements

interface RankistAIDL{
    void sendScore(String score, String player, long time);
    void initSaveGame();
    void SaveGame(int id,boolean isOver, String text);
    void setBestScore(String score);
    String getBestScore();
    boolean isMute();
    void setMute(boolean isMute);
    void setTime(long time);
    long getTime();
    void initScore();
    void setPauseScore(String pPauseScore);
    String getPauseScore();
    void setGameMode(int GameMode);
    int getGameMode();
    }