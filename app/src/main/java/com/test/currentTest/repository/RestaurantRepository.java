package com.test.currentTest.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.test.currentTest.data.RestaurantDao;
import com.test.currentTest.model.Restaurant;
import com.test.currentTest.network.SimpleAPI;

import java.util.List;
import javax.inject.Inject;

import retrofit2.Call;

public class RestaurantRepository {

    private static final String TAG = RestaurantRepository.class.getSimpleName();
    private SimpleAPI simpleAPI;
    private RestaurantDao restaurantDao;

    @Inject
    public RestaurantRepository(SimpleAPI simpleAPI, RestaurantDao restaurantDao) {
        this.simpleAPI = simpleAPI;
        this.restaurantDao = restaurantDao;
    }

    public Call<List<Restaurant>> fetchRestaurants() {
        return this.simpleAPI.fetchRestaurants();
    }

    public LiveData<List<Restaurant>> getSavedRestaurants() {
        return this.restaurantDao.getAllRestaurants();
    }

    public void insertRestaurants(Restaurant... restaurants) {
        this.restaurantDao.insertRestaurants(restaurants);
    }
}
