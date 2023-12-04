package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class NORMAL_WITHDRAW extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    TextView name,editWallet,editNetwork,editAmount,confirm;
    TextView wallet,network,amount,afterTax;

    String uid ="";
    String wal ="";
    String net ="";
    String am ="";
    String rec ="";


    View saver;

    long total_invites = 0;
    long total_profit = 0;
    long totalAmount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_withdraw);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        editWallet = findViewById(R.id.edit_wallet);
        wallet = findViewById(R.id.wal);
        editNetwork = findViewById(R.id.edit_network);
        network = findViewById(R.id.net);
        editAmount = findViewById(R.id.edit_amount);
        amount = findViewById(R.id.cash);
        saver = findViewById(R.id.saver);
        confirm = findViewById(R.id.confirm_btn);
        afterTax = findViewById(R.id.cash2);



        uid = auth.getUid();
        name = findViewById(R.id.user);
        saver.setVisibility(View.VISIBLE);
        DatabaseReference user = database.getReference().child("Users").child(uid);
        user.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.getValue().toString());
                    rec = snapshot.getValue().toString();
                    saver.setVisibility(View.GONE);
                    invites();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user.child("profit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    total_profit = (long) snapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(NORMAL_WITHDRAW.this);


                // inflate the custom dialog layout
                View view = getLayoutInflater().inflate(R.layout.wallet_dialogue, null);

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
                            wallet.setText(addressValue);
                            wal = addressValue;
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
        editAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(NORMAL_WITHDRAW.this);


                // inflate the custom dialog layout
                View view = getLayoutInflater().inflate(R.layout.amount_dialogue, null);

                // get references to the dialog input fields and radio buttons
                EditText address = view.findViewById(R.id.addressEdit);

                builder.setView(view);
                AlertDialog dialog = builder.create();

                // set a click listener for the save button
                view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String amountValue = address.getText().toString();
                        if(!amountValue.isEmpty()){
                            amount.setText("Tsh "+amountValue);
                            am = amountValue;

                            if(total_invites < 3){
                                int percent = 8;
                                int amount = Integer.parseInt(am);
                                afterTax.setText(String.valueOf("Tsh "+String.valueOf(amount - amount * percent / 100)));

                                am = String.valueOf(amount - amount * percent / 100);
                            }


                            totalAmount = Long.parseLong(am);

                            dialog.dismiss();
                        }else{
                            address.setError("Invalid Amount");
                        }

                    }
                });

                // set the custom layout for the dialog and show it

                dialog.show();
            }
        });
        editNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNetDialog(network);
            }
        });
        
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(am.isEmpty()){
                    Toast.makeText(NORMAL_WITHDRAW.this, "invalid amount", Toast.LENGTH_LONG).show();
                }else{
                    CashOut(totalAmount,uid,wal,net,Long.parseLong(am));
                }

            }
        });

        getSupportActionBar().hide();
    }

    private void showNetDialog(TextView text) {
        String[] options = {"None","Tigo","Airtel","Zantel","Halotel","TTCL","Vodacom"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NORMAL_WITHDRAW.this);
        builder.setTitle("Select A Receiving Network")
                .setSingleChoiceItems(options, -1, (dialog, which) -> {
                    String selectedOption = options[which];

                    text.setText(selectedOption);
                    net = selectedOption;

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void invites(){

        DatabaseReference reference = database.getReference().child("Users").child(auth.getUid());
        DatabaseReference iUserData = database.getReference().child("Users");
        reference.child("invite").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                saver.setVisibility(View.VISIBLE);

                    String id = snapshot.getValue(String.class);
                    iUserData.child(id).child("balance")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        saver.setVisibility(View.GONE);
                                        long cash = Long.parseLong(snapshot.getValue().toString());
                                        if(cash >= 25000){
                                            total_invites++;
                                            saver.setVisibility(View.GONE);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                    if(total_invites > 1){
                        reference.child("cash_out_twice").setValue(true);
                    }else {
                        reference.child("cash_out_twice").setValue(false);

                    }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                total_invites--;

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NORMAL_WITHDRAW.this, "NONE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void CashOut(long amount,String uid,String number,String net,long cutter){

        if(totalAmount < 2500){
            Toast.makeText(this, "Minimum withdraw is Tsh 2600", Toast.LENGTH_LONG).show();
            return;
        }

        if(number.isEmpty()){
            Toast.makeText(this, "Invalid wallet", Toast.LENGTH_LONG).show();
            return;
        }

        if(net.isEmpty()){
            Toast.makeText(this, "Invalid Network", Toast.LENGTH_LONG).show();
            return;
        }

        saver.setVisibility(View.VISIBLE);
        DatabaseReference reference = database.getReference("Requests");
        String k = reference.child("Withdraw").push().getKey();
        HashMap<String,Object> data = new HashMap<>();
        data.put("amount",amount);
        data.put("id",uid);
        data.put("key",k);
        data.put("number",number);
        data.put("state","pending");
        data.put("network",net);
        data.put("expiration",String.valueOf(new Date().getTime()));

        if(amount <= total_profit){
            DatabaseReference user = database.getReference().child("Users").child(uid);
            long deduct = total_profit - cutter;
            user.child("profit").setValue(deduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    reference.child("Withdraw").child(k).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(NORMAL_WITHDRAW.this, "Withdraw Successful \n Please Wait It May Take Up To 48h To Receive Money", Toast.LENGTH_SHORT).show();
                            saver.setVisibility(View.GONE);
                            user.child("cashed_out").setValue(true);
                        }
                    });
                }
            });
        }else {
            saver.setVisibility(View.GONE);
            Toast.makeText(this, "You Have Insufficient Balance", Toast.LENGTH_LONG).show();
        }

    }
}