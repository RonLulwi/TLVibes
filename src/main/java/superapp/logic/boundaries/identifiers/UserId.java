package superapp.logic.boundaries.identifiers;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;


//@Entity
public class UserId implements Serializable{
    private static final long serialVersionUID = 1L;

	/*@Id*/ private String email;
    /*@Id*/ private String superapp;
	
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
		return "userId [superApp=" + superapp + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, superapp);
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
