package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pandora_latest.Chat_Room.TimeManager;
import com.pandora_latest.MarketUtils.marketAdapter;
import com.pandora_latest.MarketUtils.marketModel;
import com.pandora_latest.QubeAuth.QubeAuth;

import java.util.ArrayList;

public class Market extends AppCompatActivity {

    private ArrayList<marketModel> product_list = new ArrayList<>();
    marketAdapter adapter;
    RecyclerView products;
    private GridLayoutManager grid_layout;

    private FirebaseDatabase database;
    QubeAuth auth;

    private TimeManager timeManager;
    public TextView my_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        products = findViewById(R.id.store);
        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);

        my_items = findViewById(R.id.my_items);


        products.setHasFixedSize(true);
        grid_layout = new GridLayoutManager(getApplicationContext(),2);
        products.setLayoutManager(grid_layout);

        timeManager = new TimeManager(auth.uid());
        timeManager.startUpdatingTime();

        DatabaseReference product_store = database.getReference("Market");

        product_store.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                String description = snapshot.child("description").getValue(String.class);
                long price = snapshot.child("price").getValue(Long.class);
                long discount = snapshot.child("discount").getValue(Long.class);
                long duration = snapshot.child("duration").getValue(Long.class);
                long profit = snapshot.child("profit").getValue(Long.class);

                marketModel product_data = new marketModel();
                product_data.setId(id);
                product_data.setName(name);
                product_data.setImage(image);
                product_data.setPrice(price);
                product_data.setDiscount(discount);
                product_data.setDescription(description);
                product_data.setDuration(duration);
                product_data.setProfit(profit);

                product_list.add(product_data);


                adapter = new marketAdapter(product_list, getApplicationContext());
                products.setAdapter(adapter);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();

                // Iterate over the message_list array
                for (int i = 0; i < product_list.size(); i++) {
                    marketModel product_ = product_list.get(i);

                    // Check if the message has the same key as the removedKey
                    if (product_.getId().equals(removedKey)) {
                        // Remove the item from the array
                        product_list.remove(i);

                        // Notify the adapter that the item has been removed
                        adapter.notifyItemRemoved(i);
                        break;  // Exit the loop after removing the item
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        my_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MyItems.class);
                startActivity(i);
            }
        });


        getSupportActionBar().setTitle("Market Place");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeManager.stopUpdatingTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeManager.stopUpdatingTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeManager.startUpdatingTime();
    }
}