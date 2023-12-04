package com.pandora_latest.News;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandora_latest.pandora_spin.R;

import java.util.ArrayList;

public class bottomAnouncementAdapter extends RecyclerView.Adapter<bottomAnouncementAdapter.ViewHolder> {

    public ArrayList<String> data;

    public bottomAnouncementAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ViewHolder(View item){
            super(item);
            textView = item.findViewById(R.id.nn);
        }
    }
}
