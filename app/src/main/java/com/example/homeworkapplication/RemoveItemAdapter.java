package com.example.homeworkapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RemoveItemAdapter extends RecyclerView.Adapter<RemoveItemAdapter.ViewHolder> {
    private final List<Item> items;
    private final List<Item> checkedItems;

    public RemoveItemAdapter(List<Item> items) {
        this.items = items;
        checkedItems = new ArrayList<Item>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_remove_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        CheckedTextView checkedTextView = viewHolder.getCheckedTextView();
        checkedTextView.setText(item.getName());
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckedTextView) view).toggle();

                if (((CheckedTextView) view).isChecked()) {
                    checkedItems.add(item);
                } else {
                    checkedItems.remove(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Item> getCheckedItems() {
        return new ArrayList<Item>(checkedItems);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckedTextView checkedTextView;

        public ViewHolder(View view) {
            super(view);

            checkedTextView = view.findViewById(R.id.checkedTextView);
        }

        public CheckedTextView getCheckedTextView() {
            return checkedTextView;
        }
    }
}