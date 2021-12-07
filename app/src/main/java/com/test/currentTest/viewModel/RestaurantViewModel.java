package com.test.currentTest.viewModel;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.currentTest.data.TestDatabase;
import com.test.currentTest.model.Restaurant;
import com.test.currentTest.repository.RestaurantRepository;
import com.test.currentTest.restaurants.RestaurantsFragment;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {

    private static final String TAG = RestaurantViewModel.class.getSimpleName();
    private RestaurantRepository restaurantRepository;
    private MutableLiveData<Throwable> errorMutableLiveData;
    private MutableLiveData<Boolean> isLoadingMutableLiveData;
    private TestDatabase testDatabase;

    public LiveData<List<Restaurant>> getRestaurants() {
        LiveData<List<Restaurant>> savedRestaurantsLiveData = this.restaurantRepository.getSavedRestaurants();
        return savedRestaurantsLiveData;
    }

    public LiveData<Throwable> getError() {
        return this.errorMutableLiveData;
    }

    public LiveData<Boolean> isLoading() {
        return this.isLoadingMutableLiveData;
    }

    @Inject
    public RestaurantViewModel(RestaurantRepository restaurantRepository, TestDatabase testDatabase) {
        this.restaurantRepository = restaurantRepository;
        this.testDatabase = testDatabase;
        this.errorMutableLiveData = new MutableLiveData<>();
        this.isLoadingMutableLiveData = new MutableLiveData<>(false);
    }

    private Single<List<Restaurant>> fetchRestaurants() {
        return Single.create(new SingleOnSubscribe<List<Restaurant>>() {
            @Override
            public void subscribe(SingleEmitter<List<Restaurant>> emitter) throws Exception {
                RestaurantViewModel.this.restaurantRepository.fetchRestaurants().enqueue(new Callback<List<Restaurant>>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        emitter.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        emitter.onError(t);
                    }
                });
            }
        });
    }

    public void requestRestaurants() {
        Log.d(TAG, "requestRestaurants: called ..");
        this.isLoadingMutableLiveData.setValue(true);
        fetchRestaurants()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<Restaurant>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(List<Restaurant> restaurants) {
                    RestaurantViewModel.this.isLoadingMutableLiveData.setValue(false);
                    insertRestaurants(restaurants);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: e", e);
                    RestaurantViewModel.this.isLoadingMutableLiveData.setValue(false);
                    RestaurantViewModel.this.errorMutableLiveData.setValue(e);
                }
            });
    }

    private void insertRestaurants(List<Restaurant> restaurants) {
        Observable.just(restaurants)
            .subscribeOn(Schedulers.io())
            .subscribe(new Observer<List<Restaurant>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<Restaurant> restaurants) {
                    RestaurantViewModel.this.restaurantRepository.insertRestaurants(restaurants.toArray(new Restaurant[0]));
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

}
