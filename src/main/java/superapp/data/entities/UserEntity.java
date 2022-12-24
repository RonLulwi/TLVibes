package superapp.data.entities;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import superapp.data.enums.Role;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.Utilities;

@Entity
public class UserEntity {
   	@EmbeddedId private UserId userId;
	private Role role;
	private String username;
	private String avatar;
	
	public UserEntity(UserId userId) {
		this();
		this.userId = userId;	
	}
	
	public UserEntity() {
		this.role = Role.SUPERAPP_USER;
		this.username = Utilities.GeneratingRandomString();
		this.avatar = Utilities.GeneratingRandomString(1);
	}
	
	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		return Objects.equals(userId, other.userId);
	}
	
	
}
