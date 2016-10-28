package com.cabbage.firetic.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.data.UserAccountManager;
import com.cabbage.firetic.ui.gameboard.GameboardActivity;
import com.cabbage.firetic.ui.uiUtils.ActivityUtils;
import com.cabbage.firetic.ui.uiUtils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    SignInFragment signInFragment;

    @Inject SignInPresenter presenter;
    UserAccountManager userAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_lounge);

        ButterKnife.bind(this);
        setUpAppBar();

        userAccountManager = MyApplication.component().getUserAccountManager();
        if (userAccountManager.isLoggedIn()) {
            moveToGameBoard();
        } else {
            showSignInPage();
            SignInComponent signInComponent = DaggerSignInComponent.builder()
                    .appComponent(MyApplication.component())
                    .signInModule(new SignInModule(signInFragment))
                    .build();

            signInComponent.inject(this);
            Timber.e("Presenter null: %b", (presenter == null));
        }
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    private void showSignInPage() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof SignInFragment) {
            signInFragment = (SignInFragment) fragment;
        } else {
            signInFragment = SignInFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), signInFragment, R.id.content);
        }
    }

    public void signInSuccess() {
        FirebaseUser user = userAccountManager.getFirebaseUser();
        boolean loggedIn = userAccountManager.getFirebaseUser() != null;

        if (!loggedIn) return;

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        Snackbar.make(viewGroup, String.format("Welcome, %s", user.getDisplayName()), Snackbar.LENGTH_LONG).show();
        Observable.empty().delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(
                        new Action1<Object>() {
                            @Override
                            public void call(Object o) {

                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                moveToGameBoard();
                            }
                        });
    }

    private void moveToGameBoard() {
        Intent intent = new Intent(this, GameboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.i("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }
}
