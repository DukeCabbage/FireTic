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
        for (VerticalOrder vo : VerticalOrder.values()) {
            for (HorizontalOrder ho : HorizontalOrder.values()) {
                GameboardSector tempSector = (GameboardSector) inflater.inflate(R.layout.view_gameboard_sector, this, false);
                // 0,1,2
                // 3,4,5
                // 6,7,8
                tempSector.setTag(count++);
                attachSector(tempSector, vo, ho);
            }
        }
    }

    static int orderToIndex(VerticalOrder vo, HorizontalOrder ho) {
        int vi = vo.ordinal();
        int hi = ho.ordinal();
        return vi * HorizontalOrder.values().length + hi;
    }

    static VerticalOrder indexToVerticalOrder(int index) {
        int columnCount = HorizontalOrder.values().length;
        return VerticalOrder.values()[index / columnCount];
    }

    static HorizontalOrder indexToHorizontalOrder(int index) {
        int columnCount = HorizontalOrder.values().length;
        return HorizontalOrder.values()[index % columnCount];
    }

    private void attachSector(GameboardSector sector, VerticalOrder vo, HorizontalOrder ho) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sector.getLayoutParams();
        setAlignments(lp, vo, ho);
        sector.setOnClickListener(this);
        this.addView(sector);
    }

    private void setAlignments(RelativeLayout.LayoutParams lp, VerticalOrder vo, HorizontalOrder ho) {
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
                if (currentFocus != -1) {
                    ((GameboardSector) getChildAt(currentFocus)).focusOnSector(false);
                }
                ((GameboardSector) view).focusOnSector(true);
                currentFocus = index;
            }
        }
    }

    enum VerticalOrder {
        TOP, CENTER, BOTTOM
    }

    enum HorizontalOrder {
        LEFT, CENTER, RIGHT
    }
}
