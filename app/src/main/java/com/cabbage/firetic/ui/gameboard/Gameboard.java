package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.Constants;

public class Gameboard extends RelativeLayout
        implements View.OnClickListener {

    private int currentlyZoomed = Constants.NotChosen;
    private int currentlyUnlocked = Constants.NotChosen;
    private int mCurrentPlayer = Constants.Player1Token;
    private int mOwnerShip[] = new int[Constants.BoardCount * Constants.GridCount];

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

    private void attachSector(GameboardSector sector, Order.VerticalOrder vo, Order.HorizontalOrder ho) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sector.getLayoutParams();
        setAlignments(lp, vo, ho);
        int boardIndex = Order.HorizontalOrder.values().length * vo.ordinal() + ho.ordinal();
        sector.setGameboard(this, boardIndex);
        this.addView(sector, boardIndex);   // No other child views other than GameboardSectors
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
        int index = boardIndex * Constants.GridCount + gridIndex;
        if (currentlyUnlocked != Constants.NotChosen && currentlyUnlocked != boardIndex) {
            // This board is locked
            return Constants.Invalid;
        } else if (mOwnerShip[index] != Constants.OpenGrid) {
            // Already set
            return Constants.Invalid;
        } else {
            mOwnerShip[index] = mCurrentPlayer;
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
    public boolean placeMoves(int[] moves) {
        for (int index = 0; index < moves.length; index++) {
            int boardIndex = index / Constants.GridCount;
            int gridIndex = index % Constants.GridCount;
            int player = moves[index];
            if (!placeMove(boardIndex, gridIndex, player))
                return false;
        }
        return true;
    }

    // Down stream only
    public boolean placeMove(int boardIndex, int gridIndex, int player) {
        GameboardSector sector = (GameboardSector) getChildAt(boardIndex);
        return sector != null && sector.placeMove(gridIndex, player);
    }
}
