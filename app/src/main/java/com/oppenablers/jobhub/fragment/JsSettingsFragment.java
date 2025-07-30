package com.oppenablers.jobhub.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.model.JobSeeker;

public class JsSettingsFragment extends PreferenceFragmentCompat {

    private DummyDataStore dataStore;

    public JsSettingsFragment() {
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(JobSeeker result) {

                if (result == null) return;

                Context context = getContext();
                if (context == null) return;

                dataStore = new DummyDataStore(result);
                getPreferenceManager().setPreferenceDataStore(dataStore);
                setPreferencesFromResource(R.xml.js_preferences, rootKey);
            }
        });
    }
}

class DummyDataStore extends PreferenceDataStore {

    private JobSeeker accountDetails;

    public DummyDataStore(JobSeeker accountDetails) {
        this.accountDetails = accountDetails;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {

        switch (key) {
            case "email_address":
                return accountDetails.getEmail();
            case "phone_number":
                return accountDetails.getPhoneNumber();
            default:
                return "";
        }
    }

    @Override
    public void putString(String key, @Nullable String value) {
        switch (key) {
            case "email_address":
                accountDetails.setEmail(value);
            case "phone_number":
                accountDetails.setPhoneNumber(value);
            default:
                break;
        }

        updateInfo();
    }

    public void updateInfo() {
        JobHubClient.updateAccountInfoJobSeeker(accountDetails, new JobHubClient.JobHubCallbackVoid() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess() {
            }
        });
    }
}