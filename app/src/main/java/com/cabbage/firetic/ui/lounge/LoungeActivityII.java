package com.cabbage.firetic.ui.lounge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.ui.lounge.signIn.DaggerSignInComponent;
import com.cabbage.firetic.ui.lounge.signIn.SignInComponent;
import com.cabbage.firetic.ui.lounge.signIn.SignInFragment;
import com.cabbage.firetic.ui.lounge.signIn.SignInModule;
import com.cabbage.firetic.ui.lounge.signIn.SignInPresenter;
import com.cabbage.firetic.ui.uiUtils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LoungeActivityII extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    boolean loggedIn;
    SignInFragment signInFragment;
    LoungeFragment loungeFragment;

    @Inject
    SignInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lounge);

        ButterKnife.bind(this);
        setUpAppBar();

        DataManager mDataManager = MyApplication.component().getDataManager();
//        loggedIn = mDataManager.getActiveUser() != null;
        loggedIn = false;

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (loggedIn) {
            if (fragment != null && fragment instanceof LoungeFragment) {
                loungeFragment = (LoungeFragment)fragment;
            } else {
                loungeFragment = LoungeFragment.newInstance();
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loungeFragment, R.id.content);
            }
        } else {
            if (fragment != null && fragment instanceof SignInFragment) {
                signInFragment = (SignInFragment)fragment;
            } else {
                signInFragment = SignInFragment.newInstance();
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), signInFragment, R.id.content);
            }

            SignInComponent signInComponent = DaggerSignInComponent.builder()
                    .appComponent(MyApplication.component())
                    .signInModule(new SignInModule(signInFragment))
                    .build();

            signInComponent.inject(this);
            Timber.e("Presenter null: " + (presenter == null));
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
}
