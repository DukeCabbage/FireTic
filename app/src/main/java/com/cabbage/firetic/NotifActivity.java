package com.cabbage.firetic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cabbage.firetic.model.User;
import com.cabbage.firetic.network.NepheleService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NotifActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_simple) TextView tvSimple;

    Retrofit fireRetro;

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    void onClick(View view) {
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Timber.d("FCM token: " + token);

        NepheleService service = fireRetro.create(NepheleService.class);
        service.getUsers().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<User> users) {
                        Timber.d("User count: " + users.size());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fireRetro = MyApplication.component().getRetrofit();
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
