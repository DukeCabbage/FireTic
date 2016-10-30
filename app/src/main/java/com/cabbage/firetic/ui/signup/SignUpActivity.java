package com.cabbage.firetic.ui.signup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.ui.uiUtils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
            getSupportActionBar().setTitle(R.string.app_name);
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
}
