package com.cabbage.firetic.model;

import com.cabbage.firetic.ui.uiUtils.Constants;

@SuppressWarnings("unused")
public class Game {

    private final String player1Id;
    private String player2Id;
    private final String player1Name;
    private String player2Name;
    private int currentPlayer;

    private int lastMove;
    private int[] ownership;
    private int[] localWinner;
    private int globalWinner;

    public Game(User player1) {
        this.player1Id = player1.getUserId();
        this.player1Name = player1.getUserName();
        this.currentPlayer = Constants.Player1Token;

        this.lastMove = Constants.NotChosen;
        this.ownership = new int[Constants.BoardCount * Constants.GridCount];
        this.localWinner = new int[Constants.BoardCount];
        this.globalWinner = Constants.OpenGrid;
    }
}
