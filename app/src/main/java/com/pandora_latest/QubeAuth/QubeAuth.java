package com.pandora_latest.QubeAuth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.pandora_spin.MainActivity;
import com.pandora_latest.pandora_spin.Register;

import java.util.Date;
import java.util.HashMap;

public class QubeAuth {
    static Context context;
    static FirebaseDatabase database;
    static QubeAuthCallbacks.SignInCallback qubeAuthCallbacks;
    boolean activeService = false;
    static View progress;


    public QubeAuth(Context context,FirebaseDatabase database,View progress) {
        QubeAuth.context = context;
        QubeAuth.database = database;
        QubeAuth.progress = progress;
        subscription();
        currentUser();
    }
    public void setSignInCallback(QubeAuthCallbacks.SignInCallback callback) {
        QubeAuth.qubeAuthCallbacks = callback;
    }
    public void createUser(String phone,String password,String username,String imageLink){


        if(activeService){
            DatabaseReference reference = database.getReference().child("Users");

            Query phoneRef = reference.orderByChild("number").equalTo(phone);

            phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(context, "Account Already Exists", Toast.LENGTH_LONG).show();
                        View saver = QubeAuth.progress;
                        if (qubeAuthCallbacks != null) {
                            qubeAuthCallbacks.onSignInFailure("Account Already Exists Callback");
                        }
                        if(saver != null){
                            saver.setVisibility(View.GONE);
                        }

                    }else{
                        HashMap<String,Object> data = new HashMap<>();
                        String id = reference.push().getKey();
                        data.put("number",phone);
                        data.put("pass",password);
                        data.put("auth","0");
                        data.put("balance",0);
                        data.put("name",username);
                        data.put("image",imageLink);
                        data.put("profit",0);
                        data.put("gift",5000);
                        data.put("d",new Date().getTime());

                        saveAuth(id);

                        reference.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent i = new Intent(context,MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed Try Again", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
           // Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }



    }
    public void signUser(String number, String password) {
        if(activeService){
            DatabaseReference userRef = database.getReference("Users");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String phoneNumber = userSnapshot.child("number").getValue(String.class);
                            String userPassword = userSnapshot.child("pass").getValue(String.class);
                            String userId = userSnapshot.getKey();

                            if (phoneNumber != null && userPassword != null &&
                                    phoneNumber.equals(number) && userPassword.equals(password)) {
                                // You found a user with the provided number and password
                                // userId contains the user's ID
                                //Toast.makeText(context, userId, Toast.LENGTH_LONG).show();
                                saveAuth(userId);

                                return; // Exit the loop as you found a matching user
                            }
                        }

                        // If you reach here, it means there's no user with the provided number and password
                        Toast.makeText(context, "User doesn't exist or wrong password", Toast.LENGTH_LONG).show();
                        View saver = QubeAuth.progress;
                        if (saver != null) {
                            saver.setVisibility(View.GONE);
                        }
                    } else {
                        // Handle the case where there are no users in the "Users" node
                        if (qubeAuthCallbacks != null) {
                            qubeAuthCallbacks.onSignInFailure("Account doesn't exits");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        }else{
            //Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }

    }
    private void saveAuth(String id){
        if(activeService){
            SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
            final SharedPreferences.Editor editor = datas.edit();
            editor.putString("auth",id);
            editor.apply();

            if (qubeAuthCallbacks != null) {
                qubeAuthCallbacks.onSignInSuccess(id);
            }
        }else{
            //Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }

    }
    public String uid(){
        SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
        return datas.getString("auth","OUT");
    }
    public void signOut(){
        if(activeService){
            SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
            final SharedPreferences.Editor editor = datas.edit();
            editor.remove("auth");
            editor.apply();
            Intent i = new Intent(context,Register.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            if (qubeAuthCallbacks != null) {
                qubeAuthCallbacks.onSignInSuccess("Signed Out");
            }
        }else{
           // Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }

    }
    private void subscription(){
        DatabaseReference security = database.getReference().child("QubeSecurity");


        security.child("sub").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    activeService = true;
                    //Toast.makeText(context, "Active Service", Toast.LENGTH_LONG).show();
                }else{
                    activeService = false;
                   // Toast.makeText(context, "InActive Service", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void currentUser(){
        String id = "";

        SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
        id = datas.getString("auth","OUT");

        if(!id.equals("OUT")){
            DatabaseReference r = database.getReference().child("Users").child(id);
            Query phoneRef = r.orderByChild("number");
            phoneRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        //this means user data is corrupted
                        //For management reasons and security it has to be deleted automatic
                        r.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    signOut();
                                }
                                if(task.isCanceled()){
                                    signOut();
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            signOut();
        }
    }
    public void DeleteAccount(String url){
        if(activeService){

            SharedPreferences datas = context.getSharedPreferences("qube_auth",0);
            final SharedPreferences.Editor editor = datas.edit();
            editor.remove("auth");
            editor.apply();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            if (qubeAuthCallbacks != null) {
               // qubeAuthCallbacks.onSignInSuccess("Authenticating");
            }
        }else{
            // Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
        }
    }
}
