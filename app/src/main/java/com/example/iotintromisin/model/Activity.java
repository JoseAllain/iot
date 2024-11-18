package com.example.iotintromisin.model;

import java.time.LocalDateTime;

public class Activity {
    private Long id;

    private String description;
    private String timestamp;

    public Activity(Long id, String description, String timestamp) {
        this.id = id;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
