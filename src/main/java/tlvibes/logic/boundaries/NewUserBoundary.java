package tlvibes.logic.boundaries;


import tlvibes.data.enums.Role;

public class NewUserBoundary {
	
	private String email;
	private Role role;
	private String username;
	private String avatar;
	
	public NewUserBoundary() {
		
	}

	public NewUserBoundary(String superApp,String email) {
		this.email=email;
		this.role=Role.MINIAPP_USER;
		this.username="Jane Roe";
		this.avatar="J";
		
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
	
	

}
