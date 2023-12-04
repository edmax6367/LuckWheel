package com.pandora_latest.MarketUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pandora_latest.pandora_spin.ProductView;
import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;
import java.util.Locale;

public class marketAdapter extends RecyclerView.Adapter<marketAdapter.ViewHolder> {

    public ArrayList<marketModel> product_list;
    Context context;
    private AlertDialog dialog;

    public marketAdapter(ArrayList<marketModel> product_list, Context context) {
        this.product_list = product_list;
        this.context = context;
    }

    @NonNull
    @Override
    public marketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new marketAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull marketAdapter.ViewHolder holder, int position) {
        marketModel data = product_list.get(position);
        String name = data.getName();
        String description = data.getDescription();
        String image = data.getImage();
        long price = data.getPrice();
        long discount = data.getDiscount();
        long duration = data.getDuration();
        long profit = data.getProfit();
        String id = data.getId();

        holder.description.setText(name+"~"+description);
        Glide.with(context).load(image).into(holder.product_image);
        holder.price.setText(String.format(Locale.US,"%,d", price)+"Tsh");
        holder.discount.setText(String.valueOf(discount)+"% OFF");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductView.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtra("image",image);
                i.putExtra("details",name+"~"+description);
                i.putExtra("price",price);
                i.putExtra("profit",profit);
                i.putExtra("duration",duration);
                i.putExtra("id",id);

                context.startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return product_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView discount,price,description;
        public ImageView product_image;


        public ViewHolder(View v){
            super(v);

            price = v.findViewById(R.id.price);
            description = v.findViewById(R.id.description);
            discount = v.findViewById(R.id.discount);
            product_image = v.findViewById(R.id.product_image);

        }
    }


}