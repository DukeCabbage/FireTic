package com.cabbage.firetic.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cabbage.firetic.dagger.ActivityComponent;
import com.cabbage.firetic.dagger.DaggerActivityComponent;

public class BaseActivity extends AppCompatActivity {

    protected ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder().build();
    }
}
