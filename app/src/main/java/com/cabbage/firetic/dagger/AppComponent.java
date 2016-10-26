package com.cabbage.firetic.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.cabbage.firetic.data.DataManager;
import com.cabbage.firetic.data.UserAccountManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {
        AppModule.class,
        FirebaseModule.class,
        NetworkModule.class
})
public interface AppComponent {

    /**
     * ----------------------------------------------------------------------------------------
     */

    @Named("application")
    Context appContext();

    SharedPreferences getSharedPreferences();

    DataManager getDataManager();

    UserAccountManager getUserAccountManager();

    /**
     * ----------------------------------------------------------------------------------------
     */

    FirebaseDatabase fireDB();

    FirebaseRemoteConfig fireConfig();

//    FirebaseAnalytics fireAnalytics();

//    FirebaseAuth fireAuth();

    @Named("user")
    DatabaseReference getUsersRef();

    @Named("game")
    DatabaseReference getGamesRef();

    /**
     * ----------------------------------------------------------------------------------------
     */

    Retrofit getRetrofit();
}
