package com.pandora_latest.Customer_Room;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.PandoraBot.IntentModel;
import com.pandora_latest.PandoraBot.Response;
import com.pandora_latest.PandoraBot.SuggestionAdapter;
import com.pandora_latest.QubeAuth.QubeAuth;
import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRoom extends AppCompatActivity {

    private ArrayList<RoomModel> message_list = new ArrayList<>();
    RoomAdapter adapter;
    SuggestionAdapter suggestionAdapter;
    RecyclerView message_view;
    RecyclerView suggestions_view;
    private LinearLayoutManager vertical_layout;
    private LinearLayoutManager vertical_layout2;

    private FirebaseDatabase database;
    QubeAuth auth;

    View send_btn;
    EditText message_box;

    private ItemTouchHelper itemTouchHelper;

    String content = "";
    String content_key = "";
    
    ArrayList<Response> responses = new ArrayList<>();
    boolean bot_respond = false;
    boolean bot_initiated = false;
    boolean scanMessage = false;

    private int messageIndex = -1;

    private ArrayList<IntentModel> intent = new ArrayList<>();
    private ArrayList<String> suggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        message_view = findViewById(R.id.messages);
        suggestions_view = findViewById(R.id.suggestions_viewer);
        send_btn = findViewById(R.id.send_btn);
        message_box = findViewById(R.id.message_box);
        database = FirebaseDatabase.getInstance();
        auth = new QubeAuth(getApplicationContext(),database,null);
        bot_initiated = true;


        DatabaseReference bot = database.getReference("Bot");

        bot.child("Brain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, List<String>> phraseToResponsesMap = new HashMap<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String phrase = dataSnapshot.child("phrase").getValue(String.class);
                    String response = dataSnapshot.child("suggestion").getValue(String.class);

                    // Check if the phrase already exists in the map
                    if (phraseToResponsesMap.containsKey(phrase)) {
                        // If the phrase exists, add the response to the existing list of responses
                        phraseToResponsesMap.get(phrase).add(response);
                    } else {
                        // If the phrase doesn't exist, create a new list with the response
                        List<String> responses = new ArrayList<>();
                        responses.add(response);
                        phraseToResponsesMap.put(phrase, responses);
                    }
                }

                intent.clear();

                ArrayList<IntentModel> intentModels = new ArrayList<>();

                for (Map.Entry<String, List<String>> entry : phraseToResponsesMap.entrySet()) {
                    String intent = entry.getKey();
                    List<String> responses = entry.getValue();

                    IntentModel intentModel = new IntentModel();
                    intentModel.setIntent(intent);
                    intentModel.setResponses(responses.toArray(new String[0]));

                    intentModels.add(intentModel);
                }

                // Add the elements from intentModels to the intent ArrayList
                intent.addAll(intentModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        message_view.setHasFixedSize(true);
        vertical_layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        vertical_layout2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        message_view.setLayoutManager(vertical_layout);
        suggestions_view.setLayoutManager(vertical_layout2);

        // Create and attach the swipe-to-reply callback
        itemTouchHelper = new ItemTouchHelper(new CustomerRoom.SwipeToReplyCallback(adapter));
        itemTouchHelper.attachToRecyclerView(message_view);
        message_view.scrollToPosition(message_list.size());

        View status_reader = findViewById(R.id.linearLayout8);
        status_reader.setVisibility(View.GONE);

        DatabaseReference room = database.getReference("Customer");
        room.child(auth.uid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                String id = snapshot.child("id").getValue(String.class);
                String message = snapshot.child("message").getValue(String.class);
                String image = snapshot.child("image").exists()? snapshot.child("image").getValue(String.class):"";
              //  long time = snapshot.child("time").getValue(Long.class);

                messageIndex++;

                boolean reply = Boolean.TRUE.equals(snapshot.child("reply").getValue(Boolean.class));
                boolean solved = false;
                boolean end_session = false;
                int solved_index = -1;
                String defaultResponse = "Dear Customer, Please wait while we are working on your request.";

                if(id == auth.uid() && bot_respond){

                    Query scanner = database.getReference("Bot").child("Brain")
                            .orderByChild("suggestion").equalTo(message);
                    scanner.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()) {

                                for(DataSnapshot ds : snapshot.getChildren()){
                                    String response = ds.child("response").getValue().toString();

                                    bot_respond =false;

                                    HashMap<String,Object> message_data = new HashMap<>();
                                    String _key = room.push().getKey();
                                    message_data.put("id","Customer Support");
                                    message_data.put("message",response);
                                    message_data.put("time",new Date().getTime());
                                    message_data.put("reply",false);

                                    room.child(auth.uid()).child(_key).setValue(message_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                send_btn.setVisibility(View.VISIBLE);
                                                content = "";
                                            } else {
                                                send_btn.setVisibility(View.VISIBLE);
                                                Toast.makeText(CustomerRoom.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }

                            }
                            else{
                                bot_respond =false;

                               /* HashMap<String,Object> message_data = new HashMap<>();
                                String _key = room.push().getKey();
                                message_data.put("id","Customer Support");
                                message_data.put("message",defaultResponse);
                                message_data.put("time",new Date().getTime());
                                message_data.put("reply",false);

                                room.child(auth.uid()).child(_key).setValue(message_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()) {
                                            send_btn.setVisibility(View.VISIBLE);
                                        } else {
                                            send_btn.setVisibility(View.VISIBLE);
                                            Toast.makeText(CustomerRoom.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


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


                adapter = new RoomAdapter(message_list, getApplicationContext(),auth.uid().toString(),database,CustomerRoom.this);
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
                    com.pandora_latest.Customer_Room.RoomModel message = message_list.get(i);

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

        message_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                scanMessage = charSequence.length() > 4;
                String typeQuery = charSequence.toString();
                if(scanMessage){
                    for (int s = 0; s < intent.size();s++) {
                        String keyword = intent.get(s).getIntent();

                        if(keyword != null){
                            if(!keyword.isEmpty()){
                                boolean related = typeQuery.toLowerCase().contains(keyword.toLowerCase());
                                //Toast.makeText(CustomerRoom.this, keyword+" : "+String.valueOf(related), Toast.LENGTH_SHORT).show();
                                if(related){
                                    suggestions.clear();
                                    for (String response:intent.get(s).getResponses()) {
                                        suggestions.add(response);

                                    }
                                    suggestionAdapter = new SuggestionAdapter(suggestions,getApplicationContext(),auth.uid(),database,CustomerRoom.this);
                                    suggestions_view.setAdapter(suggestionAdapter);

                                    break;
                                }else{
                                    suggestions.clear();
                                    suggestionAdapter = new SuggestionAdapter(suggestions,getApplicationContext(),auth.uid(),database,CustomerRoom.this);
                                    suggestions_view.setAdapter(suggestionAdapter);
                                }
                            }
                        }
                    }
                }else{
                    suggestions.clear();
                    suggestionAdapter = new SuggestionAdapter(suggestions,getApplicationContext(),auth.uid(),database,CustomerRoom.this);
                    suggestions_view.setAdapter(suggestionAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                    Toast.makeText(CustomerRoom.this, "Phone numbers are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the message contains a link
                if (containsLink(message)) {
                    Toast.makeText(CustomerRoom.this, "Links are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }
                message_view.scrollToPosition(message_list.size()-1);
                bot_respond = true;


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

                room.child(auth.uid()).child(key).setValue(message_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                           // Toast.makeText(CustomerRoom.this, "Sent", Toast.LENGTH_SHORT).show();
                            message_box.setText("");
                            send_btn.setEnabled(true);
                            message_view.scrollToPosition(message_list.size()-1);

                            View reply_box = findViewById(R.id.reply);
                            content = "";
                            content_key = "";
                            reply_box.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(CustomerRoom.this, "Can't send. Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        getSupportActionBar().setTitle("Pandora Spin Customer Support");
    }

    // Helper method to check if a string contains a phone number
    private boolean containsPhoneNumber(String data) {
        return data.matches(".*\\b\\d{6}\\b.*"); // Matches a 10-digit phone number
    }

    // Helper method to check if a string contains a link
    private boolean containsLink(String input) {
        return input.matches(".*\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }


    public class SwipeToReplyCallback extends ItemTouchHelper.SimpleCallback {

        private com.pandora_latest.Customer_Room.RoomAdapter adapter;

        public SwipeToReplyCallback(com.pandora_latest.Customer_Room.RoomAdapter adapter) {
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
        com.pandora_latest.Customer_Room.RoomModel selectedMessage = message_list.get(position);
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

    // Function to count the occurrences of a pattern in a string
    private static int countPatternOccurrences(String input, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = input.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }

    // Function to find the pattern with the highest count
    private static String findMostRepeatedPattern(HashMap<String, Integer> patternCounts) {
        String mostRepeatedPattern = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : patternCounts.entrySet()) {
            String pattern = entry.getKey();
            int count = entry.getValue();

            if (count > maxCount) {
                maxCount = count;
                mostRepeatedPattern = pattern;
            }
        }

        return mostRepeatedPattern;
    }

    public void BotRespond(){
        bot_respond = true;
        send_btn.setVisibility(View.GONE);
        message_box.setText("");
    }


}