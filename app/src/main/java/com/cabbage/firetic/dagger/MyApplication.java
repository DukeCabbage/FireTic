package com.cabbage.firetic.dagger;

import android.app.Application;

import com.cabbage.firetic.BuildConfig;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static AppComponent mComponent;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static AppComponent component() {
        return mComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        initAppComponent();
    }

    private void initAppComponent() {
        Timber.i("Initializing app component...");
        if (mComponent == null) {
            mComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
    }
}
