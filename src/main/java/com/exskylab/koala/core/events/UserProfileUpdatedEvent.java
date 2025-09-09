package com.exskylab.koala.core.events;

import com.exskylab.koala.entities.User;

public class UserProfileUpdatedEvent {
    private final User updatedUser;

    public UserProfileUpdatedEvent(User updatedUser) {
        this.updatedUser = updatedUser;
    }

    public User getUpdatedUser() {
        return updatedUser;
    }
}
