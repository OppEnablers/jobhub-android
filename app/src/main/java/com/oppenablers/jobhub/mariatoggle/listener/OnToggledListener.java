package com.oppenablers.jobhub.mariatoggle.listener;

import com.oppenablers.jobhub.mariatoggle.model.ToggleableView;

public interface OnToggledListener {

    void onToggled(ToggleableView view, boolean isOn);
}
