package demo.boundaries;

import demo.boundaries.identifiers.UserId;
import demo.enums.Role;

public class UserBoundary {
	private UserId userId;
	private String role;
	private String username;
	private String avatar;
	
	public UserBoundary() {
		this.userId = new UserId();
		this.role=Role.STUDENT;
		this.username="Jane Roe";
		this.avatar="J";

	}
	
	public UserBoundary(UserId userId)
	{
		this();
		this.userId = userId;
	}

	
	public UserBoundary(UserId userId,String role,String userName, String avatar)
	{
		this.userId = userId;
		this.role=role;
		this.username=userName;
		this.avatar=avatar;
	}
	

	public String getrole() {
		return role;
	}

	public void setrole(String role) {
		this.role = role;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getavatar() {
		return avatar;
	}

	public void setavatar(String avatar) {
		this.avatar = avatar;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}
}
