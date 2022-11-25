package demo.boundaries;

import java.util.HashMap;

import demo.enums.Role;

public class UserBoundary {
	
	
	
	private HashMap<String,String> userId;
	private String role;
	private String username;
	private String avatar;
	
	public UserBoundary() {
		
	}

	public UserBoundary(String superApp,String email) {
		this.userId=new HashMap<>();
		this.userId.put("superapp", superApp);
		this.userId.put("email", email);
		this.role=Role.STUDENT;
		this.username="Jane Roe";
		this.avatar="J";
		
	}

	public HashMap<String, String> getuserId() {
		return userId;
	}

	public void setuserId(HashMap<String, String> userId) {
		this.userId = userId;
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
	
	
	

}
