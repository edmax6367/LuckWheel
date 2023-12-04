package com.pandora_latest.pandora_spin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Payment_adapter extends RecyclerView.Adapter<Payment_adapter.ViewHolder>{

    private Context context;
    private ArrayList<Payment_model> paymentsList;

    public Payment_adapter(Context context, ArrayList<Payment_model> paymentsList) {
        this.context = context;
        this.paymentsList = paymentsList;
    }

    @NonNull
    @Override
    public Payment_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.methods_item, parent, false);
        return new Payment_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Payment_adapter.ViewHolder holder, int position) {
        Payment_model data = paymentsList.get(position);

        holder.type.setText(String.valueOf(data.getType()));

        int activity = data.getActivity();

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusDialog(holder,activity);
            }
        });

    }


    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type,select;

        public ViewHolder(@NonNull View v) {
            super(v);

            type = v.findViewById(R.id.type_);
            select = v.findViewById(R.id.select);
        }
    }

    private void showStatusDialog(ViewHolder holder,int location) {
        String[] options = {"Deposit", "Withdraw"};

        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Select Payment Activity")
                .setSingleChoiceItems(options, -1, (dialog, which) -> {
                    String selectedOption = options[which];

                    if(selectedOption.equals("Deposit")){
                        if(location == 2){
                            Intent i = new Intent(context,Amount.class);
                            i.putExtra("l",location);
                            i.putExtra("t",selectedOption);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if(location == 1){
                            Intent i = new Intent(context,Amount.class);
                            i.putExtra("l",location);
                            i.putExtra("t",selectedOption);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }else{
                        if(location == 1){
                            Intent i = new Intent(context,NORMAL_WITHDRAW.class);
                            i.putExtra("l",location);
                            i.putExtra("t",selectedOption);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if(location == 2){
                            Intent i = new Intent(context,Amount.class);
                            i.putExtra("l",location);
                            i.putExtra("t",selectedOption);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //context.startActivity(i);
                            Toast.makeText(context, "Unavailable!\nUSDT Withdraw Support Coming Soon ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
