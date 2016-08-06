package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;

import com.cabbage.firetic.model.DataManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RealLoungePresenter extends LoungePresenter {

    public RealLoungePresenter(@NonNull DataManager dataManager) {
        super(dataManager);
    }

    @Override
    void login(String inputUserName) {
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        signInSubscription = mDataManager
                .signInAs(inputUserName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signInSuccessPipe(), signInFailPipe());
    }
}
