package com.cabbage.firetic.data;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.Player;
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

    Player activeUser;

    public DataManager() {
        AppComponent com = MyApplication.component();
        gameRef = com.getGamesRef();
        userRef = com.getUsersRef();
        sharedPreferences = com.getSharedPreferences();
    }

    public Observable<Player> signInAs(final String inputUserName) {

        Observable.OnSubscribe<Player> onSubscribe = new Observable.OnSubscribe<Player>() {
            @Override
            public void call(final Subscriber<? super Player> subscriber) {
                Query existingUserQuery = userRef.orderByChild("userName").equalTo(inputUserName);

                existingUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Timber.d("Child exists: %b", dataSnapshot.exists());
                        Player user;
                        if (dataSnapshot.exists()) {
                            Timber.d("Player existed:");
                            DataSnapshot snap = dataSnapshot.getChildren().iterator().next();
                            user = snap.getValue(Player.class);
                            user.setUserId(snap.getKey());

                        } else {
                            Timber.d("Creating new user:");
                            DatabaseReference newUserRef = userRef.push();
                            user = new Player(newUserRef.getKey(), inputUserName);
                            newUserRef.setValue(user);
                        }

                        saveActiveUser(user);
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
    public Player getActiveUser() {
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
    public void saveActiveUser(Player user) {
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
            activeUser = gson.fromJson(userStr, Player.class);
        } catch (JsonSyntaxException e) {
            Timber.e(e.getLocalizedMessage());
        }
    }
}
