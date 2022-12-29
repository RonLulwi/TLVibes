package superapp.logic.interfaces;

import java.util.List;

import superapp.logic.boundaries.UserBoundary;

public interface EnhancedUsersService extends UsersService {
	
	public List<UserBoundary> getAllUsers(int size,int page);

}
