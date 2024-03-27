package com.design_shinbi.tsubuyaki.model;

import com.design_shinbi.tsubuyaki.model.entity.Image;
import com.design_shinbi.tsubuyaki.model.entity.Message;
import com.design_shinbi.tsubuyaki.model.entity.User;

public class PostInfo {
    private User user;
    private Message message;
    private Image image;

    public PostInfo(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
