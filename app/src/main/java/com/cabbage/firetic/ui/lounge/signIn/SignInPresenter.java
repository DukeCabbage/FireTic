package com.cabbage.firetic.ui.lounge.signIn;

import android.support.annotation.NonNull;

import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SignInPresenter implements SignInContract.Presenter {

    private final DataManager mDataManager;
    private final SignInContract.View mSignInView;
    private Subscription signInSubscription;


    @Inject
    public SignInPresenter(DataManager dataManager, SignInContract.View view) {
        this.mDataManager = dataManager;
        this.mSignInView = view;
        mSignInView.setPresenter(this);
    }

    @Override
    public void signInAs(@NonNull String userName) {
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        signInSubscription = mDataManager
                .signInAs(userName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signInSuccessPipe(), signInFailPipe());
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {
        if (signInSubscription != null && !signInSubscription.isUnsubscribed()) {
            signInSubscription.unsubscribe();
        }
    }

    protected Action1<User> signInSuccessPipe() {
        return new Action1<User>() {
            @Override
            public void call(User user) {
                Timber.d(user.getUserName());
                mSignInView.signInSuccess(user);
            }
        };
    }

    protected Action1<Throwable> signInFailPipe() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable.getLocalizedMessage());
                mSignInView.signInFail(throwable.getLocalizedMessage());
            }
        };
    }
}
