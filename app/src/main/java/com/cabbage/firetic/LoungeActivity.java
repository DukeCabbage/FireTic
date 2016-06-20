package com.cabbage.firetic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoungeActivity extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_game_id) EditText etGameId;
    @BindView(R.id.btn_connect) Button btnConnect;
    //endregion

    //region Actions
    @SuppressWarnings("unused")
    @OnClick(R.id.btn_connect)
    void connectOnClick(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReferenceFromUrl("https://project-nephele.firebaseio.com/");

        myRef.setValue("Hello, World!");
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lounge);
        ButterKnife.bind(this);
        setUpAppBar();
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }
}
