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

import java.util.List;


public class RemoveItemFragment extends DialogFragment implements View.OnClickListener {
    private RemoveItemAdapter adapter;
    private Context activity;

    public RemoveItemFragment() {
        // Required empty public constructor
    }

    public static RemoveItemFragment newInstance() {
        return new RemoveItemFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (context instanceof Activity) ? context : null;
    }

    @Override
    public int getTheme() {
        return R.style.AnimatedDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_remove_item, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.list_remove_item);

        List<Item> items = ((MainApplication) activity.getApplicationContext()).getItems();
        adapter = new RemoveItemAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        root.findViewById(R.id.btn_remove_item_confirm).setOnClickListener(this);
        root.findViewById(R.id.btn_all_back).setOnClickListener(this);

        return root;
    }

    private void confirm() {
        List<Item> items = adapter.getCheckedItems();
        MainApplication mainApplication = ((MainApplication) activity.getApplicationContext());

        if (items.size() == 0)
            return;

        for (Item item : items) {
            mainApplication.removeItem(item);
        }

        ((MainActivity) activity).onItemsUpdated();
        close();
    }

    public void close() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.d("click", String.valueOf(id));
        if (id == R.id.btn_remove_item_confirm) {
            confirm();
        } else if (id == R.id.btn_all_back) {
            close();
        }
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

}