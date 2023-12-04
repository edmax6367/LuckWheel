package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public StorageReference sr;

    public CircleImageView image;
    public TextView name;
    Uri selectedImage = null;
    String imageLink = null;

    public View processing,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        image =  findViewById(R.id.image);
        name = findViewById(R.id.name);
        processing = findViewById(R.id.processing);
        save = findViewById(R.id.save);
        database = FirebaseDatabase.getInstance();
        sr = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid());
        processing.setVisibility(View.VISIBLE);

        reference.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Glide.with(getApplicationContext()).load(snapshot.getValue(String.class)).into(image);
                    imageLink = snapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.getValue(String.class));
                    processing.setVisibility(View.GONE);
                }else{
                    Toast.makeText(Edit.this, "Set You Profile", Toast.LENGTH_LONG).show();
                    processing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose an image from the device
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 100);
                } else {
                    // Provide a fallback action if the desired Activity is not available
                    // For example, you could use a file manager to allow the user to pick an image
                    // Use a file manager as a fallback
                    Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    fileManagerIntent.setType("image/*");
                    startActivityForResult(fileManagerIntent, 100);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()){
                    name.setError("Name Required");
                }else if(imageLink == null){
                    Toast.makeText(Edit.this, "Tap Image Icon To Set Profile Image", Toast.LENGTH_LONG).show();
                }else {
                    processing.setVisibility(View.VISIBLE);
                    reference.child("name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                                reference.child("image").setValue(imageLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        processing.setVisibility(View.GONE);
                                        Toast.makeText(Edit.this, "Profile Updated Successful", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        getSupportActionBar().setTitle("Profile Data");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            selectedImage = data.getData();
            Glide.with(getApplicationContext()).load(selectedImage).into(image);

            Upload_Main();
        }
    }

    public void Upload_Main()
    {
        final StorageReference ref = sr.child("images/" + new Date().getTime() +"/"+"mg");
        UploadTask uploadTask = ref.putFile(selectedImage);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                processing.setVisibility(View.VISIBLE);
                Toast.makeText(Edit.this, "Profile Image Uploading" + progress + "%", Toast.LENGTH_LONG).show();

            }
        });

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        processing.setVisibility(View.VISIBLE);
                        Toast.makeText(Edit.this,"Finishing Up", Toast.LENGTH_LONG).show();
                        return ref.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        Uri main_image_link = task.getResult();
                        Toast.makeText(getApplicationContext(), "profile image updated", Toast.LENGTH_LONG).show();

                        imageLink = main_image_link.toString();
                        //Toast.makeText(Profile.this, imageLink, Toast.LENGTH_LONG).show();

                        processing.setVisibility(View.GONE);

                    }
                });
    }

}