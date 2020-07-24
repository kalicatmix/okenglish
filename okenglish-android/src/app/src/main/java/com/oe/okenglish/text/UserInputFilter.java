package com.oe.okenglish.text;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class UserInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (dest.length() > 10) {
            return "";
        }
        if (Pattern.matches("[^a-z0-9A-Z]", source)) return "";
        return source;
    }

}
