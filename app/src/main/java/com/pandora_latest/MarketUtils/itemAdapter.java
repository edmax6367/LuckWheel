package com.pandora_latest.MarketUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;
import java.util.Locale;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {

    public ArrayList<itemModel> product_list;
    Context context;
    private AlertDialog dialog;

    FirebaseAuth auth;
    FirebaseDatabase database;

    public itemAdapter(ArrayList<itemModel> product_list, Context context) {
        this.product_list = product_list;
        this.context = context;
    }

    @NonNull
    @Override
    public itemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchased_item, parent, false);
        return new itemAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull itemAdapter.ViewHolder holder, int position) {
        itemModel data = product_list.get(position);
        String name = data.getName();
        String image = data.getImage();
        long time = data.getTime();
        long profit = data.getProfit();
        long price = data.getPrice();
        String id = data.getId();
        Glide.with(context).load(image).into(holder.product_image);
        holder.profit.setText(String.format(Locale.US,"%,d", profit)+"Tsh");
        holder.time.setText(calculateRemainingDays(time)+"day(s) Left");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference myItems = database.getReference("Users").child(auth.getUid()).child("Items");
        DatabaseReference user = database.getReference("Users").child(auth.getUid());

        if(calculateRemainingDays(time) <= 0){

            user.child("profit").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        long profit_balance = snapshot.getValue(Long.class);

                        user.child("profit").setValue((profit_balance+profit+price)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                myItems.child(id).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return product_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView profit,time;
        public ImageView product_image;


        public ViewHolder(View v){
            super(v);
            product_image = v.findViewById(R.id.product_image);
            profit = v.findViewById(R.id.price);
            time = v.findViewById(R.id.timer);

        }
    }

    public static long calculateRemainingDays(long targetMillis) {
        long currentMillis = System.currentTimeMillis();
        long remainingMillis = targetMillis - currentMillis;

        if (remainingMillis <= 0) {
            return 0;
        }

        // Convert milliseconds to days
        long remainingDays = remainingMillis / (1000 * 60 * 60 * 24);
        return remainingDays;
    }


}