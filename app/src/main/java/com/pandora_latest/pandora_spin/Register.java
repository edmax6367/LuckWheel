package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pandora_latest.Auth.CountryCode;
import com.pandora_latest.QubeAuth.QubeAuth;
import com.pandora_latest.QubeAuth.QubeAuthCallbacks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Register extends AppCompatActivity {

    View need_account,have_account;
    View log_container,reg_container,processing,verification_container;
    View create,login,verify;
    EditText number,password,referal,ver,name;
    EditText lnumber,lpassword;
    TextView resend;

    // [START declare_auth]
    private QubeAuth mAuth;
    private FirebaseDatabase database;
    public StorageReference sr;
    boolean user_exists;

    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    String verified_phone_number = "";

    Uri selectedImage = null;
    String imageLink = "https://cdn3.iconfinder.com/data/icons/vector-icons-6/96/256-1024.png";


    public View name_container,phone_container,password_container;
    public View login_phone_container,login_password_container;
    public View to_number,to_password;
    public View login_to_password;
    public  TextView set_name,set_name_password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        need_account = findViewById(R.id.open_reg);
        have_account = findViewById(R.id.open_log);
        log_container = findViewById(R.id.login_container);
        reg_container = findViewById(R.id.signup_container);
        create = findViewById(R.id.create_btn);
        login = findViewById(R.id.log_btn);
        processing = findViewById(R.id.processing);
        number = findViewById(R.id.num);
        password = findViewById(R.id.pass);
        lnumber = findViewById(R.id.log_num);
        lpassword = findViewById(R.id.log_pass);
        name = findViewById(R.id.name);

        //
        name_container = findViewById(R.id.name_container);
        phone_container = findViewById(R.id.number_container);
        password_container = findViewById(R.id.password_container);

        login_phone_container = findViewById(R.id.number_container_log);
        login_password_container = findViewById(R.id.password_container_log);

        to_number = findViewById(R.id.to_phone);
        to_password = findViewById(R.id.to_pass);
        login_to_password = findViewById(R.id.to_pass_log);

        set_name = findViewById(R.id.set_name);
        set_name_password = findViewById(R.id.set_name_number);

        //

        //verification
        // [START initialize_auth]
        // Initialize Firebase Auth
        database = FirebaseDatabase.getInstance();
        mAuth = new QubeAuth(getApplicationContext(),database,processing);
        // [END initialize_auth]

        Spinner spinner = findViewById(R.id.country_code_spinner);
        Spinner spinner2 = findViewById(R.id.country_code_spinner2);
        sr = FirebaseStorage.getInstance().getReference();

        // Create a list of CountryCode objects
        List<CountryCode> countryCodes = new ArrayList<>();
        countryCodes.add(new CountryCode("TZ", "Tanzania","255"));
        //countryCodes.add(new CountryCode("UG", "Uganda","256"));
        //countryCodes.add(new CountryCode("KN", "Kenya","254"));
        //countryCodes.add(new CountryCode("NG", "Nigeria","234"));
        //countryCodes.add(new CountryCode("SA", "South Africa","27"));
        // Add more country codes here...

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CountryCode> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        Spinner countryCodeSpinner = findViewById(R.id.country_code_spinner);
        Spinner countryCodeSpinner2 = findViewById(R.id.country_code_spinner2);
               // Toast.makeText(Register.this, countryCodes.get(position).getName().toString(), Toast.LENGTH_LONG).show();



        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Get the phone number and the selected CountryCode object
                String phoneNumber = number.getText().toString().trim();
                CountryCode countryCode = (CountryCode) countryCodeSpinner.getSelectedItem();

                // Combine the phone number and the country code
                //phoneNumber = phoneNumber.isEmpty() ?"0": countryCode.getNumberCode() + phoneNumber.substring(1);
                phoneNumber = phoneNumber.isEmpty() ?"0": phoneNumber.substring(0);
                String fullPhoneNumber = phoneNumber;

                if(phoneNumber.length() > 13){
                    number.setError("Invalid Phone Number");
                }else {
                    verified_phone_number = fullPhoneNumber;
                }
            }
        });

        to_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_data = name.getText().toString();
                if(!name_data.isEmpty()){
                    set_name.setText("Hey "+name_data+" Let's Set Up your Phone Number Make Sure to begin with your Country Code eg 255 and do not include 0 currently we support Airtel, Vodacom, Halotel and Tigo");
                    name_container.setVisibility(View.GONE);
                    password_container.setVisibility(View.GONE);
                    phone_container.setVisibility(View.VISIBLE);
                }else{
                    name.setError("Name needed");
                }

            }
        });
        login_to_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!verified_phone_number.isEmpty()){
                    login_password_container.setVisibility(View.VISIBLE);
                    login_phone_container.setVisibility(View.GONE);
                }else{
                    lnumber.setError("Valid Phone number needed");
                }

            }
        });

        to_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!verified_phone_number.isEmpty()){
                    if(!verified_phone_number.contains("255")){
                        number.setError("Country code needed");
                    }
                    else if(verified_phone_number.contains("2550")){
                        number.setError("Do not include 0 after country code");
                    }else{
                        set_name_password.setText("Hey "+name.getText().toString()+" Let's Set Up your Security Pin for "+verified_phone_number.toString()+" Make Sure to create a strong pin or password with 6 or more digits to secure your account.");
                        name_container.setVisibility(View.GONE);
                        password_container.setVisibility(View.VISIBLE);
                        phone_container.setVisibility(View.GONE);
                    }

                }else{
                    number.setError("Phone needed");
                }
            }
        });

        lnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Get the phone number and the selected CountryCode object
                String phoneNumber = lnumber.getText().toString().trim();
                CountryCode countryCode = (CountryCode) countryCodeSpinner2.getSelectedItem();

                // Combine the phone number and the country code
                phoneNumber = phoneNumber.isEmpty() ?"0": "255"+phoneNumber.substring(1);
                String fullPhoneNumber = phoneNumber;

                if(phoneNumber.length() > 13){
                    number.setError("Invalid Phone Number");
                }else {
                    verified_phone_number = fullPhoneNumber;
                }
            }
        });

        //controlling containers
        need_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_container.setVisibility(View.VISIBLE);
                log_container.setVisibility(View.GONE);
                need_account.setVisibility(View.GONE);
                have_account.setVisibility(View.VISIBLE);
            }
        });
        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_container.setVisibility(View.GONE);
                log_container.setVisibility(View.VISIBLE);
                have_account.setVisibility(View.GONE);
                need_account.setVisibility(View.VISIBLE);
            }
        });

        //registration
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number_data = number.getText().toString();
                String password_data = password.getText().toString();
                String name_data = name.getText().toString();
                if(number_data.isEmpty()){
                    number.setError("Number Required");
                }else if(password_data.isEmpty()){
                    password.setError("Password Needed");
                }else if(imageLink == null){
                    Toast.makeText(Register.this, "Tap Image Icon So Set Profile Image", Toast.LENGTH_LONG).show();
                }else if(name_data.isEmpty()){
                    name.setError("Name Needed");
                }
                else if(password_data.length() < 5){
                    Toast.makeText(Register.this, "Password Should Be At Least 6 Characters", Toast.LENGTH_LONG).show();
                }
                else {
                    processing.setVisibility(View.VISIBLE);
                    mAuth.createUser(verified_phone_number,password_data,name_data,imageLink);
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing.setVisibility(View.VISIBLE);
                String number_data = lnumber.getText().toString();
                String password_data = lpassword.getText().toString();
                if(number_data.isEmpty()){
                    lnumber.setError("Number Required");
                }else if(password_data.isEmpty()){
                    lpassword.setError("Password Required");
                }else if(password_data.length() < 5){
                    Toast.makeText(Register.this, "Password Should Be At Least 6 Characters", Toast.LENGTH_LONG).show();
                }
                else {
                    // processing.setVisibility(View.VISIBLE);
                    mAuth.setSignInCallback(new QubeAuthCallbacks.SignInCallback() {
                        @Override
                        public void onSignInSuccess(String userId) {
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            processing.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSignInFailure(String message) {
                            // Handle sign-in failure
                            processing.setVisibility(View.GONE);
                            Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                        }
                    });

                    mAuth.signUser(verified_phone_number,password_data);
                }
            }
        });

    getSupportActionBar().hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            selectedImage = data.getData();
            //Glide.with(getApplicationContext()).load(selectedImage).into(image);

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
                // Toast.makeText(Register.this, "Profile Image Uploading" + progress + "%", Toast.LENGTH_LONG).show();

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
                       // Toast.makeText(Register.this,"Finishing Up", Toast.LENGTH_LONG).show();
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

    @Override
    public void onStart() {
        super.onStart();

    }
}