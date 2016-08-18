package com.cabbage.firetic.model;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.dagger.MyApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class DataManager {

    public static final String SPKEY_ACTIVE_USER = "ActiveUser";
    public static final boolean CACHE_ENABLED = true;

    DatabaseReference userRef;
    DatabaseReference gameRef;
    SharedPreferences sharedPreferences;

    User activeUser;

    public DataManager() {
        AppComponent com = MyApplication.component();
        gameRef = com.getGamesRef();
        userRef = com.getUsersRef();
        sharedPreferences = com.getSharedPreferences();
    }

    public Observable<User> signInAs(final String inputUserName) {

        Observable.OnSubscribe<User> onSubscribe = new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                User mock = new User("kkk", "Mock");
                subscriber.onNext(mock);
                subscriber.onCompleted();
            }
        };

        return Observable.create(onSubscribe).delay(3000, TimeUnit.MILLISECONDS);

    }

    @Nullable
    public User getActiveUser() {
        if (activeUser == null) {
            retrieveCachedUser();
        }

        return activeUser;
    }

    public void revokeActiveUser() {
        activeUser = null;
        sharedPreferences.edit().remove(SPKEY_ACTIVE_USER).apply();
    }

    // TODO: Change access level to private
    public void saveActiveUser(User user) {
        activeUser = user;
        if (!CACHE_ENABLED)
            return;
        Gson gson = new Gson();
        String userStr = gson.toJson(user);
        sharedPreferences.edit().putString(SPKEY_ACTIVE_USER, userStr).apply();
    }

    private void retrieveCachedUser() {
        String userStr = sharedPreferences.getString(SPKEY_ACTIVE_USER, "");
        if (userStr.equals("")) {
            return;
        }
        try {
            Gson gson = new Gson();
            activeUser = gson.fromJson(userStr, User.class);
        } catch (JsonSyntaxException e) {
            Timber.e(e.getLocalizedMessage());
        }
    }
}
