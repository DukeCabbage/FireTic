package com.cabbage.firetic.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.utility.RxUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import timber.log.Timber;

public class UserAccountManager {

    @NonNull private FirebaseAuth mAuth;
    @NonNull private FirebaseAnalytics mAnalytics;
    @NonNull private Subject<FirebaseUser, FirebaseUser> mAuthStateChangePublisher;

    public UserAccountManager(@NonNull FirebaseAuth firebaseAuth, @NonNull FirebaseAnalytics firebaseAnalytics) {
        this.mAuth = firebaseAuth;
        this.mAnalytics = firebaseAnalytics;
        this.mAuthStateChangePublisher = PublishSubject.create();

        mAuth.addAuthStateListener(firebaseAuth1 -> {
            if (firebaseAuth1 != mAuth) throw new RuntimeException();
            mAuthStateChangePublisher.onNext(mAuth.getCurrentUser());
        });
    }

    public Subject<FirebaseUser, FirebaseUser> getAuthStateChangePublisher() {
        return mAuthStateChangePublisher;
    }

    @Nullable
    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return getFirebaseUser() != null;
    }

    public Observable<FirebaseUser> signUpEmailAndPassword(@NonNull final String email,
                                                           @NonNull final String password,
                                                           @NonNull final String userName) {

        return Observable.create(new Observable.OnSubscribe<FirebaseUser>() {
            @Override
            public void call(final Subscriber<? super FirebaseUser> subscriber) {
                RxUtils.printIsMainThread("sign up on subscribe");
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            RxUtils.printIsMainThread("sign up success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser == null) {
                                subscriber.onError(new RuntimeException("No result"));
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebaseUser.getUid());
                                mAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName)
                                        .build())
                                        .addOnSuccessListener(
                                                aVoid -> {
                                                    subscriber.onNext(firebaseUser);
                                                    subscriber.onCompleted();
                                                }
                                        )
                                        .addOnFailureListener(e -> {
                                            throw new RuntimeException("Fail to set name");
                                        });

                            }
                        })
                        .addOnFailureListener(e -> {
                            RxUtils.printIsMainThread("sign up fail");
                            subscriber.onError(e);
                        })
                        .addOnCompleteListener(task -> {
                            RxUtils.printIsMainThread("sign up complete");
//                            subscriber.onCompleted();
                        });
            }
        });
    }

    public Observable<FirebaseUser> signInEmailAndPassword(@NonNull final String email, @NonNull final String password) {
        return Observable.create(new Observable.OnSubscribe<FirebaseUser>() {
            @Override
            public void call(final Subscriber<? super FirebaseUser> subscriber) {
                RxUtils.printIsMainThread("Custom sign in on subscribe");
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            RxUtils.printIsMainThread("sign in success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser == null) {
                                subscriber.onError(new RuntimeException("No result"));
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebaseUser.getUid());
                                mAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                                subscriber.onNext(firebaseUser);
                            }
                        })
                        .addOnFailureListener(e -> {
                            RxUtils.printIsMainThread("sign in fail");
                            subscriber.onError(e);
                        })
                        .addOnCompleteListener(task -> {
                            RxUtils.printIsMainThread("sign in complete");
                            subscriber.onCompleted();
                        });
            }
        });
    }

    public Observable<FirebaseUser> signInWithCredential(@NonNull final AuthCredential credential) {
        return Observable.create(subscriber -> {
            RxUtils.printIsMainThread("Sign in with credential on subscribe");
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        RxUtils.printIsMainThread("sign in success");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser == null) {
                            subscriber.onError(new RuntimeException("No result"));
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebaseUser.getUid());
                            mAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                            subscriber.onNext(firebaseUser);
                        }
                    })
                    .addOnFailureListener(e -> {
                        RxUtils.printIsMainThread("sign in fail");
                        subscriber.onError(e);
                    })
                    .addOnCompleteListener(task -> {
                        RxUtils.printIsMainThread("sign in complete");
                        subscriber.onCompleted();
                    });
        });
    }

    public void signOut() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Timber.e("Trying to logout when no current user");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebaseUser.getUid());
        mAnalytics.logEvent("logout", bundle);

        mAuth.signOut();
    }
}
