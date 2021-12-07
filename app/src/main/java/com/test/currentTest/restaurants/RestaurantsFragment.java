package com.test.currentTest.restaurants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.currentTest.R;
import com.test.currentTest.databinding.FragmentRestaurantsBinding;
import com.test.currentTest.model.Restaurant;
import com.test.currentTest.viewModel.RestaurantViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantsFragment extends Fragment {

    private static final String TAG = RestaurantsFragment.class.getSimpleName();
    private FragmentRestaurantsBinding binding;
    private View rootView;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantAdapter restaurantAdapter;
    private SwipeRefreshLayout srRestaurants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        this.rootView = this.binding.getRoot();
        this.srRestaurants = this.binding.srRestaurants;
        this.restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        this.setRecyclerViewData();
        return this.rootView;
    }

    private void setSearchView() {
        SearchView svRestaurants = this.binding.svRestaurants;
        svRestaurants.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RestaurantsFragment.this.restaurantAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setPullToRefresh() {
        this.srRestaurants.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RestaurantsFragment.this.requestRestaurants(true);
            }
        });
    }

    private void setRecyclerViewData() {
        RecyclerView rvRestaurants = this.binding.rvRestaurants;
        rvRestaurants.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        this.restaurantAdapter = new RestaurantAdapter();
        rvRestaurants.setAdapter(this.restaurantAdapter);
        this.setSearchView();
        this.observeData();
    }

    private void requestRestaurants(boolean forceRequest) {
        this.binding.tvError.setText(null);
        this.restaurantViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                if (restaurants != null) {
                    boolean isRefreshing = RestaurantsFragment.this.srRestaurants.isRefreshing();
                    if ((forceRequest && isRefreshing) || restaurants.size() <= 0) {
                        Log.d(TAG, "onChanged(): request");
                        RestaurantsFragment.this.restaurantViewModel.requestRestaurants();
                    } else if (restaurants.size() > 0) {
                        Log.d(TAG, "onChanged(): add");
                        RestaurantsFragment.this.restaurantAdapter.addRestaurants(restaurants);
                    } else if (restaurants.size() <= 0 && !isRefreshing) {
                        Log.d(TAG, "onChanged(): found none");
                        RestaurantsFragment.this.binding.tvError.setText(getString(R.string.found_none));
                    }
                }
            }
        });
    }

    private void observeData() {
        this.restaurantViewModel.isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                RestaurantsFragment.this.srRestaurants.setRefreshing(isLoading);
            }
        });
        this.restaurantViewModel.getError().observe(getViewLifecycleOwner(), new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                RestaurantsFragment.this.binding.tvError.setText(getString(R.string.error));
                Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.requestRestaurants(false);
        this.setPullToRefresh();
    }
}