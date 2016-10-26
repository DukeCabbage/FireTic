package com.cabbage.firetic.ui.lounge.signIn;

import android.support.annotation.NonNull;

import com.cabbage.firetic.data.UserAccountManager;
import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.utility.RxUtils;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import timber.log.Timber;

public class SignInPresenter extends SignInContract.Presenter {

    private final UserAccountManager mUserAccountManager;
    private final SignInContract.View mSignInView;

    @Inject
    SignInPresenter(UserAccountManager userAccountManager, SignInContract.View view) {
        this.mUserAccountManager = userAccountManager;
        this.mSignInView = view;
        mSignInView.setPresenter(this);
    }

    @Override
    public void loginEmailAndPassword(@NonNull final String email, @NonNull final String password) {
        if (mUserAccountManager.getFirebaseUser() != null) {
            mSignInView.signInFail("Already signed in");
        } else {
            signUp(email, password);
        }
    }

    private void signUp(@NonNull final String email, @NonNull final String password) {
        addNewSubscription(mUserAccountManager.signUpEmailAndPassword(email, password)
                .compose(RxUtils.<FirebaseUser>applySchedulers())
                .subscribe(new Subscriber<FirebaseUser>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("sign up on complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            signIn(email, password);
                        } else {
                            e.printStackTrace();
                            String errorMsg = e.getMessage();
                            Timber.e(errorMsg);
                            mSignInView.signInFail(errorMsg);
                        }
                    }

                    @Override
                    public void onNext(FirebaseUser firebaseUser) {
                        Timber.v("sign up on next");
                    }
                }));
    }

    private void signIn(@NonNull final String email, @NonNull final String password) {
        addNewSubscription(mUserAccountManager.signInEmailAndPassword(email, password)
                .compose(RxUtils.<FirebaseUser>applySchedulers())
                .subscribe(new Subscriber<FirebaseUser>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("sign in on complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String errorMsg = e.getMessage();
                        Timber.e(errorMsg);
                        mSignInView.signInFail(errorMsg);
                    }

                    @Override
                    public void onNext(FirebaseUser firebaseUser) {
                        Timber.v("sign in on next");
                    }
                }));
    }

    @Override
    public void start() {
        Timber.d("start");
        addNewSubscription(Observable.merge(Observable.just(mUserAccountManager.getFirebaseUser()), mUserAccountManager.getAuthStateChangePublisher())
                .subscribe(new Action1<FirebaseUser>() {
                    @Override
                    public void call(FirebaseUser firebaseUser) {
                        if (firebaseUser != null) {
                            Player player = new Player("123", firebaseUser.getDisplayName());
                            mSignInView.signInSuccess(player);
                        } else {
                            Timber.i("No current user");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void stop() {
        Timber.d("stop");
        unSubscribeAll();
    }
}
