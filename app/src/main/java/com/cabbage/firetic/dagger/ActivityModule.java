package com.cabbage.firetic.dagger;

import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.ui.lounge.LoungePresenter;
import com.cabbage.firetic.ui.lounge.MockLoungePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @ActivityScope
    LoungePresenter providesSignInPresenter(DataManager dataManager) {
        return new MockLoungePresenter(dataManager);
    }

}
