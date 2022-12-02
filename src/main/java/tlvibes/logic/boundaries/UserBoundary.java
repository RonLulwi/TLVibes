package tlvibes.boundaries;


import tlvibes.boundaries.identifiers.UserId;
import tlvibes.enums.Role;

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
}
