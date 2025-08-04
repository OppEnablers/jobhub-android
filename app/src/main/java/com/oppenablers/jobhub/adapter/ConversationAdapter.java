package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ItemConversationBinding;
import com.oppenablers.jobhub.model.Conversation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<Conversation> conversations;
    private OnClickListener onClickListener;

    public ConversationAdapter(List<Conversation> conversations, OnClickListener onClickListener) {
        this.conversations = conversations != null ? conversations : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    public ConversationAdapter(List<Conversation> conversations) {
        this(conversations, null);
    }

    public void updateConversations(List<Conversation> newConversations) {
        if (newConversations != null) {
            this.conversations.clear();
            this.conversations.addAll(newConversations);
            notifyDataSetChanged();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConversationBinding binding = ItemConversationBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ConversationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.bind(conversations.get(position));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        private final ItemConversationBinding binding;

        public ConversationViewHolder(@NonNull ItemConversationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Conversation conversation) {
            if (conversation.getOtherUserName() != null && !conversation.getOtherUserName().isEmpty()) {
                binding.userName.setText(conversation.getOtherUserName());
            } else {
                binding.userName.setText("User");
            }

            if (conversation.getLastMessageContent() != null && !conversation.getLastMessageContent().isEmpty()) {
                binding.messagePreview.setText(conversation.getLastMessageContent());
            } else {
                binding.messagePreview.setText("No messages yet");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            if (conversation.getLastMessageTimestamp() > 0) {
                binding.time.setText(sdf.format(new Date(conversation.getLastMessageTimestamp())));
            } else {
                binding.time.setText("");
            }

            if (conversation.hasUnreadMessages()) {
                binding.unreadIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.unreadIndicator.setVisibility(View.GONE);
            }

            if (conversation.getOtherUserPhotoUrl() != null && !conversation.getOtherUserPhotoUrl().isEmpty()) {
                Glide.with(binding.getRoot())
                        .load(conversation.getOtherUserPhotoUrl())
                        .placeholder(R.drawable.hugh_l)
                        .transform(new CircleCrop())
                        .into(binding.profileImage);
            } else {
                binding.profileImage.setImageResource(R.drawable.hugh_l);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(
                            conversation.getOtherUserId(),
                            conversation.getOtherUserName() != null ?
                                    conversation.getOtherUserName() : "User"
                    );
                }
            });
        }
    }

    public interface OnClickListener {
        void onClick(String userId, String userName);
    }
}