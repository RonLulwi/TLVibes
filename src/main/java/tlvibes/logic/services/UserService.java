package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.NewUserBoundary;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.convertes.UserConvertor;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.infrastructure.ImutableField;
import tlvibes.logic.interfaces.UsersService;

@Service
public class UserService implements UsersService {
	
    private ConfigProperties configProperties;
    private UserConvertor convertor;
	private List<UserEntity> UserEntities;

	@Autowired
	public UserService(ConfigProperties configProperties,UserConvertor convertor)
	{
		this.configProperties = configProperties;
		this.convertor = convertor;
	}
	
	@PostConstruct
	public void init() {
		UserEntities = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public UserBoundary createUser(NewUserBoundary user) {
		
		Guard.AgainstNull(user, user.getClass().getName());
		
		UserBoundary boundary = user.ToUserBoudary(new UserId(configProperties.getSuperAppName(), user.getemail()));
		
		var entity = convertor.UserBoundaryToEntity(boundary);
				
		UserEntities.add(entity);
		
		return boundary;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");

		UserEntity retrivedEntiry = GetUserEntityById(new UserId(userSuperApp,userEmail));
		
		return convertor.UserEntityToBoundary(retrivedEntiry);
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");

		UserEntity updateAsEntity = convertor.UserBoundaryToEntity(update);
		
		UserEntity currentUser = GetUserEntityById(new UserId(userSuperApp,userEmail));
		
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
		System.err.println(updatedUser);
		
		return updatedUser;
	}


	@Override
	public List<UserBoundary> getAllUsers() {
		return this.UserEntities.stream().map(this.convertor::UserEntityToBoundary).collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers() {
		 UserEntities = Collections.synchronizedList(new ArrayList<>());
	}
	
	private UserEntity GetUserEntityById(UserId id) {
		return UserEntities.stream().filter(e -> e.getUserId().equals(id)).findFirst().get();	
	}


}
