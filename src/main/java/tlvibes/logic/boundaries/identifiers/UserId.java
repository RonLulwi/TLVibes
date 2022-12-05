package tlvibes.logic.boundaries.identifiers;


import tlvibes.logic.infrastructure.ImutableObject;

@ImutableObject
public class UserId{
	
	private String superApp;
	private String email;
	
	public UserId() {
	}

	public UserId(String superApp, String email) {
		this.superApp = superApp;				
		this.email = email;	
		
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
