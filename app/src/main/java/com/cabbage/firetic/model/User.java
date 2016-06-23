package com.cabbage.firetic.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String userName;
    public List<Game> matches;

    public User(String userName) {
        this.userName = userName;
        matches = new ArrayList<>();
    }

}
