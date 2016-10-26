package com.cabbage.firetic.utility;

import android.support.annotation.Nullable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter {

    @Nullable private CompositeSubscription mCompositeSubscription;

    public abstract void start();

    public abstract void stop();

    protected void addNewSubscription(Subscription subscription) {
        mCompositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscribeAll() {
        RxUtils.unSubscribeIfNotNull(mCompositeSubscription);
    }

    protected void unSubscribe(Subscription subscription) {
        if (mCompositeSubscription != null) {
            /** {@link CompositeSubscription#remove(Subscription)} */
            mCompositeSubscription.remove(subscription);
        }
    }
}
