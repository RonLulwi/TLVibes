package tlvibes.data.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.infrastructure.Utilities;

@Entity
public class UserEntity {
    @EmbeddedId	private UserId userId;
	private Role role;
	private String username;
	private String avatar;
	
	public UserEntity(UserId userId) {
		this();
		this.userId = userId;	
	}
	
	public UserEntity() {
		this.role = Role.UNDEFINED;
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
	
}
