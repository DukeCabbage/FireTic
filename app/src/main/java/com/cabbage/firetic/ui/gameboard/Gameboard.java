package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.cabbage.firetic.R;

public class Gameboard extends RelativeLayout implements View.OnClickListener {

    private int currentFocus = -1;

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
        sector.setOnClickListener(this);
        this.addView(sector);
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
            if (index == currentFocus) {
                ((GameboardSector) view).focusOnSector(false);
            } else {
                unFocusAll();
                ((GameboardSector) view).focusOnSector(true);
                currentFocus = index;
            }
        }
    }

    public void unFocusAll() {
        if (currentFocus != -1) {
            ((GameboardSector) getChildAt(currentFocus)).focusOnSector(false);
            currentFocus = -1;
        }
    }
}
