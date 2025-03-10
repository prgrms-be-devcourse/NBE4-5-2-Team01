package com.team01.project.domain.user.entity;

import com.team01.project.domain.notification.entity.Notification;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "USER")
public class User {
	@Id
	@Column(name = "USER_ID")
	private String id;

	@Email
	@Column(name = "USER_EMAIL")
	private String email;

	@Column(name = "NAME")
	private String name;

	@Column(name = "Nickname")
	private String nickName;

	@Column(name = "birthday")
	private LocalDate birthDay;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdDate;

	@Column(name = "Field")
	private String field;

	@Column(name = "follow_id")
	private long followId;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RefreshToken> refreshTokens;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<>();
}
