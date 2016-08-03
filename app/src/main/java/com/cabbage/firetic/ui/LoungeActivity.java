package com.cabbage.firetic.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

public class LoungeActivity extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.login_layout) ViewGroup signInLayout;
    @BindView(R.id.et_username) EditText etUsername;
//    @BindView(R.id.btn_connect) Button btnConnect;

    @BindView(R.id.refresh_layout) SwipeRefreshLayout gameListLayout;
//    @BindView(R.id.btn_create_game) Button btnCreateGame;
    //endregion

    @BindColor(R.color.primary) int colorPrimary;

    DataManager mDataManager;
    Subscription signInSubscription;

    //region Actions
    @SuppressWarnings("unused")
    @OnClick(R.id.btn_connect)
    void connectOnClick(View view) {

        final String inputUserName = etUsername.getText().toString();
        if (TextUtils.isEmpty(inputUserName)) {
            return;
        }

        DialogHelper.showProgressDialog(this, "Logging in");
        Observable<User> ob = mDataManager.signInAs(inputUserName);
        signInSubscription = ob.subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                Timber.d(user.getUserName());
                DialogHelper.dismissProgressDialog();
                checkIfSignedIn();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                DialogHelper.dismissProgressDialog();
                Toast.makeText(LoungeActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_create_game)
    void createNewGameOnClick() {
        Toast.makeText(this, "Make new game", Toast.LENGTH_SHORT).show();
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lounge);
        ButterKnife.bind(this);
        mDataManager = MyApplication.component().getDataManager();
        setUpAppBar();

        gameListLayout.setColorSchemeColors(colorPrimary);

        gameListLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(LoungeActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                gameListLayout.setRefreshing(true);
                gameListLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameListLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    private void checkIfSignedIn() {
        User activeUser = mDataManager.getActiveUser();
        if (activeUser != null) {
            signInLayout.setVisibility(View.GONE);
            gameListLayout.setVisibility(View.VISIBLE);
        } else {
            signInLayout.setVisibility(View.VISIBLE);
            gameListLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lounge_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.revoke_user:
                mDataManager.revokeActiveUser();
                checkIfSignedIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkIfSignedIn();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        DialogHelper.dismissProgressDialog();
    }
}
