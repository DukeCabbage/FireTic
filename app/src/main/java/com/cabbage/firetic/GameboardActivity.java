package com.cabbage.firetic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameboardActivity extends AppCompatActivity {

    @BindColor(R.color.accent) int colorAccent;

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gameboard) CardView mGameboard;
    //endregion

    //region Actions
    @SuppressWarnings("unused")
    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6,
            R.id.btn_7, R.id.btn_8, R.id.btn_9,})
    void unitOnClick(View view) {
        Toast.makeText(this, view.getTag().toString(), Toast.LENGTH_SHORT).show();
    }
    //endregion

    AlertDialog alertDialog = null;

    //region Setup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard);
        ButterKnife.bind(this);
        setUpAppBar();

        Drawable cross = ContextCompat.getDrawable(this, R.drawable.ic_close_80dp);
        DrawableCompat.setTint(cross, colorAccent);
        Drawable circle = ContextCompat.getDrawable(this, R.drawable.ic_circle_80dp);
        DrawableCompat.setTint(circle, colorAccent);

        showMessageFromIntent();
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

    private void showMessageFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("message");
            if (!TextUtils.isEmpty(message)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("FireTic")
                        .setMessage(message)
                        .setPositiveButton("Pos", null)
                        .setNeutralButton("Neu", null)
                        .setNegativeButton("Neg", null)
                        .setCancelable(true);
                alertDialog = builder.show();
            }
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    //endregion
}
