package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pandora_latest.Chat_Room.TimeManager;
import com.pandora_latest.MarketUtils.itemAdapter;
import com.pandora_latest.MarketUtils.itemModel;
import com.pandora_latest.QubeAuth.QubeAuth;

import java.util.ArrayList;

public class MyItems extends AppCompatActivity {

    private ArrayList<itemModel> product_list = new ArrayList<>();
    itemAdapter adapter;
    RecyclerView products;
    private GridLayoutManager grid_layout;

    private FirebaseDatabase database;
    QubeAuth auth;

    private TimeManager timeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        products = findViewById(R.id.purchases);
        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);


        products.setHasFixedSize(true);
        grid_layout = new GridLayoutManager(getApplicationContext(),2);
        products.setLayoutManager(grid_layout);

        timeManager = new TimeManager(auth.uid());
        timeManager.startUpdatingTime();

        DatabaseReference product_store = database.getReference("Users");

        product_store.child(auth.uid()).child("Items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);
                long time = snapshot.child("time").getValue(Long.class);
                long profit = snapshot.child("profit").getValue(Long.class);
                long price = snapshot.child("price").getValue(Long.class);

                itemModel product_data = new itemModel();
                product_data.setId(id);
                product_data.setName(name);
                product_data.setImage(image);
                product_data.setTime(time);
                product_data.setProfit(profit);
                product_data.setPrice(price);

                product_list.add(product_data);


                adapter = new itemAdapter(product_list, getApplicationContext());
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
                    itemModel product_ = product_list.get(i);

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

        getSupportActionBar().setTitle("My Purchases");
    }
}