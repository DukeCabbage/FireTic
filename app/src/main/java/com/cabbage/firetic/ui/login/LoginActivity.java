package com.cabbage.firetic.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.data.UserAccountManager;
import com.cabbage.firetic.ui.gameboard.GameboardActivity;
import com.cabbage.firetic.ui.uiUtils.ActivityUtils;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    LoginFragment loginFragment;

    @Inject LoginPresenter presenter;
    UserAccountManager userAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        setUpAppBar();

        userAccountManager = MyApplication.component().getUserAccountManager();
        if (userAccountManager.isLoggedIn()) {
            moveToGameBoard();
        } else {
            showSignInPage();
            LoginComponent loginComponent = DaggerLoginComponent.builder()
                    .appComponent(MyApplication.component())
                    .loginModule(new LoginModule(loginFragment))
                    .build();

            loginComponent.inject(this);
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
        if (fragment != null && fragment instanceof LoginFragment) {
            loginFragment = (LoginFragment) fragment;
        } else {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.content);
        }
    }

    public void signInSuccess() {
        FirebaseUser user = userAccountManager.getFirebaseUser();
        boolean loggedIn = userAccountManager.getFirebaseUser() != null;

        if (!loggedIn) return;

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        Snackbar.make(viewGroup, String.format("Welcome, %s", user.getDisplayName()), Snackbar.LENGTH_LONG).show();
        Observable.just(1)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(o -> moveToGameBoard());
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
