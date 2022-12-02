package tlvibes.logic.interfaces;

import java.util.List;

import tlvibes.logic.boundaries.UserBoundary;

public interface UsersService {
	
	public UserBoundary createUser(UserBoundary user);
	
	public UserBoundary login(String userSuperApp , String userEmail);
	
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update);
	
	List<UserBoundary> getAllUsers();
	
	void deleteAllUsers();

}
