package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.User;

public interface LoungeMVPView {

    void loginSuccess(@NonNull User user);
    void loginFailed(@Nullable String message);
    void logout();
}
