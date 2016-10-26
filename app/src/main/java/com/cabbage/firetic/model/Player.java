package com.cabbage.firetic.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Player implements Serializable {

    private String userId;
    private String userName;
    private List<String> matchIdList;

    public Player() {
    }

    public Player(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.matchIdList = new ArrayList<>();
        matchIdList.add("first game");
        matchIdList.add("second game");
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getMatchIdList() {
        return matchIdList;
    }

    public void setMatchIdList(List<String> matchIdList) {
        this.matchIdList = matchIdList;
    }

    public void addMatchId(String matchId) {
        if (matchIdList.indexOf(matchId) == -1) {
            matchIdList.add(matchId);
        } else {
            throw new IllegalStateException();
        }

    }
}
