package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;

import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MockLoungePresenter extends LoungePresenter {

    public MockLoungePresenter(@NonNull DataManager dataManager) {
        super(dataManager);
    }

    @Override
    void login(String inputUserName) {
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

    /**-------------------------------------------------------------------------------------*/

    @Override
    void getGames() {

    }

    @Override
    void createGame() {

    }

    @Override
    void deleteGame(String gameId, int position) {

    }
}
