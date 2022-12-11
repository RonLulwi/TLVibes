package tlvibes.logic.boundaries.identifiers;


import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserId implements Serializable{
    private static final long serialVersionUID = 1L;
    private String superApp;
    private String email;
	
	public UserId() {
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

	public void setSuperApp(String superApp) {
		this.superApp = superApp;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserId [superApp=" + superApp + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		UserId userId = (UserId) obj;
        return Equals(userId);
	}

	private boolean Equals(UserId userId) {
		return this.superApp.equals(userId.getSuperApp()) &&
				this.email.equals(userId.getEmail());
	}

	

}
