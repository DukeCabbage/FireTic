package com.cabbage.firetic.ui.login;

import android.support.annotation.NonNull;

import com.cabbage.firetic.data.UserAccountManager;
import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.utility.RxUtils;
import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

public class LoginPresenter extends LoginContract.Presenter {

    private final UserAccountManager mUserAccountManager;

    @Inject
    LoginPresenter(UserAccountManager userAccountManager, @NonNull LoginContract.View view) {
        super(view);
        this.mUserAccountManager = userAccountManager;
        getMvpView().setPresenter(this);
    }

    @Override
    public void loginEmailAndPassword(@NonNull final String email, @NonNull final String password) {
        if (mUserAccountManager.getFirebaseUser() != null) {
            getMvpView().loginFail("Already signed in");
            return;
        }

        Subscription sub = mUserAccountManager.signInEmailAndPassword(email, password)
                .compose(RxUtils.applySchedulers())
                .subscribe(signInSubscriber());
        addNewSubscription(sub);
    }

    @Override
    void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Subscription sub = mUserAccountManager.signInWithCredential(credential)
                .compose(RxUtils.applySchedulers())
                .subscribe(signInSubscriber());
        addNewSubscription(sub);

    }

    @Override
    void firebaseAuthWithFacebook(LoginResult loginResult) {
        AccessToken token = loginResult.getAccessToken();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Subscription sub = mUserAccountManager.signInWithCredential(credential)
                .compose(RxUtils.applySchedulers())
                .subscribe(signInSubscriber());
        addNewSubscription(sub);
    }

    private Subscriber<FirebaseUser> signInSubscriber() {
        return new Subscriber<FirebaseUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getMvpView().loginFail(e.getLocalizedMessage());
            }

            @Override
            public void onNext(FirebaseUser firebaseUser) {
                if (firebaseUser == null) throw new RuntimeException();
            }
        };
    }


    @Override
    public void start() {
        Timber.v("start");
        addNewSubscription(Observable.merge(Observable.just(mUserAccountManager.getFirebaseUser()), mUserAccountManager.getAuthStateChangePublisher())
                .subscribe(firebaseUser -> {
                    if (firebaseUser != null) {
                        getMvpView().loginSuccess(firebaseUser);
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
}
