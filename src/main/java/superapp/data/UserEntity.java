package superapp.data;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.Utilities;

@Entity
@IdClass(UserId.class)
public class UserEntity {
   	@Id private String superapp;
   	@Id private String email;
	private UserRole role;
	private String username;
	private String avatar;
	
	public UserEntity(UserId userId) {
		this();
		this.email = userId.getEmail();	
		this.superapp = userId.getSuperapp();	

	}
	
	public UserEntity() {
		this.role = UserRole.SUPERAPP_USER;
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

	@Override
	public int hashCode() {
		return Objects.hash(email, superapp);
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
		return Objects.equals(email, other.email) && Objects.equals(superapp, other.superapp);
	}

	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", superapp=" + superapp + ", role=" + role + ", username=" + username
				+ ", avatar=" + avatar + "]";
	}
	
	
	
	
}
