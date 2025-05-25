package com.oppenablers.mariatoggle.listener;

import com.oppenablers.mariatoggle.model.ToggleableView;

public interface OnToggledListener {

    void onToggled(ToggleableView view, boolean isOn);
}
