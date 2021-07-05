package com.example.my2048.model;

public class Score {
    String mScore;

    public Score() {

    }
    public String getMscore() { return mScore;  }
    public void setMscore(String mscore) { this.mScore = mscore; }
    String player;
    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }
    public Score(String x, String  y){
        mScore = x;
        player = y;
    }
}
