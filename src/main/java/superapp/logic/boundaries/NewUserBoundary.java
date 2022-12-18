package tlvibes.logic.boundaries;


import java.util.Objects;

import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.identifiers.UserId;

public class NewUserBoundary {
	
	private String email;
	private Role role;
	private String username;
	private String avatar;
	
	public NewUserBoundary() {
		this.avatar="J";
		this.role=Role.MINIAPP_USER;

	}

	public NewUserBoundary(String superApp,String email) {
		this();
		this.email=email;
		this.username="Jane Roe";
	}

	public  String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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