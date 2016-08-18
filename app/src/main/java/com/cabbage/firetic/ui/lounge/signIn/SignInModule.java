package com.cabbage.firetic.ui.lounge.signIn;

import dagger.Module;
import dagger.Provides;

@Module
public class SignInModule {

    private final SignInContract.View mView;

    public SignInModule(SignInContract.View view) {
        mView = view;
    }

    @Provides
    SignInContract.View provideSignInContractView() {
        return mView;
    }
}
