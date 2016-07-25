package com.cabbage.firetic.ui.gameboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cabbage.firetic.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gameboard_sector) GameboardSector sector;

    @SuppressWarnings("unused")
    @OnClick({R.id.dismiss_area_left, R.id.dismiss_area_right, R.id.dismiss_area_top, R.id.dismiss_area_bottom})
    void dismiss() {
        sector.focusOnSector(false);
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
                sector.focusOnSector(true);
                return true;
            case R.id.zoom_out:
                sector.focusOnSector(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
