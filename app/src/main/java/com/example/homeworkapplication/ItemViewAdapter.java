package com.example.homeworkapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {
    private final List<Item> items;

    public ItemViewAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.getImageView().setImageBitmap(item.getImage());
        holder.getNameTextView().setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameTextView;

        public ViewHolder(@NonNull View view) {
            super(view);

            imageView = view.findViewById(R.id.imageView);
            nameTextView = view.findViewById(R.id.textView);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }
    }
}
