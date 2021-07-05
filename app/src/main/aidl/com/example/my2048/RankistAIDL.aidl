// RankistAIDL.aidl
package com.example.my2048;
// Declare any non-default types here with import statements

interface RankistAIDL{
    void sendScore(String score, String player);
    void initSaveGame();
    void Savegame(int id,boolean isOver, String text);
    void setBestScore(String score);
    String getBestScore();

}