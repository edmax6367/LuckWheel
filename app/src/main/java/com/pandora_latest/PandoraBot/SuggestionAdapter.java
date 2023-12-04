package com.pandora_latest.PandoraBot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pandora_latest.Customer_Room.CustomerRoom;
import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder>{

    ArrayList<String> suggestionList;
    Context context;

    String auth;
    FirebaseDatabase database;
    private CustomerRoom customerRoom;


    public SuggestionAdapter(ArrayList<String> suggestionList, Context context, String auth, FirebaseDatabase database, CustomerRoom customerRoom) {
        this.suggestionList = suggestionList;
        this.context = context;
        this.auth = auth;
        this.database = database;
        this.customerRoom = customerRoom;
    }

    @NonNull
    @Override
    public SuggestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_item, parent, false);
        return new SuggestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdapter.ViewHolder holder, int position) {
        String qn = suggestionList.get(position);

        holder.questionData.setText(qn);
        holder.questionData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference room = database.getReference("Customer");


                customerRoom.BotRespond();

                HashMap<String,Object> message_data = new HashMap<>();
                String key = room.push().getKey();
                message_data.put("id",auth);
                message_data.put("message",qn);
                message_data.put("time",new Date().getTime());
                message_data.put("reply",false);


                room.child(auth).child(key).setValue(message_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {

                        } else {
                            Toast.makeText(context, "Something went wrong, Try again Later", Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionData;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionData = itemView.findViewById(R.id.questionData);
        }
    }
}
