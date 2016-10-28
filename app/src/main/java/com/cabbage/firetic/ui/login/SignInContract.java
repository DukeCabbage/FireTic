package com.cabbage.firetic.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.ui.uiUtils.BaseView;
import com.cabbage.firetic.utility.BasePresenter;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignInContract {

    interface View extends BaseView<Presenter> {
        void signInSuccess(@NonNull Player player);

        void signInFail(@Nullable String errMsg);
    }

    abstract class Presenter extends BasePresenter {
        abstract void loginEmailAndPassword(@NonNull String email, @NonNull String password);

        abstract void firebaseAuthWithGoogle(GoogleSignInAccount acct);

        abstract void firebaseAuthWithFacebook(LoginResult loginResult);
    }
}
