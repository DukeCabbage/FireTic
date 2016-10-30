package com.cabbage.firetic.ui.login;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.ui.uiUtils.ActivityScoped;

import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class, modules = LoginModule.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
}
