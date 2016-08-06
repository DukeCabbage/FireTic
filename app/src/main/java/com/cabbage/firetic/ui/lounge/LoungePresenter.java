package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;

import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;

import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

public abstract class LoungePresenter {

    protected LoungeMVPView view;
    protected DataManager mDataManager;
    protected Subscription signInSubscription;

    public LoungePresenter(@NonNull DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    void onTakeView(@NonNull LoungeMVPView view) {
        this.view = view;
    }

    void onStopView() {
        view = null;
        if (signInSubscription != null && !signInSubscription.isUnsubscribed()) {
            signInSubscription.unsubscribe();
        }
    }

    abstract void login(String inputUserName);

    void revokeLogin() {
        mDataManager.revokeActiveUser();
        view.logout();
    }

    protected Action1<User> signInSuccessPipe() {
        return new Action1<User>() {
            @Override
            public void call(User user) {
                Timber.d(user.getUserName());
                view.loginSuccess(user);
            }
        };
    }

    protected Action1<Throwable> signInFailPipe() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable.getLocalizedMessage());
                view.loginFailed(throwable.getLocalizedMessage());
            }
        };
    }
}
