package com.example.ulakdemo.network;

import com.example.ulakdemo.model.User;


import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("users/{id}")
    Flowable<User> getUser(
            @Path("id") int id
    );



}
