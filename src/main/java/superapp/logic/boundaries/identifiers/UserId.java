package superapp.logic.boundaries.identifiers;


import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserId implements Serializable{
    private static final long serialVersionUID = 1L;
    private String superapp;
    private String email;
	
	public UserId() {
	}

	public UserId(String superApp, String email) {
		this.superapp = superApp;				
		this.email = email;	
		
	}
	
	public String getSuperapp() {
		return superapp;
	}
	public String getEmail() {
		return email;
	}

	public void setSuperapp(String superApp) {
		this.superapp = superApp;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserId [superApp=" + superapp + ", email=" + email + "]";
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
		return this.superapp.equals(userId.getSuperapp()) &&
				this.email.equals(userId.getEmail());
	}

	

}
