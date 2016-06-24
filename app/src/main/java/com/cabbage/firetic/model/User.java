package com.cabbage.firetic.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class User {

    private String userId;
    private String userName;
    private List<String> matchIdList;

    public User() {}

    public User(String userId, String userName) {
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

    public void setUserId(String val) {
        this.userId = val;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String val) {
        this.userName = val;
    }

    public List<String> getMatchIdList() {
        return matchIdList;
    }

    public void setMatchIdList(List<String> val) {
        this.matchIdList = val;
    }

    public void addMatchId(String matchId) {
        if (matchIdList.indexOf(matchId) == -1) {
            matchIdList.add(matchId);
        } else {
            throw new IllegalStateException();
        }

    }
}
