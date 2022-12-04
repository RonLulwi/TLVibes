package tlvibes.logic.boundaries.identifiers;

import org.springframework.beans.factory.annotation.Autowired;
import tlvibes.logic.infrastructure.ImutableObject;

@ImutableObject
public class UserId {
	
	private String superApp;
	private String email;

	@Autowired
	public UserId() {
		this.email = "jane@demo.org";
	}
	
	public UserId(String email) {
		this.email = email;
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
	
	public void setSuperApp(String superApp) {
		this.superApp = superApp;
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
