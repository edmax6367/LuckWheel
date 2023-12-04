package com.pandora_latest.Chat_Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.QubeAuth.QubeAuth;
import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    private ArrayList<RoomModel> message_list = new ArrayList<>();
    RoomAdapter adapter;
    RecyclerView message_view;
    private LinearLayoutManager vertical_layout;

    private FirebaseDatabase database;
    QubeAuth auth;

    View send_btn;
    EditText message_box;
    private ItemTouchHelper itemTouchHelper;

    String content = "";
    String content_key = "";

    private TimeManager timeManager;
    public TextView all,online;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        message_view = findViewById(R.id.messages);
        send_btn = findViewById(R.id.send_btn);
        message_box = findViewById(R.id.message_box);
        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);

        all = findViewById(R.id.all_members);
        online = findViewById(R.id.online_members);

        message_view.setHasFixedSize(true);
        vertical_layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        message_view.setLayoutManager(vertical_layout);

        // Create and attach the swipe-to-reply callback
        itemTouchHelper = new ItemTouchHelper(new SwipeToReplyCallback(adapter));
        itemTouchHelper.attachToRecyclerView(message_view);

        timeManager = new TimeManager(auth.uid());
        timeManager.startUpdatingTime();

        View status_reader = findViewById(R.id.linearLayout8);
        status_reader.setVisibility(View.VISIBLE);

        DatabaseReference room = database.getReference("Room");


        room.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                String id = snapshot.child("id").getValue(String.class);
                String message = snapshot.child("message").getValue(String.class);
                String image = snapshot.child("image").exists()? snapshot.child("image").getValue(String.class):"";
                //long time = snapshot.child("time").getValue(Long.class);

                boolean reply = Boolean.TRUE.equals(snapshot.child("reply").getValue(Boolean.class));

                RoomModel message_data = new RoomModel();
                message_data.setKey(key);
                message_data.setId(id);
                message_data.setMessage(message);
                //message_data.setTime(time);
                message_data.setImage(image);

                if(reply){

                    String content = snapshot.child("content").getValue(String.class);
                    String content_key = snapshot.child("content_key").getValue(String.class);


                    message_data.setReply(true);
                    message_data.setContent(content);
                    message_data.setContent_key(content_key);
                }

                message_list.add(message_data);
                message_view.scrollToPosition(message_list.size()-1);

                adapter = new RoomAdapter(message_list, getApplicationContext(),auth.uid().toString(),database);
                message_view.setAdapter(adapter);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();

                // Iterate over the message_list array
                for (int i = 0; i < message_list.size(); i++) {
                    RoomModel message = message_list.get(i);

                    // Check if the message has the same key as the removedKey
                    if (message.getKey().equals(removedKey)) {
                        // Remove the item from the array
                        message_list.remove(i);

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

        message_view.scrollToPosition(message_list.size()-1);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = message_box.getText().toString();

                if (message.isEmpty()) {
                    message_box.setError("Message can't be empty");
                    return;
                }

                // Check if the message contains a phone number
                if (containsPhoneNumber(message)) {
                    Toast.makeText(ChatRoom.this, "Phone numbers are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the message contains a link
                if (containsLink(message)) {
                    Toast.makeText(ChatRoom.this, "Links are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                send_btn.setEnabled(false);
                HashMap<String,Object> message_data = new HashMap<>();
                String key = room.push().getKey();
                message_data.put("id",auth.uid());
                message_data.put("message",message);
                message_data.put("time",new Date().getTime());

                if(content != ""){
                    message_data.put("reply",true);
                    message_data.put("content",content);
                    message_data.put("content_key",content_key);
                }else{
                    message_data.put("reply",false);
                }

                room.child(key).setValue(message_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            //Toast.makeText(ChatRoom.this, "Sent", Toast.LENGTH_SHORT).show();
                            message_box.setText("");
                            send_btn.setEnabled(true);
                            message_view.scrollToPosition(message_list.size()-1);

                            View reply_box = findViewById(R.id.reply);
                            content = "";
                            content_key = "";
                            reply_box.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(ChatRoom.this, "Can't send. Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalUsers = snapshot.getChildrenCount();
                all.setText(String.valueOf(totalUsers)+" members");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View chat_box = findViewById(R.id.linearLayout7);
        View banned = findViewById(R.id.blocked);

        usersRef.child(auth.uid()).child("auth").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String type = snapshot.getValue().toString();
                    if(type.equals("1")){
                        chat_box.setVisibility(View.GONE);
                        banned.setVisibility(View.VISIBLE);

                    }else{
                        chat_box.setVisibility(View.VISIBLE);
                        banned.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // chat_box.setVisibility(View.GONE);
            }
        });

        usersRef.orderByChild("online").startAt(System.currentTimeMillis() - (5000))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long onlineUsersCount = snapshot.getChildrenCount();
                                online.setText(String.valueOf(onlineUsersCount)+" online");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



        getSupportActionBar().setTitle("Pandora Spin Room");
    }

    // Helper method to check if a string contains a phone number
    private boolean containsPhoneNumber(String data) {
        return data.matches(".*\\b\\b(225|2|1|224|07|06|7|6).*"); // Matches a 10-digit phone number
    }

    // Helper method to check if a string contains a link
    private boolean containsLink(String input) {
        return input.matches(".*\\b((?:https?|ftp|file)://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    public class SwipeToReplyCallback extends ItemTouchHelper.SimpleCallback {

        private RoomAdapter adapter;

        public SwipeToReplyCallback(RoomAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // No action needed for move gestures
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            replyToMessage(position);
        }
    }

    public void replyToMessage(int position) {
        RoomModel selectedMessage = message_list.get(position);
        View reply_box = findViewById(R.id.reply);
        View close = findViewById(R.id.close);
        TextView user = findViewById(R.id.user);
        TextView contents = findViewById(R.id.content);

        message_list.remove(position);
        adapter.notifyItemRemoved(position);

        message_list.add(position,selectedMessage);
        adapter.notifyItemInserted(position);

        content = selectedMessage.getMessage();
        content_key = selectedMessage.getKey();
        user.setText("Replying");
        contents.setText(content);
        reply_box.setVisibility(View.VISIBLE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = "";
                content_key = "";
                reply_box.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeManager.stopUpdatingTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeManager.stopUpdatingTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeManager.startUpdatingTime();
    }


}