package com.example.homeworkapplication;

import android.provider.BaseColumns;

public final class ItemContract {
    private ItemContract() {}

    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_NAME = "name";
    }
}