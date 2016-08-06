package com.cabbage.firetic.ui.lounge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cabbage.firetic.model.User;

import java.util.List;

public interface LoungeMVPView {

    void loginSuccess(@NonNull User user);
    void loginFailed(@Nullable String message);
    void logout();

    void updateGameList(List<String> games);
    void updateGame(String gameId);
}
