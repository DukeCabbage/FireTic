package com.cabbage.firetic.utility;

import android.os.Looper;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class RxUtils {
    public static void unSubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }

    public static void printIsMainThread(@Nullable String tag) {
        boolean isMainThread = false;
        Looper looper = Looper.myLooper();
        isMainThread = looper != null && looper == Looper.myLooper();
        if (tag == null) {
            Timber.w("Is main thread: %b", isMainThread);
        } else {
            Timber.w("%s, is main thread: %b", tag, isMainThread);
        }
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
