package com.test.currentTest.dependencyInjection;

import com.test.currentTest.network.SimpleAPI;
import com.test.currentTest.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Singleton
    @Provides
    public OkHttpClient provideHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Singleton
    @Provides
    public SimpleAPI provideAPI(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SimpleAPI.class);
    }
}
