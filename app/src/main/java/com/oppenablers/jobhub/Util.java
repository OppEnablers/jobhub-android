package com.oppenablers.jobhub;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class Util {

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
}
