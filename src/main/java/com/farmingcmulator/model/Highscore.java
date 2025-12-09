package com.farmingcmulator.model;

public class Highscore {
    private String playerName;
    private int score;
    
    public Highscore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
