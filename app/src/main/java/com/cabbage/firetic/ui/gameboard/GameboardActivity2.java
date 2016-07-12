package com.cabbage.firetic.ui.gameboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cabbage.firetic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameboardActivity2 extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //endregion

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
}
