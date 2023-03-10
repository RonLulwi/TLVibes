package superapp.logic;

import java.util.List;

import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;

public interface UsersService {
	
	public UserBoundary createUser(NewUserBoundary user);
	
	public UserBoundary login(String userSuperApp , String userEmail);
	
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update);
	
	@Deprecated
	List<UserBoundary> getAllUsers();
	
	@Deprecated
	void deleteAllUsers();

}
