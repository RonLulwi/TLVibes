package superapp.data.enums;

public enum Role {
	MINIAPP_USER,
	SUPERAPP_USER,
	ADMIN,
	;

	public static void ValidateEnumThrowsIfNotExists(Role role) {
		
		boolean isExist = false;
		
	    for (Role r : Role.values()) {
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
