package superapp.logic;

import java.util.List;

import superapp.logic.boundaries.UserBoundary;

public interface EnhancedUsersService extends UsersService {
	
	public List<UserBoundary> getAllUsers(String userSuperApp, String userEmail, int size,int page);
	
	void deleteAllUsers(String userSuperApp, String userEmail);

}
