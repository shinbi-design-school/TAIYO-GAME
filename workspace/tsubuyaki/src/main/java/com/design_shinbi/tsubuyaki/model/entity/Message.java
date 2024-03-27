package com.design_shinbi.tsubuyaki.model.entity;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int userId;
    private String text;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public Message() {
    }
    
    public Message(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
