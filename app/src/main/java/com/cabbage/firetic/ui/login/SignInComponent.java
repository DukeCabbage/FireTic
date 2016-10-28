package com.cabbage.firetic.ui.login;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.ui.uiUtils.ContractScoped;

import dagger.Component;

@ContractScoped
@Component(dependencies = AppComponent.class, modules = SignInModule.class)
public interface SignInComponent {

    void inject(LoginActivity activity);
}
