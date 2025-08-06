package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationDao extends JpaRepository<Notification, UUID> {
}
