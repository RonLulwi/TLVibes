package tlvibes.logic.boundaries;


import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.identifiers.UserId;

public class UserBoundary {

	private UserId userId;
	private Role role;
	private String username;
	private String avatar;
	
	public UserBoundary() {
		this.userId = new UserId();
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
	

	public Role getrole() {
		return role;
	}

	public void setrole(Role role) {
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

}
