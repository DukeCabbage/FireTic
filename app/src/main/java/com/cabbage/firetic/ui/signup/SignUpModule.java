package com.cabbage.firetic.ui.signup;

import com.cabbage.firetic.ui.uiUtils.ActivityScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    private final SignUpContract.View mView;

    SignUpModule(SignUpContract.View view) {
        mView = view;
    }

    @Provides
    @ActivityScoped
    SignUpContract.View provideView() {
        return mView;
    }
}
