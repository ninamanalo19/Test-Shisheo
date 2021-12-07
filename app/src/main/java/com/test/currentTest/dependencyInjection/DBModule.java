package com.test.currentTest.dependencyInjection;

import android.content.Context;

import androidx.room.Room;

import com.test.currentTest.MainApplication;
import com.test.currentTest.data.RestaurantDao;
import com.test.currentTest.data.TestDatabase;
import com.test.currentTest.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DBModule {

    @Singleton
    @Provides
    public MainApplication provideApplication(@ApplicationContext Context app) {
        return (MainApplication) app;
    }


    @Singleton
    @Provides
    public RestaurantDao provideQuestionDao(TestDatabase testDatabase) {
        return testDatabase.restaurantDao();
    }

    @Singleton
    @Provides
    public TestDatabase provideDatabase(MainApplication application) {
        return Room.databaseBuilder(
            application,
            TestDatabase.class,
            Constants.DB_NAME
        ).build();
    }
}
