package com.oppenablers.jobhub.mariatoggle.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.oppenablers.jobhub.mariatoggle.listener.OnToggledListener;

public class ToggleableView extends View {

    protected int width;
    protected int height;

    protected boolean isOn;
    protected boolean isEnabled;
    protected OnToggledListener onToggledListener;

    public ToggleableView(Context context) {
        super(context);
    }

    public ToggleableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ToggleableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setOnToggleListener(OnToggledListener onToggledListener) {
        this.onToggledListener = onToggledListener;
    }
}
