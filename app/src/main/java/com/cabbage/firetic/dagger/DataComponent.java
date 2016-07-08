package com.cabbage.firetic.dagger;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {FirebaseModule.class})
public interface DataComponent {

    FirebaseDatabase fireDB();
    @Named("user") DatabaseReference getUsersRef();
    @Named("game") DatabaseReference getGamesRef();
}
