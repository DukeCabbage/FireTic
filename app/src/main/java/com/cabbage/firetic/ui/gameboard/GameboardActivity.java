package com.cabbage.firetic.ui.gameboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.MyAnimatorListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameboardActivity extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gameboard_sector) GameboardSector sector;
    //endregion

    @SuppressWarnings("unused")
    @OnClick(R.id.gameboard_sector)
    void boardOnClick(View view) {
//        zoomBoard((GameboardSector) view.getParent(), true);
        zoomBoard(view, true);
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.dismiss_area_left, R.id.dismiss_area_right, R.id.dismiss_area_top, R.id.dismiss_area_bottom})
    void dismiss() {
        zoomBoard(sector, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard);
        ButterKnife.bind(this);
        setUpAppBar();
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gameboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.zoom_in:
                zoomBoard(sector, true);
                return true;
            case R.id.zoom_out:
                zoomBoard(sector, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void zoomBoard(View boardView, boolean isEnlarging) {
        if (boardView == null)
            return;

        float currentScale = boardView.getScaleX();
        final ViewPropertyAnimator animator = boardView.animate();
        int width = boardView.getWidth();
        int height = boardView.getHeight();
        boardView.setPivotX(width/2);
        boardView.setPivotY(height/2);
        if (currentScale == 1 && isEnlarging) {
            float scaleTo = 2;
            animator.scaleX(scaleTo).scaleY(scaleTo);

        } else if (!isEnlarging) {
            animator.scaleX(1).scaleY(1);

        } else {
            return;
        }
        ((GameboardSector)boardView).enableGridClick(isEnlarging);

        animator.setDuration(333L)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new MyAnimatorListener(new WeakReference<>(boardView)))
                .start();
    }
}
