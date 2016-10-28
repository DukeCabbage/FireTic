package com.cabbage.firetic.ui.login;

import dagger.Module;
import dagger.Provides;

@Module
public class SignInModule {

    private final SignInContract.View mView;

    SignInModule(SignInContract.View view) {
        mView = view;
    }

    @Provides
    SignInContract.View provideSignInContractView() {
        return mView;
    }
}
