package com.example.homeworkapplication;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;

import com.example.homeworkapplication.ItemContract.ItemEntry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    private List<Item> items;
    private ItemDBHelper itemDBHelper;
    private boolean loggedIn = false;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        itemDBHelper = new ItemDBHelper(this);

        SharedPreferences prefs = getSharedPreferences("application", 0);
        if (prefs.getBoolean("is_default", true)) {
            Log.d("is_default", "true");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("is_default", false);
            editor.apply();

            items = new ArrayList<Item>();

            addItem(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.snack_1), getResources().getString(R.string.media_snack_1)));
            addItem(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.snack_2), getResources().getString(R.string.media_snack_2)));
            addItem(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.snack_3), getResources().getString(R.string.media_snack_3)));
            addItem(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.snack_4), getResources().getString(R.string.media_snack_4)));
            addItem(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.snack_5), getResources().getString(R.string.media_snack_5)));
        } else {
            loadItems();
            Log.d("is_default", "false");
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return new ArrayList<Item>(items);
    }

    private void loadItems() {
        SQLiteDatabase db = itemDBHelper.getReadableDatabase();

        Cursor cursor = db.query(
                ItemEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ItemEntry.COLUMN_NAME_NAME + " ASC"
        );
        items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_NAME));
            long id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            Bitmap bitmap = Item.readImage(this, id);
            items.add(new Item(bitmap, name, id));
        }

        cursor.close();
    }

    public void removeItem(Item item) {
        if (item.getId() == null)
            return;

        Item.deleteImage(this, item.getId());

        SQLiteDatabase db = itemDBHelper.getWritableDatabase();

        String selection = BaseColumns._ID + "=?";
        String[] selectionArgs = new String[]{item.getId().toString()};

        db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);

        items.remove(item);
    }

    public void addItem(Item item) {
        SQLiteDatabase db = itemDBHelper.getWritableDatabase();

        if (item.getId() == null) {
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_NAME_NAME, item.getName());

            long id = db.insert(ItemEntry.TABLE_NAME, null, values);
            item.setId(id);
            Item.saveImage(this, id, item.getImage());
            items.add(item);
        }
    }

    public static class ItemDBHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "item.db";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                        ItemEntry._ID + " INTEGER PRIMARY KEY," +
                        ItemEntry.COLUMN_NAME_NAME + " TEXT)";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

        public ItemDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
