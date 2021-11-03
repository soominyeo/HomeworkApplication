package com.example.homeworkapplication;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserInfoListAdapter extends RecyclerView.Adapter<UserInfoListAdapter.ViewHolder> {
    private static final int INFO_COUNT = 5;
    private final List<Pair<String, String>> infoList;
    private final Context activity;

    public UserInfoListAdapter(Context context, User user) {
        activity = context;

        infoList = new ArrayList<Pair<String, String>>();
        infoList.add(new Pair<String, String>(activity.getResources().getString(R.string.all_user_id), user.getId()));
        infoList.add(new Pair<String, String>(activity.getResources().getString(R.string.all_user_name), user.getName()));
        infoList.add(new Pair<String, String>(activity.getResources().getString(R.string.all_user_phone_number), user.getPhone()));
        infoList.add(new Pair<String, String>(activity.getResources().getString(R.string.all_user_address), user.getAddress()));
        infoList.add(new Pair<String, String>(activity.getResources().getString(R.string.all_user_term), user.isAccepted() ? "동의" : "거부"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, String> info = infoList.get(position);

        holder.getKeyTextView().setText(info.first);
        holder.getValueTextView().setText(info.second);
    }

    @Override
    public int getItemCount() {
        return INFO_COUNT;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView keyTextView;
        private final TextView valueTextView;

        public ViewHolder(View view) {
            super(view);

            keyTextView = view.findViewById(R.id.text_user_info_key);
            valueTextView = view.findViewById(R.id.text_user_info_value);
        }

        public TextView getKeyTextView() {
            return keyTextView;
        }

        public TextView getValueTextView() {
            return valueTextView;
        }
    }
}
