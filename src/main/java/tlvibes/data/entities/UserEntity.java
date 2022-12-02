package tlvibes.data.entities;

import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.infrastructure.ImutableField;

public class UserEntity {
	@ImutableField
	private UserId userId;
	private Role role;
	private String username;
	private String avatar;
	
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
	
}
