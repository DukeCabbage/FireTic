package com.cabbage.firetic.ui.gameboard;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.cabbage.firetic.BuildConfig;
import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.Constants;
import com.cabbage.firetic.ui.uiUtils.MyAnimatorListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GameboardActivity extends AppCompatActivity
        implements Gameboard.Callback {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gameboard) Gameboard mGameboard;
    @BindView(R.id.barContainer) View barContainer;

    private float playerBarShift = 180; // Default value, overridden in onStart

    @SuppressWarnings("unused")
    @OnClick({R.id.dismiss_area_left, R.id.dismiss_area_right, R.id.dismiss_area_top, R.id.dismiss_area_bottom})
    void dismiss() {
        mGameboard.unFocusAll();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard);
        ButterKnife.bind(this);
        setUpAppBar();
        mGameboard.setCallback(this);
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
    public void onStart() {
        super.onStart();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Timber.i("Screen size: %d, %d", width, height);
        playerBarShift = 0.3f * size.x;
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
//                sector.focusOnSector(true);
                return true;
            case R.id.zoom_out:
//                sector.focusOnSector(false);
                return true;
            case R.id.increase_blue:
                toggleUserIndicator(1);
                return true;
            case R.id.increase_red:
                toggleUserIndicator(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Player info
     */

    public void toggleUserIndicator(int player) {
        if (BuildConfig.DEBUG && (player < 0 || player > 2))
            throw new AssertionError();

        Timber.d("toggleUserIndicator %d", player);
        ViewPropertyAnimator animator = barContainer.animate();
        switch (player) {
            case 1:
                animator.translationX(playerBarShift).setDuration(333L);
                break;
            case 2:
                animator.translationX(-playerBarShift).setDuration(333L);
                break;
            default:
                animator.translationX(0).setDuration(0);
        }

        animator.setListener(new MyAnimatorListener(new WeakReference<>(barContainer)))
                .start();
    }

    /**
     * Gameboard
    */

    public void refreshBoard(int[] localWinners, int[] moves) {
        if (BuildConfig.DEBUG && moves.length != Constants.BoardCount * Constants.GridCount)
            throw new AssertionError();

        boolean success = mGameboard.placeMoves(localWinners, moves);
        Timber.d("Successfully refreshed whole board");
    }

    public void refreshBoard(int boardIndex, int gridIndex, int player) {
        if (BuildConfig.DEBUG) {
            boolean bool1 = boardIndex < 0 || boardIndex >= Constants.BoardCount;
            boolean bool2 = gridIndex < 0 || gridIndex >= Constants.GridCount;
            if (bool1 || bool2)
                throw new AssertionError();
        }


        boolean success =  mGameboard.placeMove(boardIndex, gridIndex, player);
        if (success)
            Timber.d("Successfully placed a move at (%d, %d), player %d", boardIndex, gridIndex, player);
        else
            Timber.d("Failed to  placed a move at (%d, %d), player %d", boardIndex, gridIndex, player);
    }

    @Override
    public void userClicked(int index, int player) {
        toggleUserIndicator(player);
    }
}
