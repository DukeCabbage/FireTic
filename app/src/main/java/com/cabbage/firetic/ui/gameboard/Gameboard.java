package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.Constants;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Gameboard extends RelativeLayout
        implements View.OnClickListener {

    private Callback mCallback;
    private int currentlyZoomed = Constants.Invalid;
    private int currentlyUnlocked = Constants.Invalid;

    private int mCurrentPlayer = Constants.Player1Token;
//    private int mOwnerShip[] = new int[Constants.BoardCount * Constants.GridCount];

    private int[] mLocalWinners = new int[Constants.BoardCount];
    private int[][] gridOwnership = new int[Constants.GridCount][Constants.BoardCount];

    private List<GameboardSector> mSectorList = new ArrayList<>();

    public Gameboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);

        int count = 0;
        for (Order.VerticalOrder vo : Order.VerticalOrder.values()) {
            for (Order.HorizontalOrder ho : Order.HorizontalOrder.values()) {
                GameboardSector tempSector = (GameboardSector) inflater.inflate(R.layout.view_gameboard_sector, this, false);
                // 0,1,2
                // 3,4,5
                // 6,7,8
                tempSector.setTag(count++);
                tempSector.verticalOrder = vo;
                tempSector.horizontalOrder = ho;
                attachSector(tempSector, vo, ho);
            }
        }
    }

    public void setCallback(Callback mCallback) { this.mCallback = mCallback; }

    private void attachSector(GameboardSector sector, Order.VerticalOrder vo, Order.HorizontalOrder ho) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sector.getLayoutParams();
        setAlignments(lp, vo, ho);
        int boardIndex = Order.HorizontalOrder.values().length * vo.ordinal() + ho.ordinal();
        sector.setGameboard(this, boardIndex);
        mSectorList.add(sector);
        this.addView(sector);   // No other child views other than GameboardSectors
    }

    private void setAlignments(RelativeLayout.LayoutParams lp, Order.VerticalOrder vo, Order.HorizontalOrder ho) {
        switch (vo) {
            case TOP:
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case BOTTOM:
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            default:
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
        }

        switch (ho) {
            case LEFT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_START);
                break;
            case RIGHT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_END);
                break;
            default:
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof GameboardSector) {
            int index = indexOfChild(view);
            if (index == currentlyZoomed) {
                ((GameboardSector) view).focusOnSector(false);
            } else {
                unFocusAll();
                ((GameboardSector) view).focusOnSector(true);
                currentlyZoomed = index;
            }
        }
    }

    public void unFocusAll() {
        if (currentlyZoomed != Constants.NotChosen) {
            ((GameboardSector) getChildAt(currentlyZoomed)).focusOnSector(false);
            currentlyZoomed = Constants.NotChosen;
        }
    }

    // Called by sectors, returned value dictates what sector should draw, or draw at all
    int onUserClick(int boardIndex, int gridIndex) {
        Timber.d("User picked (%d, %d)", boardIndex, gridIndex);
        if (currentlyUnlocked != Constants.NotChosen && currentlyUnlocked != boardIndex) {
            // This board is locked
            Timber.d("This grid is locked");
            return Constants.Invalid;
        } else if (gridOwnership[boardIndex][gridIndex] != Constants.NotChosen) {
            // Already set
            Timber.d("This grid is occupied");
            return Constants.Invalid;
        } else {
            gridOwnership[boardIndex][gridIndex] = mCurrentPlayer;
            if (mLocalWinners[boardIndex] != Constants.NotChosen) {
                int tempLocalWinner = checkWin(gridOwnership[boardIndex]);
                if (tempLocalWinner != Constants.NotChosen) {
                    Timber.i("Local winner at board %d is %d", boardIndex, tempLocalWinner);
                    mLocalWinners[boardIndex] = tempLocalWinner;
                    mSectorList.get(boardIndex).setLocalWinner(tempLocalWinner);

                    int globalWinner = checkWin(mLocalWinners);
                    if (globalWinner != Constants.NotChosen) {
                        Timber.i("Global winner is %d, locking whole board", globalWinner);
                        currentlyUnlocked = Constants.Invalid;
                    }
                }
            }

            // Notify observers
            if (mCallback != null) {
                int index = boardIndex * Constants.GridCount + gridIndex;
                mCallback.userClicked(index, mCurrentPlayer);
            }

            // Unlock sector
            currentlyUnlocked = gridIndex;
            if (currentlyUnlocked != currentlyZoomed) {
                unFocusAll();
            }

            // Toggle current player, notify sector to draw
            if (mCurrentPlayer == Constants.Player1Token) {
                mCurrentPlayer = Constants.Player2Token;
                return Constants.Player1Token;
            } else {
                mCurrentPlayer = Constants.Player1Token;
                return Constants.Player2Token;
            }
        }
    }

    // Down stream only
    public boolean placeMoves(int[] localWinners, int[] moves) {
        for (int i = 0; i < localWinners.length; i++) {
            mLocalWinners[i] = localWinners[i];
            mSectorList.get(i).setLocalWinner(mLocalWinners[i]);
        }

        for (int j = 0; j < moves.length; j++) {
            int boardIndex = j / Constants.GridCount;
            int gridIndex = j % Constants.GridCount;
            int player = moves[j];
            gridOwnership[gridIndex][boardIndex] = player;
            if (!placeMove(boardIndex, gridIndex, player))
                return false;
        }
        return true;
    }

    // Down stream only
    public boolean placeMove(int boardIndex, int gridIndex, int player) {
        return mSectorList.get(boardIndex).placeMove(gridIndex, player);
    }

    interface Callback {
        void userClicked(int index, int player);
    }

    public static int checkWin(int[] array) {
        // Diagonals
        if (array[4] != Constants.NotChosen) {
            if (checkSame(array[0], array[4], array[8])
                    || checkSame(array[2], array[4], array[6]))
                return array[4];
        }

        // Rows
        for (int i = 0; i < 9; i+=3) {
            if (array[i] != Constants.NotChosen && checkSame(array[i], array[i + 1], array[i + 2])) {
                return array[i];
            }
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            if (array[j] != Constants.NotChosen && checkSame(array[j], array[j + 3], array[j + 6])) {
                return array[j];
            }
        }

        return Constants.NotChosen;
    }

    public static boolean checkSame(int a, int b, int c) {
        return a==b && b==c;
    }
}
