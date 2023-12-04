package com.pandora_latest.pandora_spin;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.Chat_Room.ChatRoom;
import com.pandora_latest.Chat_Room.TimeManager;
import com.pandora_latest.Customer_Room.CustomerRoom;
import com.pandora_latest.News.bottomAnouncementAdapter;
import com.pandora_latest.PandoraBot.ProductModel;
import com.pandora_latest.QubeAuth.QubeAuth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements RequestListener<Drawable> {
    TextView btnSpin;
    View ivWheel;
    CountDownTimer timer;
    TextView deg,cash,hours;
    View wallet,invite;
    CircleImageView profile;
    private CountDownTimer cdt = null;
    private boolean glideOkay = false;
    private boolean live = false;

    View auth_view,open,cancel,process,network,reset;

    View[] show;

    public RecyclerView news ;
    public ArrayList<String> winners = new ArrayList<>();


    public ArrayList<ProductModel> spin_products = new ArrayList<>();
    private ImageView[] wheelShow;
    public int current_selected = 0;
    private bottomAnouncementAdapter adapter;

    private Button[] vip;
    private ArrayList<Amount_model> amountModels = new ArrayList<>();
    private Button b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17;
    private int lastChangedIndex = 0;
    //private float percent = 0;
    private boolean asset_data;
    private long gift = 0;
    private int randomNumber = 0;
    private float degree = 0;
    private long collectedProduct = 0;
    private static final int TIMER_THRESHOLD = 10;

    FirebaseDatabase database;
    DatabaseReference reference;
    QubeAuth auth;

    String uid;
    String phone;


    int total_invites = 0;
    long total_balance = 0;
    long total_rounds = 20;
    long tokens = 0;
    boolean total_clicks = false;
    long total_profit = 0;

    long startTime;
    String savertime;
    boolean tic = false;
    String secRemaining = "";
    String minRemaining = "";
    String hoursRemaining = "";
    long serverUpTimeSeconds;
    long sec;

    MediaPlayer mp = new MediaPlayer();
    //MediaPlayer mp2 = new MediaPlayer();
    //MediaPlayer won = new MediaPlayer();

    Random random = new Random();

    private final String TAG = "MainActivity";

    public String[] chosenProducts;
    public int[] chosenImages;
    public int bound = 7;
    public Handler handler = new Handler();
    public boolean spinning;
    public int TIME_TO_RELOAD = 3000;

    boolean isReverse = false;

    //NetworkChangeReceiver receiver = new NetworkChangeReceiver();

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

    private TimeManager timeManager;

    int index = 0;
    int count = 0;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing views
        btnSpin = findViewById(R.id.btnSpin);
        ivWheel = findViewById(R.id.ivWheel);
        deg = findViewById(R.id.deg);
        wallet = findViewById(R.id.wallet);
        profile = findViewById(R.id.profile);
        invite = findViewById(R.id.invite);
        auth_view = findViewById(R.id.my_auth);
        open = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);
        cash = findViewById(R.id.cash);
        hours = findViewById(R.id.timer);
        process = findViewById(R.id.waiting);
        network = findViewById(R.id.nett);
        reset = findViewById(R.id.reset);

        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);
        btnSpin.setVisibility(View.GONE);

        b0 = findViewById(R.id.button0);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b10 = findViewById(R.id.button10);
        b11 = findViewById(R.id.button11);
        b12 = findViewById(R.id.button12);
        b13 = findViewById(R.id.button13);
        b14 = findViewById(R.id.button14);
        b15 = findViewById(R.id.button15);
        b16 = findViewById(R.id.button16);
        b17 = findViewById(R.id.button17);

        news = findViewById(R.id.news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        news.setLayoutManager(layoutManager);


        DatabaseReference product_store = database.getReference("Market");
        //product_store.keepSynced(true);

        product_store.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("image").getValue(String.class);

                ProductModel product_data = new ProductModel();
                product_data.setName(name);
                product_data.setImage(image);

                spin_products.add(product_data);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();

                // Iterate over the message_list array
                for (int i = 0; i < spin_products.size(); i++) {
                    ProductModel product_ = spin_products.get(i);

                    // Check if the message has the same key as the removedKey
                    if (product_.getName().equals(removedKey)) {
                        // Remove the item from the array
                        spin_products.remove(i);

                        // Notify the adapter that the item has been removed
                        adapter.notifyItemRemoved(i);
                        break;  // Exit the loop after removing the item
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        news.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; // intercept all touch events and do not pass them down to the RecyclerView's touch handler
            }
        });

        a1.setAmount(1500);
        a1.setProfit(1000);
        a1.setVIP(1);
        a1.setName("VIP 1");
        a1.setpReturn(0);
        a1.setDays(7);

        a2.setAmount(2500);
        a2.setProfit(1500);
        a2.setVIP(2);
        a2.setName("VIP 2");
        a2.setpReturn(0);
        a2.setDays(7);

        a3.setAmount(3500);
        a3.setProfit(2000);
        a3.setVIP(3);
        a3.setName("VIP 3");
        a3.setpReturn(0);
        a3.setDays(7);

        a4.setAmount(4500);
        a4.setProfit(2500);
        a4.setVIP(4);
        a4.setName("VIP 4");
        a4.setpReturn(0);
        a4.setDays(7);


        a5.setAmount(5500);
        a5.setProfit(3000);
        a5.setVIP(5);
        a5.setName("VIP 5");
        a5.setpReturn(0);
        a5.setDays(7);

        a6.setAmount(100000);
        a6.setProfit(25000);
        a6.setVIP(6);
        a6.setName("VIP 6");
        a6.setpReturn(0);
        a6.setDays(30);

        a7.setAmount(250000);
        a7.setProfit(30480);
        a7.setVIP(1);
        a7.setName("VVIP 1");
        a7.setpReturn(0);
        a7.setDays(30);

        a8.setAmount(255000);
        a8.setProfit(64980);
        a8.setVIP(2);
        a8.setName("VVIP 2");
        a8.setpReturn(0);
        a8.setDays(60);

        a9.setAmount(260000);
        a9.setProfit(99990);
        a9.setVIP(3);
        a9.setName("VVIP 3");
        a9.setpReturn(0);
        a9.setDays(90);

        a10.setAmount(550000);
        a10.setProfit(100500);
        a10.setVIP(4);
        a10.setName("VVIP 4");
        a10.setpReturn(0);
        a10.setDays(30);

        a11.setAmount(555000);
        a11.setProfit(201990);
        a11.setVIP(5);
        a11.setName("VVIP 5");
        a11.setpReturn(0);
        a11.setDays(60);

        a12.setAmount(560000);
        a12.setProfit(304920);
        a12.setVIP(6);
        a12.setName("VVIP 6");
        a12.setpReturn(0);
        a12.setDays(90);

        //GOLD
        a13.setAmount(1000000);
        a13.setProfit(300000);
        a13.setVIP(7);
        a13.setName("GOLD 1");
        a13.setpReturn(100000);
        a13.setDays(90);

        a14.setAmount(2000000);
        a14.setProfit(500000);
        a14.setVIP(8);
        a14.setName("GOLD 2");
        a14.setpReturn(220000);
        a14.setDays(90);

        a15.setAmount(3000000);
        a15.setProfit(700000);
        a15.setVIP(9);
        a15.setName("GOLD 3");
        a15.setpReturn(300000);
        a15.setDays(90);

        a16.setAmount(4000000);
        a16.setProfit(850000);
        a16.setVIP(10);
        a16.setName("GOLD 4");
        a16.setpReturn(330000);
        a16.setDays(90);

        a17.setAmount(5000000);
        a17.setProfit(1000000);
        a17.setVIP(11);
        a17.setName("GOLD 5");
        a17.setpReturn(500000);
        a17.setDays(90);

        a18.setAmount(10000000);
        a18.setProfit(3000000);
        a18.setVIP(12);
        a18.setName("GOLD 6");
        a18.setpReturn(1000000);
        a18.setDays(90);
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
        amountModels.add(a13);
        amountModels.add(a14);
        amountModels.add(a15);
        amountModels.add(a16);
        amountModels.add(a17);
        amountModels.add(a18);


        timeManager = new TimeManager(auth.uid());
        timeManager.startUpdatingTime();

        DatabaseReference all = database.getReference("Users");

        all.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                if(snapshot.child("name").exists()) {
                    String numberData = snapshot.child("number").getValue(String.class);
                    String profitData = Objects.requireNonNull(snapshot.child("profit").getValue()).toString();
                    String balanceData = Objects.requireNonNull(snapshot.child("balance").getValue()).toString();

                    if(numberData != null && numberData.length() >= 6){
                        int index = numberData.length()-4;
                        numberData = numberData.substring(0,4)+"****"+numberData.substring(index);
                    }
                    String finalData = "";

                    if(Long.parseLong(profitData) > 0){
                        finalData = numberData+" Gained Profit Of "+String.format(Locale.US,"%,d", Integer.parseInt(profitData))+"Tsh";
                    }else{
                        if(Long.parseLong(balanceData) >= 1500){
                            finalData = numberData+" Deposited "+String.format(Locale.US,"%,d", Integer.parseInt(balanceData))+"Tsh";
                        }
                    }


                    if(Long.parseLong(profitData) > 0){
                        winners.add(finalData);
                        count++;
                        if (count % 2 == 0) {
                            winners.add("Remove limitations by inviting");
                        }
                    }
                    //Toast.makeText(MainActivity.this, String.valueOf(index)+": "+String.valueOf(numberData), Toast.LENGTH_SHORT).show();

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

        //check for update
        DatabaseReference app__data = database.getReference("Apk");
        app__data.child("v").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int versionCode = snapshot.getValue(Integer.class);
                    // Get the current version code
                    int currentVersionCode = getAppVersionCode(getApplicationContext());

                    if(currentVersionCode < versionCode){
                        showUpdateDialogg();
                    }else{
                        if(dialog != null){
                            dialog.cancel();
                        }
                    }

                }else{


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new bottomAnouncementAdapter(winners);
        news.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable updateNews = new Runnable() {

            @Override
            public void run() {
                if(winners.size() > 0){
                    // adapter.notifyItemRangeChanged(0,winners.size());
                    news.smoothScrollToPosition(index);
                    index = (index + 1) % winners.size();
                }

                if (index == winners.size() - 1) {
                    isReverse = true;
                   // Toast.makeText(MainActivity.this, "Reversing", Toast.LENGTH_SHORT).show();
                } else if (index == 0) {
                    isReverse = false;
                }

                layoutManager.setReverseLayout(isReverse);
                news.smoothScrollToPosition(index);
                //index = (isReverse) ? index - 1 : index + 1;

                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(updateNews,2000);

        TextView about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),About.class);
                startActivity(i);
            }
        });

        ImageView samp;
        wheelShow = new ImageView[]{
                samp = findViewById(R.id.flash),
                samp = findViewById(R.id.bag),
                samp = findViewById(R.id.camera),
                samp = findViewById(R.id.watch),
                samp = findViewById(R.id.phones),
                samp = findViewById(R.id.iphone)
        };

        SetProducts(current_selected,6);
        _startAnimation();

        ReloadProducts();

        vip = new Button[]{b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17};

        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinning = true;

                if(isLogged()){

                   // if(asset_data){
                        if(total_balance >= a1.getAmount()) {
                            // disabling the button so that user
                            // should not click on the button
                            // while the wheel is spinning
                            btnSpin.setEnabled(false);
                            PlaySpin();

                            // Generate a random stopping point between 0 and 360 degrees
                            Random random = new Random();
                            int stopAngle = random.nextInt(360);

                            int spin = random.nextInt(60) + 10;
                            int spinDuration = 4000; // 4 seconds

                            float fromDegree = 0;
                            float toDegree = 360 * 6 + stopAngle; // 6 divisions

                            ObjectAnimator animator = ObjectAnimator.ofFloat(ivWheel, "rotation", fromDegree, toDegree);
                            animator.setDuration(spinDuration);
                            animator.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator.start();

                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float animatedValue = (float) animation.getAnimatedValue("rotation");
                                    deg.setText(String.valueOf(animatedValue % 360)); // Display the current degree
                                }
                            });

                            animator.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    // Disable the spin button during the animation
                                    btnSpin.setEnabled(false);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // Re-enable the spin button after the animation
                                    btnSpin.setEnabled(true);

                                    // Calculate the result based on the final degree
                                    float finalDegree = (float) ivWheel.getRotation() % 360;
                                    int division = (int) (finalDegree / 60);

                                    // Determine the result based on the division
                                    switch (division) {
                                        case 0:
                                            isElligible_(100, current_selected);
                                            break;
                                        case 1:
                                            isElligible_(100, current_selected);
                                            break;
                                        case 2:
                                            isElligible_(800, current_selected);
                                            break;
                                        case 3:
                                            isElligible_(800, current_selected);
                                            break;
                                        case 4:
                                            isElligible_(500, current_selected);
                                            break;
                                        case 5:
                                            isElligible_(500, current_selected);
                                            break;
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    // Handle animation cancellation if needed
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                    // Handle animation repeat if needed
                                }
                            });
                        }
                        else {

                               // Toast.makeText(MainActivity.this, "No Enough Assert To Spin \n You Need At least 5000 Tsh In Your Assert ", Toast.LENGTH_SHORT).show();
                                Note2("No Enough Balance","You Cannot Spin Due To Low Or Empty Balance ,Upgrade Your Balance To At least 1500Tsh To Receive VIP 1");
                        }


                }else{
                   auth_view.setVisibility(View.VISIBLE);
                }

            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //_openWhatsApp("+1(707) 731-6403","Hello Customer Service");
                _openWhatsApp("+255719755701","Hello Customer Service");
            }
        });

        ivWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogged()){
                    //auth_view.setVisibility(View.VISIBLE);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogged()){
                    auth_view.setVisibility(View.VISIBLE);
                }else {
                    ShowAd(Profile.class);

                }
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogged()){
                    auth_view.setVisibility(View.VISIBLE);
                }else {
                   /* String text = auth.getUid();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Invite Code", text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Invitation Code Copied Use It To Invite Others.", Toast.LENGTH_LONG).show();

                    */
                    View view = findViewById(R.id.invite_info);
                    View invite = findViewById(R.id.confirm_btn);
                    view.setVisibility(View.VISIBLE);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    invite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view.setVisibility(View.GONE);
                            getLink();
                        }
                    });

                       //


                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth_view.setVisibility(View.GONE);
            }
        });
        auth_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth_view.setVisibility(View.GONE);
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAd(Register.class);
            }
        });
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogged()){
                    //ShowAd(Recharge.class);

                }else {
                    //auth_view.setVisibility(View.VISIBLE);
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "No Service Available Right Now", Toast.LENGTH_LONG).show();
            }
        });
        try {
            PlayBackground();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSupportActionBar().hide();

        //checking if we have values
        if(isLogged() && !auth.uid().equals("OUT")){
            reference = database.getReference().child("Users").child(auth.uid());

            String number = phone;
            int balance = 0;
            String invite = "0";

            CheckBalance();
            InvitationListener();

            View abt = findViewById(R.id.abt);
            abt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),About.class);
                    startActivity(i);
                }
            });
            View cc = findViewById(R.id.cc);
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ChatRoom.class);
                    startActivity(i);
                }
            });

            View rr = findViewById(R.id.rr);
            rr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Recharge.class);
                    i.putExtra("balance",balance);
                    i.putExtra("open",1);
                    startActivity(i);
                }
            });


            View vv = findViewById(R.id.rr);
            vv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Recharge.class);
                    i.putExtra("balance",balance);
                    i.putExtra("open",1);
                    startActivity(i);
                }
            });View ww = findViewById(R.id.ww);
            ww.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Recharge.class);
                    i.putExtra("balance",balance);
                    i.putExtra("open",0);
                    startActivity(i);
                }
            });
            reference.child("number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("number").setValue(number);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("gift").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        gift = snapshot.getValue(Long.class);
                        if(gift > 0){
                            //Note("Bonus","You have a welcome bonus of 5000Tsh Use it for five days before its expiration. \n This Applies To New Users Whom Haven't Deposited Only \n The Offer Begun May 14, 2023");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("balance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("balance").setValue(balance);
                    }else {
                       total_balance = (long) snapshot.getValue();

                       if(total_balance < a1.getAmount()){
                            reference.child("rounds").setValue(0);
                            reference.child("tokens").setValue(0);
                       }else{
                           SetVIP(total_balance);
                       }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("tokens").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("tokens").setValue(0);
                    }else {
                       tokens = (long) snapshot.getValue();


                        if(lastChangedIndex < 5){
                            if(tokens == 7){
                                //transfer balance to profit and set balance 0
                                reference.child("profit").setValue((total_balance+total_profit)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("balance").setValue(0);
                                        reference.child("tokens").setValue(0);
                                    }
                                });
                            }
                        }
                        else{
                            if(tokens == 30 && lastChangedIndex == 6 ||tokens == 30 && lastChangedIndex == 10){
                                //transfer balance to profit and set balance 0

                                reference.child("profit").setValue((total_balance+total_profit)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("balance").setValue(0);
                                        reference.child("tokens").setValue(0);
                                    }
                                });
                            }else if(tokens == 60 && lastChangedIndex == 8 || tokens == 60 && lastChangedIndex == 11){
                                reference.child("profit").setValue((total_balance+total_profit)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("balance").setValue(0);
                                        reference.child("tokens").setValue(0);
                                    }
                                });
                            }else if(tokens == 90 && lastChangedIndex == 9 || tokens == 90 && lastChangedIndex == 12){
                                reference.child("profit").setValue((total_balance+total_profit)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("balance").setValue(0);
                                        reference.child("tokens").setValue(0);
                                    }
                                });
                            }
                        }

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
                        Glide.with(getApplicationContext()).load(snapshot.getValue(String.class)).into(profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            reference.child("rounds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("rounds").setValue(total_rounds).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    network.setVisibility(View.GONE);
                                }
                            }
                        });
                    }else {
                        total_rounds = (long) snapshot.getValue();
                        btnSpin.setText("SPIN");
                        network.setVisibility(View.GONE);
                        btnSpin.setVisibility(View.VISIBLE);

                        if(total_rounds == 0 && total_balance >= a1.getAmount()){
                           // reference.child("balance").setValue(total_balance-5000);
                           // Toast.makeText(getApplicationContext(), "Assert Used", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            reference.child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("products").setValue(collectedProduct);
                    }else {
                        collectedProduct = (long) snapshot.getValue();
                        TextView collection = findViewById(R.id.product);
                        if(collectedProduct > 1){
                            collection.setText(collectedProduct +" Products Collected.");
                        }else {
                            collection.setText(collectedProduct +" Product Collected.");
                        }

                        if(collectedProduct >= 10){
                           // PauseHour();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("clicks").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("clicks").setValue(false);
                    }else {
                       total_clicks = Boolean.getBoolean(snapshot.getValue().toString());
                        if(total_clicks){
                            btnSpin.setClickable(false);
                            btnSpin.setVisibility(View.GONE);
                            reset.setVisibility(View.VISIBLE);
                            //PauseHour();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("milliseconds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("milliseconds").setValue("Jan 9,2023 14:48:07");
                    }else {
                        savertime = snapshot.getValue().toString();
                        PauseHour();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("profit").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference.child("profit").setValue(total_profit);
                        process.setVisibility(View.GONE);
                    }else {
                        total_profit = (long) snapshot.getValue();
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                        String formattedProfit = decimalFormat.format(total_profit) + " TSH";
                        cash.setText(formattedProfit);
                        process.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //checking number of invites
            reference.child("invite").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    total_invites++;
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

                }
            });
            //
        }else {
            auth_view.setVisibility(View.VISIBLE);
        }


    }
    public boolean isLogged(){

        AccountStatus();
        updateStatus();

        String id = "";
        boolean logged = false;

        SharedPreferences datas = getSharedPreferences("qube_auth",0);
        id = datas.getString("auth","OUT");

        if(!id.equals("OUT")){
            logged = true;
        }

        return logged;
    }
    public void AccountStatus(){
        DatabaseReference r = database.getReference("Users").child(auth.uid());
        r.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getLink(){
         String urlToShare = "https://pandoraspin.com/#/i/"+auth.uid();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
        startActivity(Intent.createChooser(intent, "Invite via"));
    }
    public void isElligible_(long coins,int index){

        Promote_(index,spin_products.get(index).getName());
        spinning = false;

        UpdateCollection(collectedProduct);
    }
    public void UpdateCollection(long total){
        TextView collection = findViewById(R.id.product);

        if(total == TIMER_THRESHOLD){
            collection.setText(10 +" Products Collected.");
            reference.child("products").setValue(10);
            long token = tokens += 1;
            reference.child("tokens").setValue(token);
            Toast.makeText(this, "Wait 24h To collect Again", Toast.LENGTH_LONG).show();
            Timer();

            if(!total_clicks){

                reference.child("profit").setValue(total_profit + (amountModels.get(lastChangedIndex).getProfit()/amountModels.get(lastChangedIndex).getDays()));
                reference.child("clicks").setValue(true);
                reference.child("products").setValue(0);

            }
            collectedProduct = 0;
            return;
        }
        if(total > 1){
            collection.setText(total +" Products Collected.");
            reference.child("products").setValue(total);

        }else {
            collection.setText(total +" Product Collected.");
            reference.child("products").setValue(total);
        }
    }
    public void PlayBackground() throws IOException {
        //mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/luck-wheel-56797.appspot.com/o/mixkit-game-show-fun-suspense-942.wav?alt=media&token=84f55c32-cfe7-4b05-826d-87b62ea84114");
       // mp = MediaPlayer.create(this, R.raw.background_classic);
       // mp.start();
       // mp.setVolume(1f,1f);
       // mp.setLooping(true);

    }
    public void StopBackground(){
       // mp.pause();
       // mp.stop();
       // mp.release();
        mp = null;
    }
    public void PlaySpin(){
       // mp2 = MediaPlayer.create(this, R.raw.spin);
        //mp.prepare();
        //mp2.start();
        // mp2.setVolume(0f,0f);
       // mp2.setLooping(true);
    }
    public void ShowAd(Class activity){

            Intent i = new Intent(getApplicationContext(),activity);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
    }
    public void Timer(){

        //Toast.makeText(MainActivity.this, "Initializing...", Toast.LENGTH_LONG).show();
        btnSpin.setClickable(false);
        btnSpin.setVisibility(View.GONE);
        reset.setVisibility(View.VISIBLE);

        SimpleDateFormat times = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat mon = new SimpleDateFormat("LLL");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String t = times.format(new Date());
        String m = mon.format(new Date());
        String d = day.format(new Date());
        String y = year.format(new Date());

        String currentDateAndTime = m+" "+String.valueOf(Integer.parseInt(d)+1)+","+y+" "+t;
        Log.d("T",currentDateAndTime);
        reference.child("milliseconds").setValue(currentDateAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    reset.setVisibility(View.VISIBLE);
                    PauseHour();
                }
            }
        });
    }
    public void PauseHour(){

        tic = true;
        String time_in_server = savertime;
        long future = new InnoDate(time_in_server).getInnoTime();
        long now = new Date().getTime();
        long time = future - now;

        cdt = new CountDownTimer(time,1) {
            @Override
            public void onTick(long t) {

                long mHour = (t%(1000 * 60 * 60 * 24))/(1000 * 60 * 60);
                long mMin = (t % (1000 * 60 * 60)) / (1000 * 60);
                long mSec = (t % (1000 * 60)) / 1000;

                hours.setText(String.format("%02d",mHour) +":"+String.format("%02d",mMin)+":"+String.format("%02d",mSec));
                if(t > 10000){
                     reference.child("clicks").setValue(true);
                     btnSpin.setVisibility(View.GONE);
                     reset.setVisibility(View.VISIBLE);
                   // Toast.makeText(MainActivity.this, "t", Toast.LENGTH_SHORT).show();
                }else {

                   reference.child("clicks").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                              //  Toast.makeText(MainActivity.this, "c", Toast.LENGTH_SHORT).show();
                                tic = false;
                                btnSpin.setVisibility(View.VISIBLE);
                                reset.setVisibility(View.GONE);
                                btnSpin.setClickable(true);
                                reference.child("products").setValue(0);
                            }
                        }
                    });

                    tic = false;
                    btnSpin.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.GONE);
                    btnSpin.setClickable(true);
                    reference.child("products").setValue(0);
                    reference.child("clicks").setValue(false);
                }



            }

            @Override
            public void onFinish() {
                if(collectedProduct >= 10){
                    //Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_LONG).show();

                    reference.child("clicks").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                               // Toast.makeText(MainActivity.this, "c", Toast.LENGTH_SHORT).show();
                                tic = false;
                                btnSpin.setVisibility(View.VISIBLE);
                                reset.setVisibility(View.GONE);
                                btnSpin.setClickable(true);
                                reference.child("products").setValue(0);


                            }
                        }
                    });


                }

                //Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();

            }
        };
            cdt.start();

    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mp.isPlaying()){

            mp.pause();

        }
    }
    @Override
    protected void onStart() {
        checkWorkingDaysAndTime();
        try {
            PlayBackground();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(!mp.isPlaying()){
            if(mp != null){
                mp.start();
            }
        }
        updateStatus();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
       StopBackground();
    }
    public class InnoDate {

        private long time_24h;

        public InnoDate(String time) {
            // Split the string by space and comma
            String[] parts = time.split(" |,");

            // Extract the year, month, day, hour, minute, and second values
            int year = Integer.parseInt(parts[2]);
            int month = getInnoMonth(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int hour = Integer.parseInt(parts[3].split(":")[0]);
            int minute = Integer.parseInt(parts[3].split(":")[1]);
            int second = Integer.parseInt(parts[3].split(":")[2]);

            // Calculate the time in milliseconds
            long future = new GregorianCalendar(year, month, day, hour, minute, second).getTimeInMillis();
           // long now = System.currentTimeMillis();
            time_24h = future;

        }

        // Returns the month number (0-11) for the given month name
        private int getInnoMonth(String monthName) {
            switch (monthName) {
                case "Jan": return 0;
                case "Feb": return 1;
                case "Mar": return 2;
                case "Apr": return 3;
                case "May": return 4;
                case "Jun": return 5;
                case "Jul": return 6;
                case "Aug": return 7;
                case "Sep": return 8;
                case "Oct": return 9;
                case "Nov": return 10;
                case "Dec": return 11;
            }
            return -1;
        }

        private long getInnoTime(){
            return time_24h;
        }

    }
    private void _openWhatsApp(String numero,String mensaje) {

        Intent i = new Intent(getApplicationContext(), CustomerRoom.class);
        startActivity(i);
    }
    private void CheckBalance(){
        DatabaseReference ref = database.getReference().child("Users").child(auth.uid());
        ref.keepSynced(true);
        ref.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String b = snapshot.getValue().toString();
                    long v = Long.valueOf(b);
                    ValidateVIP(v);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ValidateVIP(long asset){

        for(int i = 0; i < vip.length;i++){

            int finalI = i;
            vip[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(amountModels.get(finalI).getName().equals("GOLD 1")){
                        Toast(
                                finalI,
                                " Tsh"+String.format(Locale.US,"%,d",amountModels.get(finalI).getAmount())+" Deposit",
                                " Tsh"+String.format(Locale.US,"%,d",(amountModels.get(finalI).getProfit()+ amountModels.get(finalI).getAmount()))+" Profit",
                                " "+amountModels.get(finalI).getDays()+"Days return",
                                " Tsh"+String.format(Locale.US,"%,d",amountModels.get(finalI).getpReturn())+" Instant Return",
                                " ONE BENEFIT OF JOINING THE GOLDEN CLUB YOU GET PROFIT RETURN INSTANTLY AND WHILE YOU CONTINUE TO WAIT FOR BIG PROFITS IN 90 DAYS"
                        );
                    }else if(amountModels.get(finalI).getName().contains("GOLD")){
                        Toast(
                                finalI,
                                " Tsh"+String.format(Locale.US,"%,d",amountModels.get(finalI).getAmount())+" Deposit",
                                " Tsh"+String.format(Locale.US,"%,d",(amountModels.get(finalI).getProfit()+ amountModels.get(finalI).getAmount()))+" Profit",
                                " "+amountModels.get(finalI).getDays()+"Days return",
                                " Tsh"+String.format(Locale.US,"%,d",amountModels.get(finalI).getpReturn())+" Instant Return",
                                " ONE BENEFIT OF JOINING THE GOLDEN CLUB YOU GET PROFIT RETURN INSTANTLY AND WHILE YOU CONTINUE TO WAIT FOR BIG PROFITS IN 90 DAYS"
                        );
                    }else{
                        Toast(
                                finalI,
                                "Tsh"+String.format(Locale.US,"%,d",amountModels.get(finalI).getAmount())+" Deposit",
                                "Tsh"+String.format(Locale.US,"%,d",(amountModels.get(finalI).getProfit()+ amountModels.get(finalI).getAmount()))+" Profit",
                                amountModels.get(finalI).getDays()+"Days return",
                                "",
                                ""
                        );
                    }
                   // Toast(finalI,"Level "+amountModels.get(finalI).getName()+" \n Deposit Amount Required is "+String.format(Locale.US,"%,d",amountModels.get(finalI).getAmount())+"Tsh At the end of "+amountModels.get(finalI).getDays()+"days you will generate a profit of "+String.format(Locale.US,"%,d",amountModels.get(finalI).getProfit())+"Tsh,\n A Total Profit Of "+String.format(Locale.US,"%,d",(amountModels.get(finalI).getProfit()+ amountModels.get(finalI).getAmount()))+"Tsh\nCAUTION: The deposit amount should not exceed the Level Limit");
                }
            });
        }
    }
    private void SetVIP(long current_amount) {

        TextView l = findViewById(R.id.level);

        for (int i = 0 ;i<amountModels.size();i++){
            if(current_amount == amountModels.get(i).getAmount()){
                lastChangedIndex = i;
                break;
            }
        }
        l.setText(amountModels.get(lastChangedIndex).getName());

    }
    private void Toast(int lastChangedIndex,String a0,String a1,String a2,String a3,String a4){
        View pannel = findViewById(R.id.my_vip);
        TextView vipType = findViewById(R.id.viptype);
        TextView z0 = findViewById(R.id.z0);
        TextView z1 = findViewById(R.id.z1);
        TextView z2 = findViewById(R.id.z2);
        TextView z3 = findViewById(R.id.z3);
        TextView k0 = findViewById(R.id.k0);
        TextView k1 = findViewById(R.id.k1);
        Button close = findViewById(R.id.close);

        int level = lastChangedIndex + 1;
        pannel.setVisibility(View.VISIBLE);
        vipType.setText(amountModels.get(lastChangedIndex).getName());

        z0.setText(a0);
        z1.setText(a1);
        z2.setText(a2);

        if(a3.isEmpty()){
            z3.setVisibility(View.GONE);
        }else {
            z3.setVisibility(View.VISIBLE);
        }
        if(a4.isEmpty()){
            k1.setVisibility(View.GONE);
        }else{
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final int screenWidth = displayMetrics.widthPixels * 4;

            final TranslateAnimation translateAnimation = new TranslateAnimation(0, -screenWidth, 0, 0);
            translateAnimation.setDuration(10000*4);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            k1.setVisibility(View.VISIBLE);
            k1.startAnimation(translateAnimation);

        }
        z3.setText(a3);
        k1.setText(a4);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pannel.setVisibility(View.GONE);
            }
        });
    }
    private void Note2(String header,String info){
        View pannel = findViewById(R.id.bal);
        TextView info_1 = findViewById(R.id.info_1);
        TextView info_2 = findViewById(R.id.info_2);
        Button c = findViewById(R.id.ccc);

        pannel.setVisibility(View.VISIBLE);
        info_1.setText(header);
        info_2.setText(info);
        //vipInfo.setText(info);

        if(info.isEmpty()){
            info_2.setVisibility(View.GONE);
        }else{
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final int screenWidth = displayMetrics.widthPixels * 4;

            final TranslateAnimation translateAnimation = new TranslateAnimation(0, -screenWidth, 0, 0);
            translateAnimation.setDuration(10000*4);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            info_2.setVisibility(View.VISIBLE);
            //info_2.startAnimation(translateAnimation);

        }

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pannel.setVisibility(View.GONE);
            }
        });
    }
    private void Promote_(int index,String name){
        View pannel_ = findViewById(R.id.my_collection);
        ImageView Pimage = findViewById(R.id.image);
        TextView vipInfo = findViewById(R.id.name);
        TextView views = findViewById(R.id.views);
        TextView pp = findViewById(R.id.promo);
        Button cls = findViewById(R.id.close_);

        views.setText("Viewed by "+ random+"k");
        pannel_.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(spin_products.get(index).getImage()).into(Pimage);
        vipInfo.setText(name+" Collected");
        pp.setText("You've Earned Profit By Advertising Our Product to reach more customers");
        final TextView promoTextView = findViewById(R.id.promo);
        promoTextView.setSelected(true);

        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;

        final TranslateAnimation translateAnimation = new TranslateAnimation(screenWidth, -screenWidth, 0, 0);
        translateAnimation.setDuration(10000);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        pp.startAnimation(translateAnimation);

        pannel_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pannel_.setVisibility(View.GONE);
                // Toast.makeText(MainActivity.this, "Save To Exit", Toast.LENGTH_LONG).show();
            }
        });

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Toast.makeText(MainActivity.this, "Press Save To Exit", Toast.LENGTH_LONG).show();
                cls.setText("Save");
                pannel_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pannel_.setVisibility(View.GONE);
                       // Toast.makeText(MainActivity.this, "Save To Exit", Toast.LENGTH_LONG).show();
                    }
                });

                cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pannel_.setVisibility(View.GONE);
                        collectedProduct++;
                        UpdateCollection(collectedProduct);
                    }
                });
            }
        });
    }
    private void SetProducts(int startIndex, int count){

        if(spin_products.isEmpty()){

            return;
        }

        /*if (spin_products.size() % 6 != 0) {
            // Handle the case where the number of items is not a multiple of 6
            current_selected = 0;
            //SetProducts(current_selected, 6);
            return;
        }*/

        View resourcesLoader = findViewById(R.id.resources_loading);

        if(glideOkay){
            resourcesLoader.setVisibility(View.GONE);
        }else{
            resourcesLoader.setVisibility(View.VISIBLE);
        }


        if (startIndex >= spin_products.size()) {
            // Handle the case where the startIndex is out of bounds.
            current_selected = 0;
            SetProducts(current_selected, 6);
            return;
        }

        for (int i = 0; i < count && i < wheelShow.length && startIndex < spin_products.size(); i++) {
           // Drawable image = getResources().getDrawable(spin_products.get(startIndex).getImage());
            //wheelShow[i].setImageDrawable(image);
            String image = spin_products.get(startIndex).getImage();
            Glide.with(getApplicationContext()).load(image).listener(this).into(wheelShow[i]);
            startIndex++;
        }
    }
    private void _startAnimation() {

        for(ImageView image : wheelShow){

            ObjectAnimator flashAnimator = ObjectAnimator.ofFloat(image, "rotationY", -45f, 45f);
            flashAnimator.setDuration(5000);
            flashAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            flashAnimator.setInterpolator(new LinearInterpolator());
            flashAnimator.start();
        }


    }
    private void ReloadProducts() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (spinning) {
                    handler.postDelayed(this, TIME_TO_RELOAD); // post this runnable again after 3 seconds
                    return;
                }

                // Increment the index
                current_selected += 6;
                SetProducts(current_selected, 6);

                // If we've reached the end of the array, reset the index
                if (current_selected >= spin_products.size()) {
                    current_selected = 0;
                }

                handler.postDelayed(this, TIME_TO_RELOAD); // post this runnable again after 3 seconds
            }
        };

        handler.postDelayed(runnable, TIME_TO_RELOAD); // post the runnable to the handler to start the loop
    }
    public void checkWorkingDaysAndTime(){

        // Get the current day of the week
      /*  Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        View v = findViewById(R.id.work);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Check if it's a weekend day (Saturday or Sunday)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.MONDAY) {
           // Toast.makeText(this, "System is unavailable", Toast.LENGTH_LONG).show();
            //v.setVisibility(View.VISIBLE);
        } else {
            // Check if it's within working hours (10:00 to 19:30)
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 10);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);

            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 25);
            endTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() >= startTime.getTimeInMillis() && calendar.getTimeInMillis() <= endTime.getTimeInMillis()) {
                //v.setVisibility(View.GONE);

            } else {
               // v.setVisibility(View.VISIBLE);

            }
        }*/
    }
    public void updateStatus() {
    }
    public void InvitationListener() {

        final long[] my_net_balance = {0};
        long[] total_invites = {0};
        DatabaseReference reference;


        reference = database.getReference().child("Users").child(auth.uid());

        reference.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_net_balance[0] = snapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        DatabaseReference invitedUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child("invite").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot ds: snapshot.getChildren()) { // Fixed typo: getChildrean() to getChildren()
                    String key = ds.getKey();
                    final long[] reward = {0};
                    invitedUserReference.child(key).child("balance").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                long userBalance = snapshot.getValue(Long.class);
                                reward[0] = userBalance == 1500 ? 200 : (userBalance > 1500 ? 2500 : 0);
                                reference.child("balance").setValue(my_net_balance[0] + reward[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Handle onComplete if needed
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle onChildChanged if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                total_invites[0] = snapshot.getChildrenCount();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle onChildMoved if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void showUpdateDialogg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Available")
                .setMessage("A new version of Pandora Spin is available. Please update to the latest version for bug fixes and new features.")
                .setPositiveButton("Update", (dialog, which) -> launchAppStore())
                .setCancelable(false); // Set dialog as not cancelable


        dialog = builder.create();
        dialog.show();

    }
    private void launchAppStore() {
        final String appPackageName = getPackageName();
        try {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://pandoraspin.com/#/home-update/")));
        } catch (android.content.ActivityNotFoundException e) {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://pandoraspin.com/#/home/")));
        }
    }

    private int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        glideOkay = true;
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
       glideOkay = true;
        return false; // Return false to allow Glide to display the resource
    }

}