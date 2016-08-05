package com.cabbage.firetic.dagger;

import com.cabbage.firetic.ui.lounge.LoungeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoungeActivity activity);
}
