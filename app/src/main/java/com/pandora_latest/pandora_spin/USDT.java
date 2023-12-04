package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class USDT extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;

    View saver;
    TextView copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usdt);
        Intent i = getIntent();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        saver = findViewById(R.id.saver);
        reference = database.getReference("Requests");
        copy = findViewById(R.id.copy);

        int cash = i.getIntExtra("amount",0);

        TextView amount = findViewById(R.id.amount);
        TextView pay = findViewById(R.id.confirm_btn);

        //convert it to usdt
        int usdts = cash/2333;
        String address = "TKjanS12K2utwuvXbEVbzAXZDSXkuMFsfa";
        amount.setText(String.valueOf("USDT "+usdts));

        ClipboardManager clip =(ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData data_ = ClipData.newPlainText("text",address);
                clip.setPrimaryClip(data_);
                Toast.makeText(getApplicationContext(), "Address Copied Successful", Toast.LENGTH_LONG).show();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(USDT.this);


                // inflate the custom dialog layout
                View view = getLayoutInflater().inflate(R.layout.address_dialogue, null);

                // get references to the dialog input fields and radio buttons
                EditText address = view.findViewById(R.id.addressEdit);

                builder.setView(view);
                AlertDialog dialog = builder.create();

                // set a click listener for the save button
                view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String addressValue = address.getText().toString();
                        if(!addressValue.isEmpty()){
                            saver.setVisibility(View.VISIBLE);
                            Request(String.valueOf(cash),auth.getUid(),addressValue);
                            dialog.dismiss();
                        }else{
                            address.setError("Invalid wallet address");
                        }

                    }
                });

                // set the custom layout for the dialog and show it

                dialog.show();
            }
        });
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getSupportActionBar().hide();
    }

    public void Request(String amount,String uid,String address)
    {

        String k = reference.child("Recharge").push().getKey();
        HashMap<String,Object> data = new HashMap<>();
        data.put("image","USDT");
        data.put("amount",amount);
        data.put("uid",uid);
        data.put("key",k);
        data.put("state","pending");
        data.put("number",address);
        data.put("date",String.valueOf(new Date().getTime()));


        reference.child("Recharge").child(k).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(USDT.this, "Request Sent \n Response May Take Up to 24h", Toast.LENGTH_LONG).show();
                saver.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(USDT.this, "Something went wrong \n Try checking your network", Toast.LENGTH_LONG).show();
                saver.setVisibility(View.GONE);
            }
        });
    }
}