package com.example.homeworkapplication;

import android.text.InputFilter;
import android.text.Spanned;

public class IDInputFilter extends MyInputFilter {
    public IDInputFilter() {
        super("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-_'", 64);
    }

    public static boolean isValid(CharSequence input) {
        return input.length() >= 6 && !Character.isDigit(input.charAt(0));
    }
}
