package com.example.homeworkapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserInfoFragment extends DialogFragment implements View.OnClickListener {
    private UserInfoListAdapter adapter;
    private Context activity;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance() {
        return new AddItemFragment();
    }

    @Override
    public int getTheme() {
        return R.style.AnimatedDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (context instanceof Activity) ? context : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_user_info, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.list_user_info);

        recyclerView.setHasFixedSize(true);
        adapter = new UserInfoListAdapter(activity, ((MainApplication) activity.getApplicationContext()).getUser());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        root.findViewById(R.id.btn_user_info_confirm).setOnClickListener(this);
        root.findViewById(R.id.btn_all_back).setOnClickListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.d("click", String.valueOf(id));
        if (id == R.id.btn_user_info_confirm || id == R.id.btn_all_back) {
            close();
        }
    }

    public void close() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }
}
