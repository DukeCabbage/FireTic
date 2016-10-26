package com.cabbage.firetic.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cabbage.firetic.data.DataManager;
import com.cabbage.firetic.data.UserAccountManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Named("application")
    @Singleton
    Context providesApplicationContext(Application app) {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application app) {
        return app.getSharedPreferences("FireTicSharedPreferences", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    DataManager providesDataManager() {
        return new DataManager();
    }

    @Provides
    @Singleton
    UserAccountManager providesUserAccountManager(FirebaseAuth firebaseAuth, FirebaseAnalytics firebaseAnalytics) {
        return new UserAccountManager(firebaseAuth, firebaseAnalytics);
    }
}
