package com.cabbage.firetic;

import android.app.Application;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class MyApplication extends Application {

    FirebaseDatabase database;
    MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
        try {
            database = FirebaseDatabase.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            database = null;
        }
    }

    @Nullable
    public FirebaseDatabase firebaseDatabase() {
        return database;
    }

    @Nullable
    public DatabaseReference usersRef() {
        return database == null ? null : database.getReference("users");
    }

    @Nullable
    public DatabaseReference gamesRef() {
        return database == null ? null : database.getReference("games");
    }
}
