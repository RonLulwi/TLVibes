package tlvibes.logic.boundaries;


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

	public  String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
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
	
	public UserBoundary ToUserBoudary(UserId userId)
	{
		UserBoundary user = new UserBoundary(userId);
		user.setUsername(this.username);
		user.setAvatar(this.avatar);
		user.setRole(this.role);
		return user;
	
	}
	
	

}