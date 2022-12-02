package tlvibes.logic.boundaries.identifiers;

import tlvibes.logic.infrastructure.ImutableObject;

@ImutableObject
public class UserId {
	String superApp;
	String email;

	public UserId() {
		superApp = "2023a.demo";
		email = "jane@demo.org";
	}
	
	public UserId(String superApp, String email) {
		this();
		
		if(superApp != null) {
			this.superApp = superApp;				
		}
		
		if(email != null)
		{			
			this.email = email;	
		}
	}

	public String getSuperApp() {
		return superApp;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "UserId [superApp=" + superApp + ", email=" + email + "]";
	}


}
