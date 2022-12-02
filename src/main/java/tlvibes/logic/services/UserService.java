package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.convertes.UserConvertor;
import tlvibes.logic.infrastructure.ImutableField;
import tlvibes.logic.interfaces.UsersService;

public class UserService implements UsersService {
	
	List<UserEntity> Users = Collections.synchronizedList(new ArrayList<>());
	UserConvertor convertor = new UserConvertor();

	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		var entity = convertor.UserBoundaryToEntity(user);
		Users.add(entity);
		return user;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		
		var currentUser = GetUserEntityBy(userSuperApp,userEmail);
		
		
		
		for (var field : currentUser.getClass().getFields()) {
			if(field.getAnnotation(ImutableField.class) == null)
			{
				try {
					field.set(currentUser, field.get(update));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		
		return null;
	}

	private UserEntity GetUserEntityBy(String userSuperApp, String userEmail) {
		return Users.get(0);
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

}
