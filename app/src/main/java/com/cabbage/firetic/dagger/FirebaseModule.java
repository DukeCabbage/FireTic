package com.cabbage.firetic.dagger;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    private FirebaseDatabase firebaseDatabase;

    public FirebaseModule(FirebaseDatabase db) {
        firebaseDatabase = db;
    }

    @Provides
    @Singleton
    FirebaseDatabase providesDatabase() {
        return firebaseDatabase;
    }

    @Provides @Named("user")
    @Singleton
    DatabaseReference providesUserRef(FirebaseDatabase db) {
        return db.getReference("users");
    }

    @Provides @Named("game")
    @Singleton
    DatabaseReference providesGameRef(FirebaseDatabase db) {
        return db.getReference("games");
    }
}
