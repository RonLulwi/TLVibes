package superapp.data;

public enum UserRole {
	MINIAPP_USER,
	SUPERAPP_USER,
	ADMIN,
	;

	//TODO: extract to separate class
	public static void ValidateEnumThrowsIfNotExists(UserRole role) {
		
		boolean isExist = false;
		
	    for (UserRole r : UserRole.values()) {
	        if (r.name().equals(role.toString())) {
	        	isExist =  true;
	        	break;
	        }
	    }
	    
	    if(!isExist) {
	    	throw new IllegalArgumentException(role.toString());
	    }
		
	}
}
