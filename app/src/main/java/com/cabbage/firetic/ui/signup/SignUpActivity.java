package com.cabbage.firetic.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    SignUpFragment signUpFragment;

    @Inject SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        setUpAppBar();

        showSignUpPage();
        SignUpComponent signUpComponent = DaggerSignUpComponent.builder()
                .appComponent(MyApplication.component())
                .signUpModule(new SignUpModule(signUpFragment))
                .build();

        signUpComponent.inject(this);
        Timber.e("Presenter null: %b", (presenter == null));
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.create_account);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    private void showSignUpPage() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof SignUpFragment) {
            signUpFragment = (SignUpFragment) fragment;
        } else {
            signUpFragment = SignUpFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), signUpFragment, R.id.content);
        }
    }

    public void signUpSuccess() {
        UserAccountManager userAccountManager = MyApplication.component().getUserAccountManager();
        FirebaseUser user = userAccountManager.getFirebaseUser();
        if (user == null) return;

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
}
