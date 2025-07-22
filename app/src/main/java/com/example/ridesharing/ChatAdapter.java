package com.example.ridesharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private String currentUserId;


    public ChatAdapter(List<ChatMessage> chatMessages, String currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return 1;  // Current user's message
        } else {
            return 0;  // Other user's message
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // Inflate layout for the current user's message (right aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        } else {
            // Inflate layout for other users' messages (left aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item2, parent, false);
        }
        return new ChatViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.getImageUrl() != null) {
            // Display the image
            holder.messageTextView.setVisibility(View.GONE);
            holder.messageImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(message.getImageUrl()).into(holder.messageImageView);
        } else {
            // Display the text message
            holder.messageImageView.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(message.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private ImageView messageImageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
            messageImageView = itemView.findViewById(R.id.message_image);
        }
    }

}

