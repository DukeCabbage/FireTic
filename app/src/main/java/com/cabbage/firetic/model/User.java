package com.cabbage.firetic.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class User {

    private String userId;
    private String userName;
    private String password;
    private List<String> matchIdList;

    public User() {
    }

    public User(String userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.matchIdList = new ArrayList<>();
        matchIdList.add("first game");
        matchIdList.add("second game");
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
