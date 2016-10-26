package com.cabbage.firetic.dagger;

import android.content.Context;

import com.cabbage.firetic.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;

    @Provides
    @Singleton
    FirebaseDatabase providesDatabase() {
        if (firebaseDatabase == null) firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase;
    }

    @Provides
    @Singleton
    FirebaseRemoteConfig providesRemoteConfig() {
        if (firebaseRemoteConfig == null) {
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            firebaseRemoteConfig.setConfigSettings(configSettings);
        }
        return firebaseRemoteConfig;
    }

    @Provides
    @Singleton
    FirebaseAnalytics providesAnalytics(@Named("application") Context context) {
        if (analytics == null) analytics = FirebaseAnalytics.getInstance(context);
        return analytics;
    }

    @Provides
    @Singleton
    FirebaseAuth providesAuth() {
        if (auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }

    @Provides
    @Named("user")
    @Singleton
    DatabaseReference providesUserRef(FirebaseDatabase db) {
        return db.getReference("users");
    }

    @Provides
    @Named("game")
    @Singleton
    DatabaseReference providesGameRef(FirebaseDatabase db) {
        return db.getReference("games");
    }
}
