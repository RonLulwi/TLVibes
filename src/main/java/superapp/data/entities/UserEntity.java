package superapp.data.entities;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import superapp.data.enums.Role;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.Utilities;

@Entity
@IdClass(UserId.class)
public class UserEntity {
   	@Id private String superapp;
   	@Id private String email;
	private Role role;
	private String username;
	private String avatar;
	
	public UserEntity(UserId userId) {
		this();
		this.email = userId.getEmail();	
		this.superapp = userId.getSuperapp();	

	}
	
	public UserEntity() {
		this.role = Role.SUPERAPP_USER;
		this.username = Utilities.GeneratingRandomString();
		this.avatar = Utilities.GeneratingRandomString(1);
	}
	
	public UserId getUserId() {
		return new UserId(this.superapp,this.email);
	}
	
	public void setUserId(UserId userId) {
		this.email = userId.getEmail();	
		this.superapp = userId.getSuperapp();	
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
	public int hashCode() {
		return Objects.hash(avatar, email, role, superapp, username);
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
		return Objects.equals(avatar, other.avatar) && Objects.equals(email, other.email) && role == other.role
				&& Objects.equals(superapp, other.superapp) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", superapp=" + superapp + ", role=" + role + ", username=" + username
				+ ", avatar=" + avatar + "]";
	}
	
	
	
	
}
