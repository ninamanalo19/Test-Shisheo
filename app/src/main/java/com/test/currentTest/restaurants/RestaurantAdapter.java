package com.test.currentTest.restaurants;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.currentTest.R;
import com.test.currentTest.databinding.RestaurantRowBinding;
import com.test.currentTest.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements Filterable {

    private static final String TAG = RestaurantAdapter.class.getSimpleName();
    private static final int ITEM_WITH_DELIVERY = 1;
    private List<Restaurant> restaurants;
    private List<Restaurant> filteredRestaurants;

    public RestaurantAdapter() {
        this.populateRestaurants(new ArrayList<>());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.restaurant_row,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = this.filteredRestaurants.get(position);
        RestaurantRowBinding binding = holder.binding;
        binding.setRestaurant(restaurant);
        binding.imgRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llRating.getRoot().setVisibility(View.VISIBLE);
            }
        });
        Glide.with(binding.getRoot())
                .load(restaurant.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.imgRestaurant);
        // BELOW: this is based on the design.
        // since I can't find delivery on the response object, I had to hard code this one.
        binding.tvDelivery.setVisibility(position == ITEM_WITH_DELIVERY ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return this.filteredRestaurants.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String constraintString = constraint.toString();
                if (TextUtils.isEmpty(constraintString)) {
                    RestaurantAdapter.this.filteredRestaurants = RestaurantAdapter.this.restaurants;
                } else {
                    RestaurantAdapter.this.filteredRestaurants = RestaurantAdapter.this.restaurants.stream().filter(
                            restaurant -> areStringsMatch(restaurant.name, constraintString) || areStringsMatch(restaurant.description, constraintString)
                    ).collect(Collectors.toList());
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = RestaurantAdapter.this.filteredRestaurants;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                RestaurantAdapter.this.filteredRestaurants = (List<Restaurant>) results.values;
                RestaurantAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private boolean areStringsMatch(String original, String current) {
        return original.toLowerCase(Locale.ROOT).contains(current.toLowerCase(Locale.ROOT));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RestaurantRowBinding binding;
        ViewHolder(RestaurantRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void populateRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.filteredRestaurants = this.restaurants;
    }

    public void addRestaurants(List<Restaurant> restaurants) {
        this.populateRestaurants(restaurants);
        this.notifyDataSetChanged();
    }
}
