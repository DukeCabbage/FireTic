package com.cabbage.firetic.dagger;

import android.app.Application;

import com.cabbage.firetic.BuildConfig;
import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static AppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        initAppComponent();
    }

    private void initAppComponent() {
        Timber.i("Initializing app component...");
        try {
            AppModule appModule = new AppModule(mInstance);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseModule firebaseModule = new FirebaseModule(database);


            mComponent = DaggerAppComponent.builder()
                    .appModule(appModule)
                    .firebaseModule(firebaseModule)
                    .build();
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
            e.printStackTrace();
            mComponent = null;
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static AppComponent component() {
        return mComponent;
    }
}
