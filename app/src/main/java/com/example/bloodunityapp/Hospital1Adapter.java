package com.example.bloodunityapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Hospital1Adapter extends RecyclerView.Adapter<Hospital1Adapter.ViewHolder> {
    ArrayList<Donatemodel> list;

    public interface OnItemClickListener {
        void onItemClick(Donatemodel item);
    }

    private OnItemClickListener listener;

    public Hospital1Adapter(ArrayList<Donatemodel> list) {
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dataview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donatemodel model = list.get(position);
        holder.imageView.setImageResource(model.getImage());
        holder.nameView.setText(model.getHname());
        holder.bloodGroupView.setText(model.getBlooodgrp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView bloodGroupView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameView = itemView.findViewById(R.id.name_view);
            bloodGroupView = itemView.findViewById(R.id.bloodgroup_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(list.get(position));
                        }
                    }
                }
            });
        }
    }
}