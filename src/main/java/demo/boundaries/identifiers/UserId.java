package demo.boundaries.identifiers;

public class UserId {
	String superApp;
	String email;

	public UserId() {
		superApp = "2023a.demo";
		email = "jane@demo.org";
	}
	
	public UserId(String superApp, String email) {
		this.superApp = superApp;	
		this.email = email;	
	}

	public String getSuperApp() {
		return superApp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}