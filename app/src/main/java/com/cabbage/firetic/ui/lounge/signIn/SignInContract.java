package com.cabbage.firetic.ui.lounge.signIn;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.ui.uiUtils.BaseView;
import com.cabbage.firetic.utility.BasePresenter;

public interface SignInContract {

    interface View extends BaseView<Presenter> {
        void signInSuccess(@NonNull Player player);

        void signInFail(@Nullable String errMsg);
    }

    abstract class Presenter extends BasePresenter {
        abstract void loginEmailAndPassword(@NonNull String email, @NonNull String password);
    }
}
