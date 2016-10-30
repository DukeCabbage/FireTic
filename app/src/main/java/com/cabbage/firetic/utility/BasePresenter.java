package com.cabbage.firetic.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.ui.uiUtils.BaseView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BasePresenter<V extends BaseView> {

    @Nullable private CompositeSubscription mCompositeSubscription;
    @NonNull private V mvpView;

    protected BasePresenter(@NonNull V mvpView) {
        this.mvpView = mvpView;
    }

    public abstract void start();

    public abstract void stop();

    protected V getMvpView() {
        checkNotNull(mvpView);
        return mvpView;
    }

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
