package com.cabbage.firetic.ui.signup;

import com.cabbage.firetic.dagger.AppComponent;
import com.cabbage.firetic.ui.uiUtils.ActivityScoped;

import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class, modules = SignUpModule.class)
public interface SignUpComponent {

    void inject(SignUpActivity activity);
}
