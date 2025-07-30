package com.oppenablers.jobhub;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Utility {

    public static String getTextFromTextInputLayout(TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static void setTextFromTextInputLayout(TextInputLayout textInputLayout, String text) {
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            editText.setText(text.trim());
        }
    }

    public static boolean hasFlag(int value, int flag) {
        return (value & flag) == flag;
    }
}
