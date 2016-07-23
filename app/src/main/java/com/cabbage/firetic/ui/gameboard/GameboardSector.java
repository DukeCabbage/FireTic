package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;

import com.cabbage.firetic.R;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import timber.log.Timber;

public class GameboardSector extends TableLayout {

    @BindColor(R.color.primary)
    int colorPrimary;
    @BindViews({R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6,
            R.id.btn_7, R.id.btn_8, R.id.btn_9,})
    List<View> gridList;
    private boolean gridClickEnabled = false;
    private int[] gridOwnership = new int[9];

    public GameboardSector(Context context) {
        super(context);
    }

    public GameboardSector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
    }

    @SuppressWarnings("unused")
    @Optional
    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6,
            R.id.btn_7, R.id.btn_8, R.id.btn_9,})
    void gripOnClick(View view) {
        if (!gridClickEnabled)
            return;
        String viewTag = String.valueOf(view.getTag());
        Timber.w("Child on click: %s", viewTag);

        int index = Integer.valueOf(viewTag) - 1;
        gridList.get(index).setBackgroundColor(colorPrimary);
    }

    void enableGridClick(boolean enable) {
        gridClickEnabled = enable;
    }
}
