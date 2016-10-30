package com.cabbage.firetic.ui.signup;

import android.support.annotation.NonNull;

import com.cabbage.firetic.data.UserAccountManager;
import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.utility.RxUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class SignUpPresenter extends SignUpContract.Presenter {

    private UserAccountManager mUserAccountManager;

    @Inject
    SignUpPresenter(UserAccountManager userAccountManager, @NonNull SignUpContract.View view) {
        super(view);
        this.mUserAccountManager = userAccountManager;
        getMvpView().setPresenter(this);
    }

    @Override
    public void start() {
        Timber.v("start");
        addNewSubscription(Observable.merge(Observable.just(mUserAccountManager.getFirebaseUser()), mUserAccountManager.getAuthStateChangePublisher())
                .subscribe(firebaseUser -> {
                    if (firebaseUser != null) {
                        Player player = new Player("123", firebaseUser.getDisplayName());
                        getMvpView().signUpSuccess(player);
                    } else {
                        Timber.i("No current user");
                    }
                }, throwable -> {
                    Timber.e(throwable.getMessage());
                    throwable.printStackTrace();
                }));
    }

    @Override
    public void stop() {
        Timber.v("stop");
        unSubscribeAll();
    }

    @Override
    void signUpEmailAndPassWord(@NonNull String email, @NonNull String password) {
        if (mUserAccountManager.getFirebaseUser() != null) {
            getMvpView().signUpFail("Already signed in");
            return;
        }

        Subscription sub = mUserAccountManager.signUpEmailAndPassword(email, password)
                .compose(RxUtils.applySchedulers())
                .subscribe(firebaseUser -> {
                            if (firebaseUser == null) throw new RuntimeException();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            getMvpView().signUpFail(throwable.getLocalizedMessage());
                        });

        addNewSubscription(sub);
    }
}
