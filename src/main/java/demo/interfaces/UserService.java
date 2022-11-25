package demo.interfaces;

import java.util.List;

import demo.boundaries.UserBoundary;

public interface UserService {
	
	public UserBoundary createUser(UserBoundary user);
	
	public UserBoundary login(String userSuperApp , String userEmail);
	
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update);
	
	List<UserBoundary> getAllUsers();
	
	void deleteAllUsers();

}
