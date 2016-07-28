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

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.MyAnimatorListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GameboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gameboard) Gameboard mGameboard;

    @BindView(R.id.barContainer) View barContainer;
//    @BindView(R.id.bar1) View bar1;
//    @BindView(R.id.bar2) View bar2;

    private float playerBarShift = 180;

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
                barContainer.animate().translationX(playerBarShift).setDuration(333L).setListener(new MyAnimatorListener(new WeakReference<>(barContainer))).start();
                return true;
            case R.id.increase_red:
                barContainer.animate().translationX(-playerBarShift).setDuration(333L).setListener(new MyAnimatorListener(new WeakReference<>(barContainer))).start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
