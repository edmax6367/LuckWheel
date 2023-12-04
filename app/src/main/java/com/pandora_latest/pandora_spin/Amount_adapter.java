package com.pandora_latest.pandora_spin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Amount_adapter extends RecyclerView.Adapter<Amount_adapter.ViewHolder>{

    private Context context;
    private ArrayList<Amount_model> amountsList;

    public Amount_adapter(Context context, ArrayList<Amount_model> amountsList) {
        this.context = context;
        this.amountsList = amountsList;
    }

    @NonNull
    @Override
    public Amount_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amount_item, parent, false);
        return new Amount_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Amount_adapter.ViewHolder holder, int position) {
        Amount_model data = amountsList.get(position);

        int amount = data.getAmount();
        int profit = data.getProfit();
        int vip = data.getVIP();
        int loc = data.getLoc();


        holder.info.setText(String.valueOf("VIP "+vip+"\nAmount: "+amount+"Tsh"+"\nProfit/Day "+profit+"Tsh"));


        holder.deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusDialog(holder,loc,amount);
            }
        });

    }


    @Override
    public int getItemCount() {
        return amountsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView info,deposit;

        public ViewHolder(@NonNull View v) {
            super(v);

            info = v.findViewById(R.id.type_);
            deposit = v.findViewById(R.id.select);
        }
    }

    private void showStatusDialog(ViewHolder holder,int loc,int amount) {
        String[] options = {"Cancel", "Confirm"};

        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("You are about to make a deposit")
                .setSingleChoiceItems(options, -1, (dialog, which) -> {
                    String selectedOption = options[which];

                    if(selectedOption.equals("Cancel")){
                        Toast.makeText(context, "Transaction Canceled", Toast.LENGTH_SHORT).show();
                    }else{
                        if(loc == 2){
                            Intent i = new Intent(context,USDT.class);
                            i.putExtra("amount",amount);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else if(loc == 1){
                            Intent i = new Intent(context,RECEIPT.class);
                            i.putExtra("amount",amount);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }

                    }

                    dialog.dismiss();
                })
                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Operation Aborted", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
    }
}
