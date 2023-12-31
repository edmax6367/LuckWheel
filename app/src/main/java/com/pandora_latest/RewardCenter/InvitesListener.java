package com.pandora_latest.RewardCenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.QubeAuth.QubeAuth;
import com.pandora_latest.QubeAuth.QubeAuthCallbacks;
import com.pandora_latest.pandora_spin.Amount_model;

import java.util.ArrayList;

public class InvitesListener {

    static Context context;
    static FirebaseDatabase database;
    static InviteListenerCallbacks.RewardedCallback rewardedCallbacks;

    long my_net_balance = 0;
    long my_net_profit = 0;

    ValueEventListener cashListener;

    rewardModel r1 = new rewardModel();
    rewardModel r2 = new rewardModel();
    rewardModel r3 = new rewardModel();
    rewardModel r4 = new rewardModel();
    rewardModel r5 = new rewardModel();
    rewardModel r6 = new rewardModel();
    rewardModel r7 = new rewardModel();
    rewardModel r8 = new rewardModel();
    rewardModel r9 = new rewardModel();
    rewardModel r10 = new rewardModel();
    rewardModel r11 = new rewardModel();
    rewardModel r12 = new rewardModel();

    private ArrayList<rewardModel> rewardModels = new ArrayList<>();

    public InvitesListener(Context context,FirebaseDatabase database,View progress) {
        InvitesListener.context = context;
        InvitesListener.database = database;
        InvitationListener();


        //VIP
        r1.setDeposited(5000);
        r1.setReward(500);

        r2.setDeposited(15000);
        r2.setReward(1000);

        r3.setDeposited(25000);
        r3.setReward(1500);

        r4.setDeposited(35000);
        r4.setReward(2000);

        r5.setDeposited(55000);
        r5.setReward(2500);

        r6.setDeposited(100000);
        r6.setReward(3000);


        //VVIP
        r7.setDeposited(200000);
        r7.setReward(5000);

        r8.setDeposited(300000);
        r8.setReward(6000);

        r9.setDeposited(400000);
        r9.setReward(7000);

        r10.setDeposited(500000);
        r10.setReward(8000);

        r11.setDeposited(600000);
        r11.setReward(9000);

        r12.setDeposited(700000);
        r12.setReward(10000);


        rewardModels.add(r1);
        rewardModels.add(r2);
        rewardModels.add(r3);
        rewardModels.add(r4);
        rewardModels.add(r5);
        rewardModels.add(r6);
        rewardModels.add(r7);
        rewardModels.add(r8);
        rewardModels.add(r9);
        rewardModels.add(r10);
        rewardModels.add(r11);
        rewardModels.add(r12);

    }


    public String uid(){
        SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
        return datas.getString("auth","OUT");
    }

    public void InvitationListener() {
        //Toast.makeText(context, "Listening My invites", Toast.LENGTH_LONG).show();


        DatabaseReference reference;


        //Check for my current data
        reference = database.getReference().child("Users").child(uid());
        reference.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_net_balance = snapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
        reference.child("profit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_net_profit = snapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        //Scan the users i invited
        Query invitedUsersReference = FirebaseDatabase.getInstance().getReference().child("Users")
                ;


        invitedUsersReference.orderByChild("invited").equalTo(uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String key = snap.getKey();

                    DatabaseReference invitedUser = database.getReference().child("Users").child(key);

                    DatabaseReference balanceListerner;
                    balanceListerner = invitedUser.child("balance");

                    cashListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot s) {



                            invitedUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ss) {

                                    long userBalance = ss.child("balance").getValue(Long.class);
                                    boolean rewardSent = ss.child("rs").exists();

                                    if(!rewardSent && userBalance >= rewardModels.get(0).deposited){
                                        for(int i = 0; i < rewardModels.size(); i++){
                                            if(userBalance == rewardModels.get(i).deposited){
                                                long myReward = my_net_profit + rewardModels.get(i).reward;
                                                invitedUser.child("rs").setValue(true);
                                                //Toast.makeText(context, "Rewarded Tsh"+myReward, Toast.LENGTH_LONG).show();
                                                reference.child("profit").setValue(myReward);
                                                break;
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    balanceListerner.addValueEventListener(cashListener);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
