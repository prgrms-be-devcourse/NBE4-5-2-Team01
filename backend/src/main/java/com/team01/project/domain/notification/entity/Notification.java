package com.team01.project.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    private LocalDateTime notificationTime;

    @Column(nullable = false)
    private String message;

    private boolean isRead = false;

//    public Notification(User user, LocalDateTime notificationTime, String message) {
//        this.user = user;
//        this.notificationTime = notificationTime;
//        this.message = message;
//        this.isRead = false;
//    }
}
