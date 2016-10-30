package com.cabbage.firetic.ui.signup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.ui.uiUtils.BaseView;
import com.cabbage.firetic.utility.BasePresenter;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {
        void signUpSuccess(@NonNull Player player);

        void signUpFail(@Nullable String errMsg);
    }

    abstract class Presenter extends BasePresenter<View> {
        Presenter(@NonNull View mvpView) {
            super(mvpView);
        }

        abstract void signUpEmailAndPassWord(@NonNull String email, @NonNull String password);
    }
}
