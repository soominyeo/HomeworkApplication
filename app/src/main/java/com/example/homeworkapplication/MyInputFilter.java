package com.example.homeworkapplication;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MyInputFilter implements InputFilter {
    private final String allowed;
    private final Set<Character> allowed_set;
    private final int maxSize;

    public MyInputFilter(String allowed, int maxSize) {
        this.allowed = allowed;
        this.maxSize = maxSize;
        allowed_set = allowed.chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
    }

    protected boolean isAllowedCharacters(char c) {
        return allowed_set.contains(c);
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destStart, int destEnd) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end && i - start < maxSize; i++) {
            char c = source.charAt(i);
            if(isAllowedCharacters(c)) {
                builder.append(c);
            }

        }

        boolean isValid = builder.length() == end - start;
        return isValid ? null : builder.toString();
    }
}
