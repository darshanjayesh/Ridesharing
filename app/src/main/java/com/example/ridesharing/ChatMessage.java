package com.example.ridesharing;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private long timestamp;
    private String imageUrl;  // New field for image URL

    // Empty constructor for Firebase
    public ChatMessage() {
    }

    // Constructor for text messages
    public ChatMessage(String senderId, String receiverId, String message, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = null;  // No image
    }

    // Constructor for image messages
    public ChatMessage(String senderId, String receiverId, long timestamp, String imageUrl) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = "";  // No text message
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


