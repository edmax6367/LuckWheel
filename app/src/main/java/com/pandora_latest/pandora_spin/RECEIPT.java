package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class RECEIPT extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference,user,reference2;
    FirebaseStorage storage;

    View saver;
    TextView wallet,amount,copy,upload,pay;
    ImageView receiptImage;
    ImageView rImage,rImage2,wImage,wImage2;

    Uri imageSelected = null;
    int SELECT_PICTURE = 200;
    String image_data = "";
    String user_number ="";
    String uid = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        wallet = findViewById(R.id.wal);
        amount = findViewById(R.id.am);
        copy = findViewById(R.id.copy);
        upload = findViewById(R.id.up);
        pay = findViewById(R.id.confirm_btn);
        receiptImage = findViewById(R.id.re);

        Intent i = getIntent();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        saver = findViewById(R.id.saver);

        rImage = findViewById(R.id.r_image);
        rImage2 = findViewById(R.id.r_image2);
        wImage = findViewById(R.id.w_image);
        wImage2 = findViewById(R.id.w_image2);

        int cash = i.getIntExtra("amount",0);

        //convert it to Tsh
        int usdts = cash/1;

        if(usdts == 500000){
            Toast.makeText(this, "You got a bonus of 5000 Tsh for depositing 500K, Confirm Deposit to Receive", Toast.LENGTH_LONG).show();
            usdts = usdts+5000;
        }else if(usdts == 1000000){
            Toast.makeText(this, "You got a bonus of 25,000 Tsh for depositing 1 Million, Confirm Deposit to Receive", Toast.LENGTH_LONG).show();
            usdts = usdts+25000;
        }
        amount.setText(String.valueOf("Tsh "+usdts));

        uid = auth.getUid();

        reference = database.getReference("Requests");
        user = database.getReference().child("Users").child(uid);
        saver.setVisibility(View.VISIBLE);

        user.child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user_number = snapshot.getValue().toString();
                    wallet.setText(user_number);
                    saver.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImage();
            }
        });

        int finalUsdts = usdts;
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saver.setVisibility(View.VISIBLE);
                Request(String.valueOf(finalUsdts),auth.getUid(),user_number);

            }
        });
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //image listeners
        reference2 = database.getReference().child("Instructions");

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    if(snapshot.getKey().equals("recharge")){
                       // Glide.with(getApplicationContext()).load(snapshot.getValue().toString()).into(rImage);
                    }

                    if(snapshot.getKey().equals("banner")){
                        //Glide.with(getApplicationContext()).load(snapshot.getValue().toString()).into(rImage2);
                        Glide.with(getApplicationContext()).load(snapshot.getValue().toString()).into(wImage2);
                    }

                    if(snapshot.getKey().equals("withdraw")){
                        Glide.with(getApplicationContext()).load(snapshot.getValue().toString()).into(wImage);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getSupportActionBar().hide();

    }
    public void OpenImage(){
        Uri imageurl = imageSelected;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageSelected = selectedImageUri;

                    Glide.with(getApplicationContext()).load(imageSelected).into(receiptImage);
                    // Create a storage reference from our app
                    UploadImage(imageSelected);
                }
            }
        }
    }

    public void UploadImage(Uri image){
        saver.setVisibility(View.VISIBLE);
        StorageReference storageRef = storage.getReference();

        final StorageReference ref = storageRef.child(image.toString());
        UploadTask uploadTask = ref.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                saver.setVisibility(View.VISIBLE);
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saver.setVisibility(View.GONE);
                    image_data = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }

    public void Request(String amount,String uid,String address)
    {
        if(image_data.isEmpty()){
            image_data = "EMPTY";
        }
        String k = reference.child("Recharge").push().getKey();
        HashMap<String,Object> data = new HashMap<>();
        data.put("image",image_data);
        data.put("amount",amount);
        data.put("uid",uid);
        data.put("key",k);
        data.put("state","pending");
        data.put("number",address);
        data.put("date",String.valueOf(new Date().getTime()));


        reference.child("Recharge").child(k).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RECEIPT.this, "Request Sent \n Response May Take Up to 24h", Toast.LENGTH_LONG).show();
               // Toast.makeText(RECEIPT.this, k, Toast.LENGTH_SHORT).show();
                saver.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RECEIPT.this, "Something went wrong \n Try checking your network", Toast.LENGTH_LONG).show();
                saver.setVisibility(View.GONE);
            }
        });
    }
}