package com.pandora_latest.pandora_spin;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pandora_latest.QubeAuth.QubeAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Recharge extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference reference,user,reference2;
    QubeAuth auth;
    FirebaseStorage storage;

    String uid = "null";

    View wid,recharge,send_w_request,send_r_request;
    View container_withdraw,container_recharge;
    TextView number,money,screenshot,network;

    EditText amount,amount_added,camount;

    Uri imageSelected = null;
    int SELECT_PICTURE = 200;
    String image_data = "";
    long total_profit = 0;
    boolean cashed_out = false;
    boolean piramid = false;
    String user_number ="";

    String wallet_network = "";
    String[] networkNames;
    Spinner spin;

    String helpType ="w";
    View close1,close2,wc,rc;

    TextView help;

    ImageView rImage_,rImage2_,wImage,wImage2;

    long tax;
    TextView taxtext;
    long total_invites = 0;


    //payments
    private ArrayList<Payment_model> paymentList;
    private Payment_adapter adapter;
    RecyclerView payments_view;
    private LinearLayoutManager holizontal_LayoutManager;
    ////////

    View saver;

    float balance = 0;
    private boolean limitWithdraw = false;


    private boolean removeTax = false;
    private long totalDeposits = 0;

    VideoView vid;
    View mute,un_mute,full_screen,min_screen;
    View min_Vid;
    MediaPlayer media;
    private boolean isFullScreen = false;

    private ArrayList<Amount_model> amountModels = new ArrayList<>();

    Amount_model a18 = new Amount_model();
    Amount_model a17 = new Amount_model();
    Amount_model a16 = new Amount_model();
    Amount_model a15 = new Amount_model();
    Amount_model a14 = new Amount_model();
    Amount_model a13 = new Amount_model();
    Amount_model a12 = new Amount_model();
    Amount_model a11 = new Amount_model();
    Amount_model a10 = new Amount_model();
    Amount_model a9 = new Amount_model();
    Amount_model a8 = new Amount_model();
    Amount_model a7 = new Amount_model();
    Amount_model a6 = new Amount_model();
    Amount_model a5 = new Amount_model();
    Amount_model a4 = new Amount_model();
    Amount_model a3 = new Amount_model();
    Amount_model a2 = new Amount_model();
    Amount_model a1 = new Amount_model();

    int openState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,saver);
        storage = FirebaseStorage.getInstance();
        recharge = findViewById(R.id.rec);
        wid = findViewById(R.id.wid);
        mute = findViewById(R.id.mute_on);
        un_mute = findViewById(R.id.mute_off);
        full_screen = findViewById(R.id.full);
        min_screen = findViewById(R.id.min);
        amount = findViewById(R.id.amount);
        camount = findViewById(R.id.amount);
        send_w_request = findViewById(R.id.w_r);
        send_r_request = findViewById(R.id.send);
        amount_added = findViewById(R.id.amount_recharge);
        screenshot = findViewById(R.id.screen_recharge);
        help = findViewById(R.id.help);
        wc = findViewById(R.id.w);
        rc = findViewById(R.id.r);
        close1 = findViewById(R.id.close1);
        close2 = findViewById(R.id.close2);
        rImage_ = findViewById(R.id.r_image);
        rImage2_ = findViewById(R.id.r_image2);
        wImage = findViewById(R.id.w_image);
        wImage2 = findViewById(R.id.w_image2);
        network = findViewById(R.id.network);
        spin = findViewById(R.id.simpleSpinner);
        taxtext = findViewById(R.id.tax);
        saver = findViewById(R.id.saver);
        vid = findViewById(R.id.videoView);
        min_Vid = findViewById(R.id.min_video);

        a1.setAmount(5000);
        a1.setProfit(8600-5000);
        a1.setVIP(1);
        a1.setName("VIP 1");
        a1.setpReturn(0);
        a1.setDays(7);

        a2.setAmount(15000);
        a2.setProfit(21000-15000);
        a2.setVIP(2);
        a2.setName("VIP 2");
        a2.setpReturn(0);
        a2.setDays(7);

        a3.setAmount(25000);
        a3.setProfit(32000-25000);
        a3.setVIP(3);
        a3.setName("VIP 3");
        a3.setpReturn(0);
        a3.setDays(7);

        a4.setAmount(35000);
        a4.setProfit(43000-35000);
        a4.setVIP(4);
        a4.setName("VIP 4");
        a4.setpReturn(0);
        a4.setDays(7);


        a5.setAmount(55000);
        a5.setProfit(70000-55000);
        a5.setVIP(5);
        a5.setName("VIP 5");
        a5.setpReturn(0);
        a5.setDays(7);

        a6.setAmount(100000);
        a6.setProfit(135000-100000);
        a6.setVIP(6);
        a6.setName("VIP 6");
        a6.setpReturn(0);
        a6.setDays(7);

        a7.setAmount(200000);
        a7.setProfit(350000-200000);
        a7.setVIP(1);
        a7.setName("VVIP 1");
        a7.setpReturn(0);
        a7.setDays(30);

        a8.setAmount(300000);
        a8.setProfit(450000-300000);
        a8.setVIP(2);
        a8.setName("VVIP 2");
        a8.setpReturn(0);
        a8.setDays(30);

        a9.setAmount(400000);
        a9.setProfit(550000-400000);
        a9.setVIP(3);
        a9.setName("VVIP 3");
        a9.setpReturn(0);
        a9.setDays(30);

        a10.setAmount(500000);
        a10.setProfit(650000-500000);
        a10.setVIP(4);
        a10.setName("VVIP 4");
        a10.setpReturn(0);
        a10.setDays(30);

        a11.setAmount(600000);
        a11.setProfit(750000-600000);
        a11.setVIP(5);
        a11.setName("VVIP 5");
        a11.setpReturn(0);
        a11.setDays(30);

        a12.setAmount(700000);
        a12.setProfit(870000-700000);
        a12.setVIP(6);
        a12.setName("VVIP 6");
        a12.setpReturn(0);
        a12.setDays(30);

        //GOLD
        a13.setAmount(1000000);
        a13.setProfit(300000);
        a13.setVIP(7);
        a13.setName("GOLD 1");
        a13.setpReturn(100000);
        a13.setDays(30);

        a14.setAmount(2000000);
        a14.setProfit(500000);
        a14.setVIP(8);
        a14.setName("GOLD 2");
        a14.setpReturn(220000);
        a14.setDays(30);

        a15.setAmount(3000000);
        a15.setProfit(700000);
        a15.setVIP(9);
        a15.setName("GOLD 3");
        a15.setpReturn(300000);
        a15.setDays(30);

        a16.setAmount(4000000);
        a16.setProfit(850000);
        a16.setVIP(10);
        a16.setName("GOLD 4");
        a16.setpReturn(330000);
        a16.setDays(30);

        a17.setAmount(5000000);
        a17.setProfit(1000000);
        a17.setVIP(11);
        a17.setName("GOLD 5");
        a17.setpReturn(500000);
        a17.setDays(30);

        a18.setAmount(10000000);
        a18.setProfit(3000000);
        a18.setVIP(12);
        a18.setName("GOLD 6");
        a18.setpReturn(1000000);
        a18.setDays(30);
        //GOLD END

        amountModels.add(a1);
        amountModels.add(a2);
        amountModels.add(a3);
        amountModels.add(a4);
        amountModels.add(a5);
        amountModels.add(a6);
        amountModels.add(a7);
        amountModels.add(a8);
        amountModels.add(a9);
        amountModels.add(a10);
        amountModels.add(a11);
        amountModels.add(a12);
       /* amountModels.add(a13);
        amountModels.add(a14);
        amountModels.add(a15);
        amountModels.add(a16);
        amountModels.add(a17);
        amountModels.add(a18);*/
        //

        //payment methords
        paymentList = new ArrayList<>();
        payments_view = findViewById(R.id.methods);
        payments_view.setHasFixedSize(true);
        holizontal_LayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        payments_view.setLayoutManager(holizontal_LayoutManager);

        Payment_model receiptPayment = new Payment_model();
        receiptPayment.setActivity(1);
        receiptPayment.setType("Receipt Payment");

        Payment_model usdtPayment = new Payment_model();
        usdtPayment.setActivity(2);
        usdtPayment.setType("USDT Payment");




        paymentList.add(receiptPayment);
        paymentList.add(usdtPayment);

        adapter = new Payment_adapter(getApplicationContext(),paymentList);
        payments_view.setAdapter(adapter);

        Intent i = getIntent();
        balance = i.getFloatExtra("balance",0);
        openState = i.getIntExtra("open",0);



        //Tutorial video

        DatabaseReference video = database.getReference().child("Tutorial");
        video.child("video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String uri = snapshot.getValue().toString();
                    if(vid.isPlaying()){
                        vid.pause();

                    }else{
                        vid.setVideoURI(Uri.parse(uri));
                        vid.start();

                        vid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(vid.getDuration() > 0){
                                    if(vid.isPlaying()){
                                        vid.pause();
                                    }else{
                                        vid.start();

                                    }
                                }else{
                                    Toast.makeText(Recharge.this, "Video not ready please wait", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                if(media == null){
                                    media = mediaPlayer;
                                }
                                if(!mediaPlayer.isPlaying()){
                                    mediaPlayer.start();
                                    mediaPlayer.setVolume(0,0);
                                }
                            }
                        });

                        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                if(media == null){
                                    media = mediaPlayer;
                                }

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    min_screen.setVisibility(View.VISIBLE);full_screen.setVisibility(View.GONE);
                    toggleFullScreen();
            }
        });
        
        min_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    full_screen.setVisibility(View.VISIBLE);min_screen.setVisibility(View.GONE);
                    toggleFullScreen();
            }
        });
        if(media != null){
            media.setVolume(0,0);
            mute.setVisibility(View.GONE);un_mute.setVisibility(View.GONE);
        }
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(media != null){
                    media.setVolume(0,0);
                    un_mute.setVisibility(View.VISIBLE);mute.setVisibility(View.GONE);
                }
            }
        });

        un_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(media != null){
                   // media.setVolume(1,1);
                    mute.setVisibility(View.VISIBLE);un_mute.setVisibility(View.GONE);
                }
            }
        });

        ///
       checkMyinvites();


        networkNames= new String[]{"Chagua","Tigo","Airtel","Zantel","Halotel","TTCL","Vodacom"};
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,networkNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        camount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!camount.getText().toString().isEmpty()){

                            long percent = 4;
                            long amount = Long.parseLong(camount.getText().toString());
                            tax = amount*percent/100;
                            taxtext.setText(String.valueOf("Tsh "+String.valueOf(tax)));

                }

            }
        });

        //myApplication.showAdIfAvailable(this);

        OnHoldTransaction();

        container_recharge = findViewById(R.id.recharge_container);
        container_withdraw = findViewById(R.id.withdraw_container);

        container_withdraw.setVisibility(View.VISIBLE);

        send_w_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Withdraw();
            }
        });

        send_r_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView msg = findViewById(R.id.transactionMessage);
                Recharge_(msg.getText().toString());
            }
        });

        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImage();
            }
        });

        number = findViewById(R.id.info);
        money = findViewById(R.id.wallet_balance);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recharge.setVisibility(View.GONE);
                wid.setVisibility(View.VISIBLE);
                container_recharge.setVisibility(View.VISIBLE);
                container_withdraw.setVisibility(View.GONE);
                View howto = findViewById(R.id.howto);
                howto.setVisibility(View.VISIBLE);
                helpType ="r";
            }
        });
        wid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recharge.setVisibility(View.GONE);
                wid.setVisibility(View.GONE);
                container_withdraw.setVisibility(View.VISIBLE);
                container_recharge.setVisibility(View.GONE);
                View howto = findViewById(R.id.howto);
                howto.setVisibility(View.GONE);
                helpType ="w";
            }
        });

        if(openState == 0){
            recharge.setVisibility(View.GONE);
            container_withdraw.setVisibility(View.VISIBLE);
            container_recharge.setVisibility(View.GONE);
            View howto = findViewById(R.id.howto);
            howto.setVisibility(View.GONE);
            helpType ="w";
        }else{
            wid.setVisibility(View.GONE);
            container_recharge.setVisibility(View.VISIBLE);
            container_withdraw.setVisibility(View.GONE);
            View howto = findViewById(R.id.howto);
            howto.setVisibility(View.VISIBLE);
            helpType ="r";
        }

        uid = auth.uid();
        reference = database.getReference().child("Requests");
        user = database.getReference().child("Users").child(uid);

        user.child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    number.setText("Hello "+snapshot.getValue().toString());
                    user_number = snapshot.getValue().toString();
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
                    money.setText(snapshot.getValue().toString()+" TSH");
                    total_profit = (long) snapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user.child("cash_out_twice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    piramid = Boolean.getBoolean(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user.child("cashed_out").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    cashed_out = Boolean.getBoolean(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //image listeners
        reference2 = database.getReference().child("Instructions");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String b1 = snapshot.child("withdraw").getValue().toString();
                    String b2 = snapshot.child("banner").getValue().toString();

                    Glide.with(getApplicationContext()).load(b1).into(wImage);
                    Glide.with(getApplicationContext()).load(b2).into(wImage2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View howto = findViewById(R.id.howto);
                if(helpType.equals("w")){
                    wc.setVisibility(View.VISIBLE);
                    rc.setVisibility(View.GONE);
                    howto.setVisibility(View.GONE);

                }else {
                    rc.setVisibility(View.VISIBLE);
                    wc.setVisibility(View.GONE);
                    howto.setVisibility(View.VISIBLE);

                }
            }
        });

        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wc.setVisibility(View.GONE);
                vid.pause();
            }
        });
        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rc.setVisibility(View.GONE);
                vid.pause();
            }
        });

        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getSupportActionBar().hide();

    }

    private void toggleFullScreen() {

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) vid.getLayoutParams();
        ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) min_Vid.getLayoutParams();
        Configuration configuration = getResources().getConfiguration();
        int newOrientation = configuration.orientation;
        if (isFullScreen) {
            // Switch back to original size and orientation
            vid.setRotation(0);
            min_Vid.setRotation(0);
            //vid.setLayoutParams(new RelativeLayout.LayoutParams(La 300, 150d));
            layoutParams.width = dpToPx(300);
            layoutParams.height = dpToPx(150);

            min_Vid.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            isFullScreen = false;
        } else {
            // Switch to full-screen size and rotate to landscape
            vid.setRotation(0);
            min_Vid.setRotation(0);
            //configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;

            vid.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            min_Vid.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            isFullScreen = true;
        }
    }
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void Withdraw(){

        String cash = amount.getText().toString();
        TextView n = findViewById(R.id.name__);
        String name = n.getText().toString();
        int c1 = cash.isEmpty() ? 0 : Integer.parseInt(cash);
        limitWithdraw = true;


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Set current time as the base

        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){

            if (cash.isEmpty()) {
                amount.setError("Weka kiasi");
            } else if (wallet_network.isEmpty() || wallet_network.equals("None")) {
                Toast.makeText(this, "Chagua aina ya mtandao", Toast.LENGTH_LONG).show();
            } else if (name.isEmpty()) {
                Toast.makeText(this, "Weka jina la akaunti", Toast.LENGTH_LONG).show();
            }else if (c1 < 2500) {
                Toast.makeText(this, "Kiasi cha chini cha kutoa ni 2500Tsh", Toast.LENGTH_LONG).show();
            } else {

                long c = c1 - tax;
                saver.setVisibility(View.VISIBLE);
                CashOut(c, uid, user_number, wallet_network,name);

            }
        }else{

            if(removeTax){
                if (cash.isEmpty()) {
                    amount.setError("Weka kiasi");
                } else if (wallet_network.isEmpty() || wallet_network.equals("Chagua")) {
                    Toast.makeText(this, "Chagua aina ya mtandao", Toast.LENGTH_LONG).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(this, "Weka jina la akaunti", Toast.LENGTH_LONG).show();
                }else if (c1 < 2500) {
                    Toast.makeText(this, "Kiasi cha chini cha kutoa ni 2500Tsh", Toast.LENGTH_LONG).show();
                } else {

                    long c = c1 - tax;
                    saver.setVisibility(View.VISIBLE);
                    CashOut(c, uid, user_number, wallet_network,name);

                }
            }else{
                Toast.makeText(this, "Unaweza kutoa jumamosi,\n Invite 3 Users to remove limit once they deposit minimum amount of 1500Tsh", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void Recharge_(String msg) {

        TextView transactionBox = findViewById(R.id.transactionMessage);
        String transactionText = msg;
        if(msg.isEmpty()){
            Toast.makeText(this, "Message Cant Be Empty.", Toast.LENGTH_LONG).show();
            saver.setVisibility(View.GONE);
            return;
        }

        if(msg.length() < 70){
            Toast.makeText(this, "Invalid transaction message, Failed to scan", Toast.LENGTH_LONG).show();
            saver.setVisibility(View.GONE);
            return;
        }

        DatabaseReference transactions = database.getReference("Bot").child("Transactions");

        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    boolean valid_id = false;
                    float amount = 0;
                    String a = "";
                    String b = "";
                    saver.setVisibility(View.VISIBLE);

                    for(DataSnapshot snap : snapshot.getChildren()){

                        String admin_id = String.valueOf(snap.child("id").getValue());
                        String user_id = ScanReference(transactionText);

                         a = admin_id.toString();
                         b = user_id.toString();

                        amount = Float.parseFloat(Objects.requireNonNull(snap.child("amount").getValue()).toString());

                        valid_id = a.equals(b);

                        if(valid_id){

                            for(int i = 0;i < amountModels.size();i++){
                                if(amount == amountModels.get(i).getAmount()){
                                    saver.setVisibility(View.VISIBLE);
                                    user.child("profit").setValue(total_profit + (amountModels.get(i).getpReturn()));
                                    break;
                                }
                            }
                            Confirm(b,amount,auth.uid());
                            break;
                        }else{
                            //System.out.println("Admin: " +a+" User : "+b+"    NO");
                        }

                        //System.out.println("ADMIN :"+transaction_id);
                        //System.out.println("USER :"+message_id);

                    }

                    if(!valid_id){
                        HoldTransaction(b,transactionText);
                    }




                }else{
                    Toast.makeText(Recharge.this, "Something went wrong please try later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private String ScanReference(String message){

        String referencePattern = "";
        String reference = "";

        //String[] patterns = new String[]{"^([A-Z0-9]+)","Muamala: (\\d+)","Muamala No: (MP[A-Za-z0-9.]+)", "Kumbukumbu no.: (\\d+)","Kumbukumbu No.: (\\d+)"};
        String[] patterns = new String[]{"Kumbukumbu no.: (\\d+)","(\\w+) Imethibitishwa","Utambulisho wa Muamala: (\\d+)","Muamala No: (MP[A-Za-z0-9.]+)","ID: (MP[A-Za-z0-9.]+)"};

        for (String pattern : patterns){
            referencePattern = pattern;
            Pattern referenceRegex = Pattern.compile(referencePattern);
            Matcher referenceMatcher = referenceRegex.matcher(message);
            if (referenceMatcher.find()) {
                reference = referenceMatcher.group(1);
                // System.out.println("REF :"+reference);
            }
        }
        return reference;
    }
    private void Confirm(String transactionID,float amount,String id){
        saver.setVisibility(View.VISIBLE);
        DatabaseReference transactions = database.getReference("Bot").child("Transactions");
        final DataSnapshot[] transactionSnap = {null};

        Query query = transactions.orderByChild("id").equalTo(transactionID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       float amountRecord = Float.parseFloat(snapshot.child("amount").getValue().toString());
                       transactionSnap[0] = snapshot;
                       boolean used = snapshot.child("used").exists();

                       if(!used){
                           if(amountRecord == amount){

                               float totalRecharge = amount + balance;
                               DatabaseReference user = database.getReference("Users").child(id);
                               //DatabaseReference requests = database.getReference("Requests").child("Recharge");
                               user.child("balance").setValue(totalRecharge).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       user.child("expiration").setValue(new Date().getTime()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                               if(task.isComplete()){
                                                   transactions.child(transactionSnap[0].getKey().toString()).child("used").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isComplete()){
                                                               saver.setVisibility(View.GONE);
                                                               Toast.makeText(getApplicationContext(), "Recharged Tsh"+String.valueOf(amount)+" Successfully", Toast.LENGTH_LONG).show();
                                                               TextView transactionBox = findViewById(R.id.transactionMessage);
                                                               transactionBox.setText("");
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                       });
                                   }
                               });
                           }
                           else{
                               Toast.makeText(getApplicationContext(), "Transaction amount is invalid or fake, contact support", Toast.LENGTH_LONG).show();
                               saver.setVisibility(View.GONE);
                               TextView transactionBox = findViewById(R.id.transactionMessage);
                               transactionBox.setText("");
                           }
                       }else{
                           Toast.makeText(getApplicationContext(), "Transaction expired or used,contact support", Toast.LENGTH_LONG).show();
                           saver.setVisibility(View.GONE);
                           TextView transactionBox = findViewById(R.id.transactionMessage);
                           transactionBox.setText("");
                       }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Transaction history not found, contact support", Toast.LENGTH_LONG).show();
                    saver.setVisibility(View.GONE);
                    TextView transactionBox = findViewById(R.id.transactionMessage);
                    transactionBox.setText("");
                    HoldTransaction(transactionID,transactionBox.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });



    }
    public void OpenImage(){
        saver.setVisibility(View.VISIBLE);
        String amount = amount_added.getText().toString();
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


                    // Create a storage reference from our app
                   UploadImage(imageSelected);
                }
            }
        }
}
    public void UploadImage(Uri image){
    StorageReference storageRef = storage.getReference();

    final StorageReference ref = storageRef.child(image.toString());
   UploadTask uploadTask = ref.putFile(image);

   Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
           // Toast.makeText(Recharge.this, "Please wait", Toast.LENGTH_SHORT).show();
            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                screenshot.setText(downloadUri.toString());
                image_data = downloadUri.toString();
                saver.setVisibility(View.GONE);
            } else {
                // Handle failures
                // ...
            }
        }
    });


}
    public void HoldTransaction(String id,String message)
    {
        if(message.isEmpty()){
            Toast.makeText(this, "Message Cant Be Empty.", Toast.LENGTH_LONG).show();
            saver.setVisibility(View.GONE);
            return;
        }

        HashMap<String,Object> data = new HashMap<>();
        data.put("id",id);
        data.put("message",message);
        data.put("date",String.valueOf(new Date().getTime()));


        reference.child(auth.uid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Recharge.this, "Transaction On-hold \n Update may take up to 24 hours to available", Toast.LENGTH_LONG).show();
                container_withdraw.setVisibility(View.GONE);
                container_recharge.setVisibility(View.GONE);
                saver.setVisibility(View.GONE);
            }
        });
    }
    public void OnHoldTransaction()
    {
        final String[] t_id = {""};
        final String[] t_msg = {""};

        DatabaseReference ref = database.getReference("Requests").child(auth.uid());
        DatabaseReference recheck = database.getReference("Bot").child("Transactions");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    t_id[0] = snapshot.child("id").getValue().toString();
                    t_msg[0] = snapshot.child("message").getValue().toString();
                    View onholdData = findViewById(R.id.pending);
                    TextView id_dat = findViewById(R.id.transaction_id);
                    id_dat.setText(t_id[0]);
                    //onholdData.setVisibility(View.VISIBLE);

                    Query checkTransaction = recheck.orderByChild("id").equalTo(t_id[0]);
                    checkTransaction.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Recharge_(t_msg[0]);
                                Toast.makeText(Recharge.this, "R", Toast.LENGTH_LONG).show();
                                ref.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            Toast.makeText(Recharge.this, "Pending transaction Confirmed", Toast.LENGTH_LONG).show();
                                            View onholdData = findViewById(R.id.pending);
                                            onholdData.setVisibility(View.GONE);
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
                    View onholdData = findViewById(R.id.pending);
                    onholdData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    public void CashOut(long amount,String uid,String number,String net,String user_name)
    {
        String k = reference.child("Withdraw").push().getKey();
        HashMap<String,Object> data = new HashMap<>();
        data.put("amount",amount);
        data.put("id",uid);
        data.put("key",k);
        data.put("number",number);
        data.put("name",user_name);
        data.put("state","pending");
        data.put("network",net);
        data.put("expiration",String.valueOf(new Date().getTime()));

        if(amount <= total_profit){
            long deduct = total_profit - (amount + tax);
            //Toast.makeText(this, String.valueOf(deduct), Toast.LENGTH_SHORT).show();

            user.child("profit").setValue(deduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    reference.child("Withdraw").child(k).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Recharge.this, "Withdraw Successful \n Please Wait It May Take Up To 48h To Receive Money", Toast.LENGTH_SHORT).show();
                            container_withdraw.setVisibility(View.GONE);
                            container_recharge.setVisibility(View.GONE);
                            camount.setText("");
                            user.child("cashed_out").setValue(true);
                            saver.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Invalid Amount", Toast.LENGTH_LONG).show();
            saver.setVisibility(View.GONE);
        }
       
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), networkNames[position], Toast.LENGTH_LONG).show();
        wallet_network = networkNames[position];
        network.setText("Mtandao wa :"+wallet_network);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }

    public String Timer(){

        SimpleDateFormat times = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat mon = new SimpleDateFormat("LLL");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String t = times.format(new Date());
        String m = mon.format(new Date());
        String d = day.format(new Date());
        String y = year.format(new Date());

        String currentDateAndTime = m+" "+String.valueOf(Integer.parseInt(d)+2)+","+y+" "+t;
        Log.d("T",currentDateAndTime);

        return currentDateAndTime;
    }


    public void checkMyinvites() {
        DatabaseReference userRef = database.getReference("Users");
        Query myInvites = userRef.orderByChild("invited").equalTo(auth.uid());
        myInvites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalDeposits = 0;
                saver.setVisibility(View.VISIBLE);
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()) {

                        long balance = Long.parseLong(ds.child("balance").getValue().toString());
                        totalDeposits += balance;
                    }
                    total_invites = snapshot.getChildrenCount();
                    removeTax = total_invites >=3 && totalDeposits >= 4500;
                    TextView limitWarning = findViewById(R.id.withdraw_limit);

                    if(removeTax){
                        limitWarning.setVisibility(View.GONE);
                        tax = 0;
                        taxtext.setText(String.valueOf("Tsh "+String.valueOf(tax)));
                    }else{
                        limitWarning.setVisibility(View.VISIBLE);
                    }
                    saver.setVisibility(View.GONE);
                }else{
                    saver.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                saver.setVisibility(View.GONE);
            }
        });
    }

}