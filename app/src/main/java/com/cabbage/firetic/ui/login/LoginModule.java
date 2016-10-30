package com.cabbage.firetic.ui.login;

import com.cabbage.firetic.ui.uiUtils.ActivityScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private final LoginContract.View mView;

    LoginModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    @ActivityScoped
    LoginContract.View provideView() {
        return mView;
    }
}
