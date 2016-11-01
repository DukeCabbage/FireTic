package com.cabbage.firetic.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.ui.uiUtils.BaseView;
import com.cabbage.firetic.utility.BasePresenter;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void loginSuccess(@NonNull FirebaseUser firebaseUser);

        void loginFail(@Nullable String errMsg);
    }

    abstract class Presenter extends BasePresenter<View> {
        Presenter(@NonNull View mvpView) {
            super(mvpView);
        }

        abstract void loginEmailAndPassword(@NonNull String email, @NonNull String password);

        abstract void firebaseAuthWithGoogle(GoogleSignInAccount acct);

        abstract void firebaseAuthWithFacebook(LoginResult loginResult);
    }
}
