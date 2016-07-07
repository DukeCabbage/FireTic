package com.cabbage.firetic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    }

}
