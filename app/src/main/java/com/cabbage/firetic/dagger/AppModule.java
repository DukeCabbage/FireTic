package com.cabbage.firetic.dagger;

import android.app.Application;
import android.content.Context;

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
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Named("application")
    @Singleton
    Context providesApplicationContext(Application app) {
        return app.getApplicationContext();
    }
}
