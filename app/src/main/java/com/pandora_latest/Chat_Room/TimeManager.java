package com.pandora_latest.Chat_Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class TimeManager {

    private static final String TAG = "TimeManager";
    private DatabaseReference timeRef;

    private Handler handler;
    private Runnable timeUpdater;



    public TimeManager(String userId) {
        if (!Objects.equals(userId, "OUT")) {
            timeRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("online");
        }
    }

    public void startUpdatingTime() {
        handler = new Handler(Looper.getMainLooper());
        timeUpdater = new Runnable() {
            @Override
            public void run() {
                if (timeRef != null) {
                    long currentTime = System.currentTimeMillis();
                    timeRef.setValue(currentTime);
                } else {
                    Log.e(TAG, "User is not authenticated.");
                }

                handler.postDelayed(this, 5000); // Update every 5sec
            }
        };

        handler.post(timeUpdater);
    }

    public void stopUpdatingTime() {
        if (handler != null && timeUpdater != null) {
            handler.removeCallbacks(timeUpdater);
        }
    }

}
