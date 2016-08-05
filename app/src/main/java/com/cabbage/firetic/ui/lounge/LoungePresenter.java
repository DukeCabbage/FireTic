package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;

import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoungePresenter {

    private LoungeMVPView view;
    private DataManager mDataManager;
    private Subscription signInSubscription;

    void onTakeView(@NonNull LoungeMVPView view) {
        this.view = view;
        mDataManager = MyApplication.component().getDataManager();
    }

    void onStopView() {
        view = null;
        if (signInSubscription != null && !signInSubscription.isUnsubscribed()) {
            signInSubscription.unsubscribe();
        }
    }

    void mockLogin() {
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        User mock = new User("kkk", "Mock");

        signInSubscription = Observable.just(mock).delay(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        mDataManager.saveActiveUser(user);
                        return user;
                    }
                }).subscribe(signInSuccessPipe(), signInFailPipe());
    }

    void login(String inputUserName) {
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        signInSubscription = mDataManager
                .signInAs(inputUserName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signInSuccessPipe(), signInFailPipe());
    }

    void revokeLogin() {
        mDataManager.revokeActiveUser();
        view.logout();
    }

    private Action1<User> signInSuccessPipe() {
        return new Action1<User>() {
            @Override
            public void call(User user) {
                Timber.d(user.getUserName());
                view.loginSuccess(user);
            }
        };
    }

    private Action1<Throwable> signInFailPipe() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable.getLocalizedMessage());
                view.loginFailed(throwable.getLocalizedMessage());
            }
        };
    }
}
