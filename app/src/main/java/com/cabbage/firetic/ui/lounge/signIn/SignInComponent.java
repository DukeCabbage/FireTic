package com.cabbage.firetic.ui.lounge.signIn;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.ui.lounge.LoungeActivityII;
import com.cabbage.firetic.ui.uiUtils.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SignInModule.class)
public interface SignInComponent {

    void inject(LoungeActivityII activity);
}
