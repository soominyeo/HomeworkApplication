package com.example.homeworkapplication;

import java.util.Set;
import java.util.stream.Collectors;

public class PasswordInputFilter extends MyInputFilter {
    private static final Set<Character> FORCED_SET = "!\"#$%&'()*+,-.:;<=>?@[]^_`{|}~".chars().mapToObj(e -> (char) e).collect(Collectors.toSet());

    public PasswordInputFilter() {
        super("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-.:;<=>?@[]^_`{|}~", 32);
    }

    public static boolean isValid(CharSequence input) {
        boolean includesForcedCharacters = false;
        for(char c: input.chars().mapToObj(e -> (char) e).collect(Collectors.toSet())) {
            if(FORCED_SET.contains(c)) {
                includesForcedCharacters = true;
                break;
            }
        }

        return input.length() >= 8 && includesForcedCharacters;
    }
}
