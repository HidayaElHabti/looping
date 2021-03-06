package com.ehtp.looping;

import android.app.Application;

import java.util.ArrayList;

public class looping extends Application {
    private String gameID; //all ok
    private String playerID; //all ok
    private String playerName; //all ok
    private String image; //!imposter
    private String hint; // imposter
    private boolean isHost; //all ok
    private boolean isImposter; //all
    private int nbPlayers;//host ok
    public ArrayList<String> playersIDs = new ArrayList<String>();; //host
    public ArrayList<String> playersNames = new ArrayList<String>();; //host
    private int nbRounds; //host ok
    private int currentRound; //all

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public boolean getIsImposter(){
        return isImposter;
    }
    public void setIsImposter(boolean isImposter){
        this.isImposter = isImposter;
    }

    public int getNbRounds() {
        return nbRounds;
    }

    public void setNbRounds(int nbRounds) {
        this.nbRounds = nbRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public boolean getIsHost(){
        return isHost;
    }

    public void setIsHost(boolean isHost){
        this.isHost = isHost;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers = nbPlayers;
    }

}
