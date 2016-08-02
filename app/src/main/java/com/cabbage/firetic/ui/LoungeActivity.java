package com.cabbage.firetic.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.R;
import com.cabbage.firetic.model.User;
import com.cabbage.firetic.ui.gameboard.GameboardActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoungeActivity extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_game_id) EditText etGameId;
    @BindView(R.id.btn_connect) Button btnConnect;
    //endregion

    //region Actions
    @SuppressWarnings("unused")
    @OnClick(R.id.btn_connect)
    void connectOnClick(View view) {
        // TODO: Need play services 9.0.x to test, not available on emulators now
//        FirebaseCrash.log("Connect on click");

        final String inputUserName = etUsername.getText().toString();
        final String inputPassword = etPassword.getText().toString();
        if (TextUtils.isEmpty(inputUserName) || TextUtils.isEmpty(inputPassword)) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        final DatabaseReference usersRef = MyApplication.component().getUsersRef();
        if (usersRef != null) {
            Query existingUserQuery = usersRef.orderByChild("userName").equalTo(inputUserName);
            existingUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Timber.d("Child exists: %b", dataSnapshot.exists());
                    if (!dataSnapshot.exists()) {
                        DatabaseReference newUserRef = usersRef.push();
                        User newUser = new User(newUserRef.getKey(), inputUserName, inputPassword);
                        Timber.d("Creating new user:");
                        Timber.d(newUser.toString());
                        newUserRef.setValue(newUser);
                    } else {
                        DataSnapshot snap = dataSnapshot.getChildren().iterator().next();
                        Timber.d(snap.getKey());
                        Timber.d(snap.getValue().toString());

                        User existingUser = snap.getValue(User.class);
                        Timber.d("User existed:");
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.e("Error code: " + databaseError.getCode() + ": " + databaseError.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    void fabOnClick(View v) {
        Intent intent = new Intent(this, GameboardActivity.class);
        startActivity(intent);
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
