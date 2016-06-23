package com.cabbage.firetic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cabbage.firetic.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        final DatabaseReference usersRef = ((MyApplication)getApplication()).usersRef();
        if (usersRef != null) {
            Query existingUserQuery = usersRef.orderByChild("userName").equalTo(inputUserName);
            existingUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Timber.d("child exists: " + (dataSnapshot.exists()));
                    if (!dataSnapshot.exists()) {
                        usersRef.push().setValue(new User(inputUserName));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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
