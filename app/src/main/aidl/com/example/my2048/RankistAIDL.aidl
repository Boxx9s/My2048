// RankistAIDL.aidl
package com.example.my2048;
// Declare any non-default types here with import statements

interface RankistAIDL{
    String getScore();
    void sendScore(String score, String player);
}