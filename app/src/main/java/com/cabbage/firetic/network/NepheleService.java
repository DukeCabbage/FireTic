package com.cabbage.firetic.network;

import com.cabbage.firetic.model.User;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Leo on 2016-07-10.
 */
public interface NepheleService {

    @GET("users")
    Observable<List<User>> getUsers();
}
