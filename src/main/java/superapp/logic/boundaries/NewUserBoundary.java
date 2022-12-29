package superapp.logic.boundaries;


import java.util.Objects;

import superapp.data.UserRole;
import superapp.logic.boundaries.identifiers.UserId;

public class NewUserBoundary {
	
	private String email;
	private UserRole role;
	private String username;
	private String avatar;
	
	public NewUserBoundary() {
	}

	public NewUserBoundary(String email) {
		this();
		this.email=email;
	}

	public  String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
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
	
	public UserBoundary ToUserBoudary(UserId userId)
	{
		UserBoundary user = new UserBoundary(userId);
		user.setUsername(this.username);
		user.setAvatar(this.avatar);
		user.setRole(this.role);
		return user;
	
	}

	@Override
	public int hashCode() {
		return Objects.hash(avatar, email, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewUserBoundary other = (NewUserBoundary) obj;
		return Objects.equals(avatar, other.avatar) && Objects.equals(email, other.email) && role == other.role
				&& Objects.equals(username, other.username);
	}
	
	
	

}