package com.cabbage.firetic.dagger;

import com.cabbage.firetic.ui.lounge.LoungePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @ActivityScope
    LoungePresenter providesSignInPresenter() {
        return new LoungePresenter();
    }

}
