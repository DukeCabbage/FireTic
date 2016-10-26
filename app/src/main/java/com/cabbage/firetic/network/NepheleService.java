package com.cabbage.firetic.network;

import com.cabbage.firetic.model.Player;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface NepheleService {

    @GET("users.json")
    Observable<List<Player>> getUsers();
}
