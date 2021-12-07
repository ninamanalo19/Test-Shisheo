package com.test.currentTest.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.currentTest.model.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM restaurant_table")
    LiveData<List<Restaurant>> getAllRestaurants();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurants(Restaurant... restaurant);
}
