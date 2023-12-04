package com.pandora_latest.pandora_spin;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.QubeAuth.QubeAuth;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    QubeAuth auth;
    long total_invites = 0;
    float balance = 0;


    TextView numbertxt,balancetxt,invitestxt,profittxt,username;
    View logout,wallet,edit,market;
    private CircleImageView image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        numbertxt = findViewById(R.id.user_number);
        balancetxt = findViewById(R.id.balance);
        invitestxt = findViewById(R.id.invites);
        profittxt = findViewById(R.id.profit);
        logout = findViewById(R.id.logout);
        wallet = findViewById(R.id.recharge);
        market = findViewById(R.id.market);
        edit = findViewById(R.id.edit);
        image = findViewById(R.id.image);
        username = findViewById(R.id.user_name);


       // myApplication.showAdIfAvailable(this);

        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);
        reference = database.getReference().child("Users").child(auth.uid());

        reference.child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   String n = snapshot.getValue().toString();
                   numbertxt.setText(n);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String b = snapshot.getValue().toString();
                    balancetxt.setText(String.format(Locale.US,"%,d", Integer.parseInt(b))+"Tsh");
                    balance = Float.parseFloat(b);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("profit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String p = snapshot.getValue().toString();
                    profittxt.setText(String.format(Locale.US,"%,d", Integer.parseInt(p))+"Tsh");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Glide.with(getApplicationContext()).load(snapshot.getValue(String.class)).into(image);
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
                    username.setText(snapshot.getValue(String.class));
                    checkMyinvites();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference banner_reference = database.getReference().child("Banners");
        //ImageView imageSlider = findViewById(R.id.image_slider);
        banner_reference.child("main").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.getValue().toString();
                //Glide.with(getApplicationContext()).load(url).into(imageSlider);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
        

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Recharge.class);
                i.putExtra("balance",balance);
                startActivity(i);
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Market.class));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Edit.class));
            }
        });

        View abt = findViewById(R.id.about);
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),About.class);
                startActivity(i);
            }
        });



        TextView deletion = findViewById(R.id.deletion);

        deletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlToOpen = "https://pandoraspin.com/#/accounts/deletion/"+numbertxt.getText()+"@qube.com";
                openUrl(urlToOpen);
            }
        });



        TextView pp = findViewById(R.id.pp);
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        String tt = pp.getText().toString();
        final int screenWidth = displayMetrics.widthPixels;
        final int Textlength = tt.length() * 13;

        final TranslateAnimation translateAnimation = new TranslateAnimation(Textlength, -Textlength, 0, 0);
        translateAnimation.setDuration(10000);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        pp.startAnimation(translateAnimation);


        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pp.startAnimation(translateAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




        getSupportActionBar().hide();
    }

    public void Logout(){
        auth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void openUrl(String url) {
       auth.DeleteAccount(url);
    }

    public void checkMyinvites() {
        DatabaseReference userRef = database.getReference("Users");
        Query myInvites = userRef.orderByChild("invited").equalTo(auth.uid());
        myInvites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()) {
                        long balance = Long.parseLong(ds.child("balance").getValue().toString());
                        total_invites ++;
                    }
                    total_invites = snapshot.getChildrenCount();
                    invitestxt.setText(String.valueOf(total_invites)+" Invites");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}