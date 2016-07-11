package com.cabbage.firetic.dagger;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        FirebaseModule.class,
        NetworkModule.class
})
public interface AppComponent {

    @Named("application") Context appContext();

    FirebaseDatabase fireDB();
    @Named("user") DatabaseReference getUsersRef();
    @Named("game") DatabaseReference getGamesRef();


}
