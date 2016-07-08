package com.cabbage.firetic;

import android.app.Application;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.dagger.AppModule;
import com.cabbage.firetic.dagger.DaggerAppComponent;
import com.cabbage.firetic.dagger.DaggerDataComponent;
import com.cabbage.firetic.dagger.DataComponent;
import com.cabbage.firetic.dagger.FirebaseModule;
import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static AppComponent mComponent;
    private static DataComponent mDataComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        initAppComponent();
        initDataComponent();
    }

    private void initAppComponent() {
        Timber.i("Initializing app component...");
        try {
            AppModule appModule = new AppModule(mInstance);
            mComponent = DaggerAppComponent.builder()
                    .appModule(appModule)
                    .build();
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
            e.printStackTrace();
            mComponent = null;
        }
    }

    private void initDataComponent() {
        Timber.i("Initializing data component...");
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseModule firebaseModule = new FirebaseModule(database);
            mDataComponent = DaggerDataComponent.builder()
                    .firebaseModule(firebaseModule)
                    .build();
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
            e.printStackTrace();
            mDataComponent = null;
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static AppComponent component() {
        return mComponent;
    }

    public static DataComponent dataComponent() {
        return mDataComponent;
    }
}
