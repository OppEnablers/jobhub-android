package com.oppenablers.jobhub.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.adapter.JobCardAdapter;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentJsJobBinding;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.Vacancy;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class JsJobFragment extends Fragment implements CardStackListener {

    private FragmentJsJobBinding binding;

    public JsJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJsJobBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardStackLayoutManager layoutManager = new CardStackLayoutManager(getContext(), this);
//        layoutManager.setTranslationInterval(0.9f);
//        layoutManager.setScaleInterval(0.9f);
        layoutManager.setStackFrom(StackFrom.Top);

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.Left);
        directions.add(Direction.Right);

        layoutManager.setDirections(directions);
        layoutManager.setCanScrollVertical(false);

        CardStackView cardStackView = binding.cardStackView;

        cardStackView.setLayoutManager(layoutManager);

        binding.fabSwipeLeft.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().setDirection(Direction.Left).build();
            layoutManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        binding.fabSwipeRight.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().setDirection(Direction.Right).build();
            layoutManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ArrayList<Rect> exclusionRects = new ArrayList<>();
            Rect rect = new Rect();
            binding.getRoot().getHitRect(rect);
            exclusionRects.add(rect);

            binding.getRoot().setSystemGestureExclusionRects(exclusionRects);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Vacancy> jobs = new ArrayList<>();

        JobHubClient.getJobsJobSeeker(new JobHubClient.JobHubCallback<ArrayList<Vacancy>>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<Vacancy> result) {
                JobCardAdapter jobCardAdapter = new JobCardAdapter(result);
                binding.cardStackView.setAdapter(jobCardAdapter);
            }
        });
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        CardStackLayoutManager layoutManager = (CardStackLayoutManager) binding.cardStackView.getLayoutManager();
        JobCardAdapter adapter = (JobCardAdapter) binding.cardStackView.getAdapter();

        if (adapter == null || layoutManager == null) return;

        if (direction == Direction.Right) {
            JobHubClient.acceptJobJobSeeker(adapter.getJob(layoutManager.getTopPosition() - 1), new JobHubClient.JobHubCallbackVoid() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Job accepted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}