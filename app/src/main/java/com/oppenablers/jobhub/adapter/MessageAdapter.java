package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.databinding.ItemMessageReceivedBinding;
import com.oppenablers.jobhub.databinding.ItemMessageSentBinding;
import com.oppenablers.jobhub.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private final String currentUserId;
    private List<Message> messages;

    public MessageAdapter(String currentUserId, List<Message> messages) {
        this.currentUserId = currentUserId;
        this.messages = new ArrayList<>();
        if (messages != null) {
            this.messages.addAll(messages);
        }
    }

    public void updateMessages(List<Message> newMessages) {
        if (newMessages != null) {
            this.messages.clear();
            this.messages.addAll(newMessages);
            notifyDataSetChanged();
        }
    }

    public void addMessage(Message message) {
        // Check for duplicates before adding
        for (Message existing : messages) {
            if (existing.getMessageId() != null &&
                    existing.getMessageId().equals(message.getMessageId())) {
                return;
            }
        }

        this.messages.add(message);
        notifyItemInserted(this.messages.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getSenderId().equals(currentUserId)
                ? VIEW_TYPE_SENT
                : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SENT) {
            ItemMessageSentBinding binding = ItemMessageSentBinding.inflate(inflater, parent, false);
            return new SentMessageViewHolder(binding);
        } else {
            ItemMessageReceivedBinding binding = ItemMessageReceivedBinding.inflate(inflater, parent, false);
            return new ReceivedMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(message.getTimestamp()));

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message, time);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message, time);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageSentBinding binding;

        public SentMessageViewHolder(ItemMessageSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message, String time) {
            binding.messageText.setText(message.getContent());
            binding.messageTime.setText(time);

            if (message.getType() == Message.MessageType.IMAGE) {
                binding.messageImage.setVisibility(View.VISIBLE);
                binding.messageText.setVisibility(View.GONE);
            } else {
                binding.messageImage.setVisibility(View.GONE);
                binding.messageText.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageReceivedBinding binding;

        public ReceivedMessageViewHolder(ItemMessageReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message, String time) {
            binding.messageText.setText(message.getContent());
            binding.messageTime.setText(time);


            if (message.getType() == Message.MessageType.IMAGE) {
                binding.messageImage.setVisibility(View.VISIBLE);
                binding.messageText.setVisibility(View.GONE);
            } else {
                binding.messageImage.setVisibility(View.GONE);
                binding.messageText.setVisibility(View.VISIBLE);
            }
        }
    }
}