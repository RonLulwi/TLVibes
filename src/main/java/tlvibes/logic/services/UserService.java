package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.convertes.UserConvertor;
import tlvibes.logic.infrastructure.ImutableField;
import tlvibes.logic.infrastructure.ImutableObject;
import tlvibes.logic.interfaces.UsersService;

public class UserService implements UsersService {
	
	List<UserEntity> UserEntities = Collections.synchronizedList(new ArrayList<>());
	List<UserBoundary> UserBoundaries = Collections.synchronizedList(new ArrayList<>());
	UserConvertor convertor = new UserConvertor();

	@Override
	public UserBoundary createUser(UserBoundary user) {
		var entity = convertor.UserBoundaryToEntity(user);
		UserEntities.add(entity);
		UserBoundaries.add(user);
		return user;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		
		UserEntity updateAsEntity = convertor.UserBoundaryToEntity(update);
		
		UserEntity currentUser = GetUserEntityBy(userSuperApp,userEmail);
		
		for (var field : currentUser.getClass().getDeclaredFields()) {
			if(!(field.isAnnotationPresent(ImutableField.class)))
			{
			    field.setAccessible(true);
				try {
					field.set(currentUser, field.get(updateAsEntity));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		UserBoundary updatedUser = convertor.UserEntityToBoundary(currentUser);
		
		if(UserBoundaries.removeIf(user -> user.getUserId().equals(updatedUser.getUserId())))
		{
			UserBoundaries.add(updatedUser);
		}
		
		return updatedUser;
	}

	private UserEntity GetUserEntityBy(String userSuperApp, String userEmail) {
		return UserEntities.get(0);
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return UserBoundaries;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

}
