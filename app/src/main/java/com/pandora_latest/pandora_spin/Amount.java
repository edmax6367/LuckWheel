package com.pandora_latest.pandora_spin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class Amount extends AppCompatActivity {

    int location = 0;

    //payments
    private ArrayList<Amount_model> amountsList;
    private Amount_adapter adapter;
    RecyclerView amounts_view;
    private LinearLayoutManager holizontal_LayoutManager;
    ////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        Intent i = getIntent();
        location = i.getIntExtra("l",0);

        //payment methords
        amountsList = new ArrayList<>();
        amounts_view = findViewById(R.id.amounts);
        amounts_view.setHasFixedSize(true);
        holizontal_LayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        amounts_view.setLayoutManager(holizontal_LayoutManager);

        Amount_model a1 = new Amount_model();
        a1.setAmount(25000);
        a1.setProfit(1200);
        a1.setVIP(1);
        a1.setLoc(location);

        Amount_model a2 = new Amount_model();
        a2.setAmount(50000);
        a2.setProfit(2300);
        a2.setVIP(2);
        a2.setLoc(location);

        Amount_model a3 = new Amount_model();
        a3.setAmount(100000);
        a3.setProfit(4700);
        a3.setVIP(3);
        a3.setLoc(location);

        Amount_model a4 = new Amount_model();
        a4.setAmount(500000);
        a4.setProfit(22000);
        a4.setVIP(4);
        a4.setLoc(location);

        Amount_model a5 = new Amount_model();
        a5.setAmount(1000000);
        a5.setProfit(25000);
        a5.setVIP(5);
        a5.setLoc(location);

        amountsList.add(a1);
        amountsList.add(a2);
        amountsList.add(a3);
        amountsList.add(a4);
        amountsList.add(a5);

        adapter = new Amount_adapter(getApplicationContext(),amountsList);
        amounts_view.setAdapter(adapter);

        ///


        getSupportActionBar().hide();
    }
}