package com.oppenablers.jobhub.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.oppenablers.jobhub.R;

import java.util.Date;
import java.util.Locale;

public class Message {
    public String content;
    public String senderId;
    public long timestamp;
    public boolean isMediaUrl; // true if the content is a URL to media (image, PDF)
    public String mediaType;
    public String mediaUrl;

    public Message() {
    }

    public Message(String content, String senderId, long timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public void setMediaUrl(boolean isMediaUrl) {
        this.isMediaUrl = isMediaUrl;
    }

    public void setMedia(String mediaType, String mediaUrl) {
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.isMediaUrl = (mediaUrl != null);
    }

    public boolean hasMedia() {
        return isMediaUrl && mediaUrl != null;
    }

    public boolean isImage() {
        return "image".equals(mediaType);
    }

    public boolean isPdf() {
        return "pdf".equals(mediaType);
    }

    public static void addSentMessageBubble(Context context, LinearLayout messagesContainer, ScrollView scroll, String messageText, long timestamp) {
        // Find your container
        //LinearLayout messagesContainer = findViewById(R.id.messages_container);

        // Outer layout for alignment
        LinearLayout outerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0,8); // match layout_marginBottom="8dp"
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.END);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        // Inner message bubble
        LinearLayout bubbleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginStart(60); // match layout_marginStart="60dp"
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12);
        bubbleLayout.setBackgroundResource(R.drawable.sent_message_bubble);

        // Message text
        TextView messageTextView = new TextView(context);
        messageTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        messageTextView.setText(messageText);
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setTextSize(16f);
        messageTextView.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_medium));

        // Time text
        TextView timeTextView = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#e0e0e0"));
        timeTextView.setTextSize(12f);
        if (timestamp == 0) {
            timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));
        } else {
            timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(timestamp)));
        }

        // Build view hierarchy
        bubbleLayout.addView(messageTextView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        // Optional: auto-scroll to bottom if wrapped in a ScrollView
        //ScrollView scroll = findViewById(R.id.messages_scroll);
        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }

    public static void addSentMessageBubbleWithImage(Context context, LinearLayout messagesContainer, ScrollView scroll, String imageUrl, long timestamp) {
        LinearLayout outerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0,8);
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.END);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bubbleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginStart(60);
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12);
        bubbleLayout.setBackgroundResource(R.drawable.sent_message_bubble);

        android.widget.ImageView imageView = new android.widget.ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
        com.bumptech.glide.Glide.with(context).load(imageUrl).into(imageView);

        TextView timeTextView = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#e0e0e0"));
        timeTextView.setTextSize(12f);
        timeTextView.setText(new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(new java.util.Date(timestamp)));

        bubbleLayout.addView(imageView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }

    public static void addSentMessageBubbleWithPdf(Context context, LinearLayout messagesContainer, ScrollView scroll, String pdfUrl, long timestamp) {
        LinearLayout outerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0,8);
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.END);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bubbleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginStart(60);
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12);
        bubbleLayout.setBackgroundResource(R.drawable.sent_message_bubble);

        TextView pdfTextView = new TextView(context);
        pdfTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        pdfTextView.setText("PDF Document");
        pdfTextView.setTextColor(Color.WHITE);
        pdfTextView.setTextSize(16f);
        pdfTextView.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(context, R.font.montserrat_medium));
        pdfTextView.setPaintFlags(pdfTextView.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        pdfTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
            context.startActivity(intent);
        });

        TextView timeTextView = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#e0e0e0"));
        timeTextView.setTextSize(12f);
        timeTextView.setText(new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(new java.util.Date(timestamp)));

        bubbleLayout.addView(pdfTextView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }

    public static void addReceivedMessageBubble(Context context, LinearLayout messagesContainer, ScrollView scroll, String messageText, long timestamp) {
        //LinearLayout messagesContainer = findViewById(R.id.messages_container);

        LinearLayout outerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0, 8);
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.START);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bubbleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginEnd(60); // match your sent version 60dp start offset
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12); // 12dp all around
        bubbleLayout.setBackgroundResource(R.drawable.received_message_bubble);

        TextView messageTextView = new TextView(context);
        messageTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        messageTextView.setText(messageText);
        messageTextView.setTextColor(Color.parseColor("#333333"));  // dark text
        messageTextView.setTextSize(16f);
        messageTextView.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_medium));

        TextView timeTextView = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#808080"));
        timeTextView.setTextSize(12f);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if (timestamp == 0) {
            timeTextView.setText(sdf.format(new Date()));
        } else {
            timeTextView.setText(sdf.format(new Date(timestamp)));
        }

        bubbleLayout.addView(messageTextView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        //ScrollView scroll = findViewById(R.id.messages_scroll);
        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }
}
