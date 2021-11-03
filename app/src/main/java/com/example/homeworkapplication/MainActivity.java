package com.example.homeworkapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null && intent.getStringExtra("ACTIVITY_SOURCE").equals(RegisterActivity.class.getName())) {
                            try {
                                boolean registerResult = intent.getBooleanExtra("result", false);
                                Toast.makeText(getApplicationContext(),
                                        getString((registerResult) ? R.string.login_toast_register_succeed : R.string.login_toast_register_failed),
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );
    private AddItemFragment addItemFragment;
    private RemoveItemFragment removeItemFragment;
    private UserInfoFragment userInfoFragment;

    private RecyclerView itemViewRecyclerView;
    private ItemViewAdapter itemViewAdapter;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = ((MainApplication) getApplication()).getItems();

        itemViewRecyclerView = findViewById(R.id.recycler_view_item_view);
        itemViewAdapter = new ItemViewAdapter(items);
        itemViewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemViewRecyclerView.setAdapter(itemViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.setGroupDividerEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main_layout_1) {
            changeItemListLayout(1);
        } else if(id == R.id.menu_main_layout_2) {
            changeItemListLayout(2);
        } else if(id == R.id.menu_main_layout_3) {
            changeItemListLayout(3);
        } else if(id == R.id.menu_main_layout_4) {
            changeItemListLayout(4);
        } else if(id == R.id.menu_main_add) {
            openAddItemFragment();
        } else if(id == R.id.menu_main_remove) {
            openRemoveItemFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onItemsUpdated() {
        Log.d("where", "updateitem");

        items.clear();
        items.addAll(((MainApplication) getApplication()).getItems());

        itemViewAdapter.notifyDataSetChanged();
    }

    public void onAddItem(View view) {
        openAddItemFragment();
    }

    private void changeItemListLayout(int columns) {
        if(columns <= 1) {
            itemViewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else if(columns <= 4) {
            itemViewRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        }
        itemViewRecyclerView.setAdapter(itemViewAdapter);
    }

    private void openAddItemFragment() {
        Log.d("where", "additem");

        if (addItemFragment == null
                || addItemFragment.getDialog() == null
                || addItemFragment.getDialog().isShowing()) {
            addItemFragment = new AddItemFragment();
            addItemFragment.show(getSupportFragmentManager(), null);
        }
    }

    public void onRemoveItem(View view) {
        openRemoveItemFragment();
    }

    private void openRemoveItemFragment() {
        Log.d("where", "removeitem");

        if (removeItemFragment == null
                || removeItemFragment.getDialog() == null
                || removeItemFragment.getDialog().isShowing()) {
            removeItemFragment = new RemoveItemFragment();
            removeItemFragment.show(getSupportFragmentManager(), null);
        }
    }

    public void onUserInfo(View view) {
        triggerUserInfo();
    }

    public void triggerUserInfo() {
        if (!((MainApplication) getApplication()).isLoggedIn()) {
            // not logged in
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setMessage(R.string.main_error_not_logged_in);
            dialogBuilder.setPositiveButton(R.string.main_login, null);
            dialogBuilder.setNeutralButton(R.string.main_register, null);
            dialogBuilder.setNegativeButton(R.string.all_cancel, null);
            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            // sign in
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                    dialog.dismiss();
                }
            });
            // sign up
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                    dialog.dismiss();
                }
            });
            // cancel
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            // logged in
            openUserInfoFragment();
        }
    }

    private void signIn() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("ACTIVITY_SOURCE", this.getClass().getName());

        resultLauncher.launch(intent);
        finish();
    }

    private void signUp() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra("ACTIVITY_SOURCE", this.getClass().getName());

        resultLauncher.launch(intent);
    }


    public void openUserInfoFragment() {
        Log.d("where", "userinfo");

        if (userInfoFragment == null
                || userInfoFragment.getDialog() == null
                || userInfoFragment.getDialog().isShowing()) {
            userInfoFragment = new UserInfoFragment();
            userInfoFragment.show(getSupportFragmentManager(), null);
        }
    }
}