package superapp.logic.boundaries;


import java.util.Objects;

import superapp.data.enums.Role;
import superapp.logic.boundaries.identifiers.UserId;

public class UserBoundary {

	private UserId userId;
	private Role role;
	private String username;
	private String avatar;
	
	public UserBoundary() {
		this.role = Role.MINIAPP_USER;
		this.username="Jane Roe";
		this.avatar="J";

	}
	
	public UserBoundary(UserId userId)
	{
		this();
		this.userId = userId;
	}

	
	public UserBoundary(UserId userId,Role role,String userName, String avatar)
	{
		this.userId = userId;
		this.role=role;
		this.username=userName;
		this.avatar=avatar;
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
		return "UserBoundary [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(avatar, role, userId, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBoundary other = (UserBoundary) obj;
		return Objects.equals(avatar, other.avatar) && role == other.role && Objects.equals(userId, other.userId)
				&& Objects.equals(username, other.username);
	}
	
	
}
