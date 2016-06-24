package com.cabbage.firetic.model;

@SuppressWarnings("unused")
public class Game {

    private final String player1Id;
    private String player2Id;
    private final String player1Name;
    private String player2Name;

    public Game(User player1) {
        this.player1Id = "";
        this.player1Name = "";
    }
}
