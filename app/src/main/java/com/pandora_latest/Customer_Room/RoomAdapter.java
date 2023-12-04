package com.pandora_latest.Customer_Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandora_latest.pandora_spin.R;
import com.pandora_latest.pandora_spin.ViewImage;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

    public ArrayList<RoomModel> message_list;
    Context context;
    String auth;
    FirebaseDatabase database;

    private CustomerRoom customerRoom;
    private AlertDialog dialog;

    private ViewHolder vholder;

    public RoomAdapter(ArrayList<RoomModel> message_list, Context context, String auth, FirebaseDatabase database,CustomerRoom customerRoom) {
        this.message_list = message_list;
        this.context = context;
        this.auth = auth;
        this.database = database;
        this.customerRoom = customerRoom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomModel data = message_list.get(position);
        //String currentUser = "Customer Support";
        String currentUser = auth;
        String user = data.getId();
        String message = data.getMessage();
        vholder = holder;

        boolean replied = data.getReply();
        String content = data.getContent();
        String content_key = data.getContent_key();
        String image = data.getImage();

        if(user != null){
            if (user.equals(currentUser)) {
            holder.sent_view.setVisibility(View.VISIBLE);
            holder.message_sent.setText(message);
            if (!image.isEmpty()) {
                Glide.with(context).load(image).into(holder.sent_image);
                holder.sent_image.setVisibility(View.VISIBLE);
                holder.sent_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, ViewImage.class);
                        i.putExtra("image", image);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });
            }
            DatabaseReference reference = database.getReference().child("Users").child(currentUser);
            reference.child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Glide.with(context).load(snapshot.getValue(String.class)).into(holder.user_image_sent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (replied) {
                holder.sent_reply.setVisibility(View.VISIBLE);
                holder.sent_reply.setText(content);

                holder.sent_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(context, String.valueOf(replied), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                holder.sent_reply.setVisibility(View.GONE);
            }

        }
            else {
            holder.received_view.setVisibility(View.VISIBLE);

            if (message.equals("I'm sorry, I didn't understand your message.")) {
                OpenPrompts(vholder.getAdapterPosition());
            }

            DatabaseReference reference = database.getReference().child("Users").child(user);
            reference.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        holder.name_received.setText(snapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            reference.child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Glide.with(context).load(snapshot.getValue(String.class)).into(holder.user_image_received);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if (replied) {
                holder.received_reply.setVisibility(View.VISIBLE);
                holder.received_reply.setText(content);

                holder.received_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, content_key, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            holder.message_received.setText(message);
            if (!image.isEmpty()) {
                Glide.with(context).load(image).into(holder.received_image);
                holder.received_image.setVisibility(View.VISIBLE);
                holder.received_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent i = new Intent(context, ViewImage.class);
                        i.putExtra("image",image);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);*/
                    }
                });
            }
        }
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // create a new dialog builder
                // Get the activity from the view's context
               /* Activity activity = (Activity) v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Delete Message")
                        .setMessage(message)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            String textKey = data.getKey();
                            DatabaseReference room = database.getReference("Customer");

                            if(user.equals("")){
                                room.child(textKey).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            Toast.makeText(context, "Deleted Message", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(context, "Only "+"Consultant"+" Can Delete This Message", Toast.LENGTH_LONG).show();
                            }

                        })
                        .setCancelable(true); // Set dialog as not cancelable


                dialog = builder.create();
                dialog.show();*/

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name_received,message_received,message_sent;
        public View received_view,sent_view,prompts;
        public CircleImageView user_image_received,user_image_sent;

        public TextView received_reply,sent_reply;

        public ImageView received_image,sent_image;


        public ViewHolder(View item){
            super(item);
            received_view = item.findViewById(R.id.received_view);
            sent_view = item.findViewById(R.id.sent_view);

            received_reply = item.findViewById(R.id.received_text_reply);
            sent_reply = item.findViewById(R.id.sent_reply);

            name_received = item.findViewById(R.id.received_name);
            message_received = item.findViewById(R.id.received_text);
            message_sent = item.findViewById(R.id.sent);

            user_image_received = item.findViewById(R.id.user_image_received);
            user_image_sent = item.findViewById(R.id.user_image_sent);

            received_image = item.findViewById(R.id.image_received);
            sent_image = item.findViewById(R.id.image_sent);

            prompts = item.findViewById(R.id.prompts);
        }
    }

    public void OpenPrompts(int index){
        int myPos = vholder.getAdapterPosition();
        if(index == myPos){
            vholder.prompts.setVisibility(View.VISIBLE);

            TextView[] options = {
                    vholder.itemView.findViewById(R.id.p1),
                    vholder.itemView.findViewById(R.id.p2),
                    vholder.itemView.findViewById(R.id.p3),
                    vholder.itemView.findViewById(R.id.p4),
                    vholder.itemView.findViewById(R.id.p5),
                    vholder.itemView.findViewById(R.id.p6),
                    vholder.itemView.findViewById(R.id.p7),
                    vholder.itemView.findViewById(R.id.p8),
            };

            for (TextView option:options) {
                option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference room = database.getReference("Customer");
                        String message = option.getText().toString();

                        customerRoom.BotRespond();

                        RoomModel message_data = new RoomModel();
                        String key = room.push().getKey();
                        message_data.setId(auth);
                        message_data.setMessage(message);
                        message_data.setTime(new Date().getTime());
                        message_data.setReply(false);


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
}}}}
