package com.cabbage.firetic.ui.lounge.signIn;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.User;
import com.cabbage.firetic.ui.uiUtils.BasePresenter;
import com.cabbage.firetic.ui.uiUtils.BaseView;

public interface SignInContract {

    interface View extends BaseView<Presenter> {
        void signInSuccess(@NonNull User user);

        void signInFail(@Nullable String errMsg);
    }

    interface Presenter extends BasePresenter {
        void signInAs(@NonNull  String userName);
    }
}
