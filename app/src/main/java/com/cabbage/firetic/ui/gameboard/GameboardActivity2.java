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
import butterknife.Optional;

public class GameboardActivity2 extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @Nullable @BindView(R.id.view_zoom_test) View testView;
    @Nullable @BindView(R.id.gameboard_ard) View gameboardCard;
    //endregion

    @SuppressWarnings("unused")
    @Optional @OnClick(R.id.view_zoom_test)
    void testZoom(final View view) {
        float currentScale = view.getScaleX();

        final ViewPropertyAnimator animator = view.animate();
        if (currentScale < 3.0) {
            animator.scaleX(currentScale + 1).scaleY(currentScale + 1);
        } else {
            animator.scaleX(1).scaleY(1);
        }

        animator.setDuration(333L)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new MyAnimatorListener(new WeakReference<>(testView)))
                .start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard_2);
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
                zoomBoard(true);
                ((GameboardSector) gameboardCard.findViewById(R.id.gameboard_sector)).enableGridClick(true);
                return true;
            case R.id.zoom_out:
                zoomBoard(false);
                ((GameboardSector) gameboardCard.findViewById(R.id.gameboard_sector)).enableGridClick(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void zoomBoard(boolean isEnlarging) {
        if (gameboardCard == null)
            return;

        float currentScale = gameboardCard.getScaleX();
        final ViewPropertyAnimator animator = gameboardCard.animate();
        int width = gameboardCard.getWidth();
        int height = gameboardCard.getHeight();
        gameboardCard.setPivotX(width/2);
        gameboardCard.setPivotY(height/2);
        if (currentScale == 1 && isEnlarging) {
            float scaleTo = 2;
            animator.scaleX(scaleTo).scaleY(scaleTo);
        } else if (!isEnlarging) {
            animator.scaleX(1).scaleY(1);
        } else {
            return;
        }

        animator.setDuration(333L)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new MyAnimatorListener(new WeakReference<>(testView)))
                .start();
    }
}
