package com.cabbage.firetic.model;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.dagger.MyApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
            public void call(final Subscriber<? super User> subscriber) {
                Query existingUserQuery = userRef.orderByChild("userName").equalTo(inputUserName);
                existingUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Timber.d("Child exists: %b", dataSnapshot.exists());
                        User user;
                        if (dataSnapshot.exists()) {
                            Timber.d("User existed:");
                            DataSnapshot snap = dataSnapshot.getChildren().iterator().next();
                            user = snap.getValue(User.class);
                            user.setUserId(snap.getKey());

                        } else {
                            Timber.d("Creating new user:");
                            DatabaseReference newUserRef = userRef.push();
                            user = new User(newUserRef.getKey(), inputUserName);
                            newUserRef.setValue(user);
                        }

                        activeUser = user;
                        cacheActiveUser();
                        subscriber.onNext(user);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getDetails());
                        subscriber.onError(new Exception(databaseError.getMessage()));
                    }
                });
            }
        };

        return Observable.create(onSubscribe);
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

    private void cacheActiveUser() {
        if (!CACHE_ENABLED)
            return;
        Gson gson = new Gson();
        String userStr = gson.toJson(activeUser);
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
