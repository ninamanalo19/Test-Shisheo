package com.test.currentTest.network;

import com.test.currentTest.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SimpleAPI {

    @GET("/social/api/web/post/arina/test")
    Call<List<Restaurant>> fetchRestaurants();
    
}
