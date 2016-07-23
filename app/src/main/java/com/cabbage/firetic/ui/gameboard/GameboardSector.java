package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;

import com.cabbage.firetic.R;

public class GameboardSector extends TableLayout {
    public GameboardSector(Context context) {
        super(context);
    }

    public GameboardSector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_gameboard_sector, this);
    }
}
