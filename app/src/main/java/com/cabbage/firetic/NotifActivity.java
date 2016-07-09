package com.cabbage.firetic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class NotifActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_simple) TextView tvSimple;

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    void onClick(View view) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Timber.d( "FCM token: " + token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

//        resetLabel();
    }

    @Override
    public void onResume() {
        super.onResume();
        String message = getMessageFromIntent();
        if (message == null) {
            resetLabel();
        } else {
            tvSimple.setText(getMessageFromIntent());
        }
    }

    private String getMessageFromIntent() {
        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            Bundle extra = startingIntent.getExtras();
            if (extra != null) {
                return extra.getString("message");
            }
        }
        return null;
    }

    private void resetLabel() {
        tvSimple.setText("Nothing yet");
    }

    @Override
    public void onPause() {
        super.onPause();
        resetLabel();
    }
}
