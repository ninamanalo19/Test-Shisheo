package com.test.currentTest.data;

import androidx.room.Database;
import androidx.room.Index;
import androidx.room.RoomDatabase;

import com.test.currentTest.model.Restaurant;
import com.test.currentTest.utils.Constants;

@Database( entities = {Restaurant.class}, version = Constants.DB_VERSION )
public abstract class TestDatabase extends RoomDatabase {
    public abstract RestaurantDao restaurantDao();
}
