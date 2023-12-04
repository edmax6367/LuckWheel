package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.MarketUtils.Timer;
import com.pandora_latest.MarketUtils.myItem;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductView extends AppCompatActivity {
    String image ,name,id;
    Long price,profit,duration;

    TextView p_name,p_price,p_profit,p_duration,purchase;
    ImageView p_image;

    FirebaseAuth auth;
    FirebaseDatabase database;

    Timer timer;
    View saver;

    boolean purchased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        p_name = findViewById(R.id.name);
        p_image = findViewById(R.id.product_image);
        p_price = findViewById(R.id.price);
        p_profit = findViewById(R.id.profit);
        p_duration = findViewById(R.id.duration);
        saver = findViewById(R.id.saver);
        purchase = findViewById(R.id.confirm_btn);

        Intent i = getIntent();
        image = i.getStringExtra("image");
        name = i.getStringExtra("details");
        price = i.getLongExtra("price",0);
        profit = i.getLongExtra("profit",0);
        duration = i.getLongExtra("duration",0);
        id = i.getStringExtra("id");

        p_name.setText(name);
        Glide.with(getApplicationContext()).load(image).into(p_image);
        p_price.setText(String.format(Locale.US,"%,d", price)+"Tsh");
        p_profit.setText(String.format(Locale.US,"%,d", profit)+"Tsh");
        p_duration.setText(String.valueOf(duration)+"days");
        TextView v = findViewById(R.id.description);
        v.setText(name);

        DatabaseReference myItems = database.getReference("Users").child(auth.getUid()).child("Items");
        DatabaseReference user = database.getReference("Users").child(auth.getUid());

        myItems.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchased = snapshot.exists();
                if(purchased){
                    purchase.setText("Purchased");
                    purchase.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!purchased){
            purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String key = myItems.push().getKey();

                    myItem product_data = new myItem();
                    product_data.setId(id);
                    product_data.setName(name);
                    product_data.setImage(image);
                    product_data.setProfit(profit);
                    product_data.setTime(addDaysToCurrentTime(duration).getTime());
                    product_data.setPrice(price);
                    product_data.setKey(key);

                    if(!purchased){
                        saver.setVisibility(View.VISIBLE);


                        user.child("balance").addListenerForSingleValueEvent(new ValueEventListener()  {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    user.child("balance").setValue(0);
                                }else {
                                    long total_balance = (long) snapshot.getValue();

                                    if(total_balance >= price){
                                        long payment =  total_balance - price;
                                        user.child("balance").setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isComplete()) {
                                                    Buy(product_data,id);
                                                }else {
                                                    Toast.makeText(ProductView.this, "Purchase Failed Try Again.", Toast.LENGTH_SHORT).show();
                                                    saver.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(ProductView.this, "Insufficient Assets/Balance", Toast.LENGTH_SHORT).show();
                                        saver.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
            });
        }else{
            purchase.setVisibility(View.GONE);
        }



    }

    public Date addDaysToCurrentTime(long days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Set current time as the base

        calendar.add(Calendar.DAY_OF_YEAR, (int) days); // Add the specified number of days

        return calendar.getTime();
    }

    public void Buy(myItem item,String key){
        DatabaseReference myItems = database.getReference("Users").child(auth.getUid()).child("Items");

        myItems.child(key).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isComplete()) {
                    saver.setVisibility(View.GONE);
                    Toast.makeText(ProductView.this, "Purchase Completed.", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(ProductView.this, "Purchase Failed.", Toast.LENGTH_LONG).show();
                    saver.setVisibility(View.GONE);
                }
            }
        });

    }
}