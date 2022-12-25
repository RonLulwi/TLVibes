package superapp.logic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.entities.UserEntity;
import superapp.data.enums.Role;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.UserConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.Guard;
import superapp.logic.interfaces.EnhancedUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class UserService implements EnhancedUsersService {
	
    private ConfigProperties configProperties;
    private UserConvertor convertor;
	private UserEntityRepository UsersRepository;

	@Autowired
	public UserService(ConfigProperties configProperties,UserConvertor convertor,UserEntityRepository UsersRepository)
	{
		this.configProperties = configProperties;
		this.convertor = convertor;
		this.UsersRepository = UsersRepository;
	}
	
	
	@Override
	@Transactional
	public UserBoundary createUser(NewUserBoundary user) {
		
		Guard.AgainstNull(user, user.getClass().getName());
		Guard.AgainstNull(user.getRole(), user.getRole().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getEmail(), user.getEmail().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getUsername(), user.getUsername().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getAvatar(), user.getAvatar().getClass().getName());

		Role.ValidateEnumThrowsIfNotExists(user.getRole());

		UserBoundary boundary = user.ToUserBoudary(new UserId());
		
		UserId userId = new UserId(configProperties.getSuperAppName(), user.getEmail());
		
		UserEntity entity = convertor.UserBoundaryToEntity(boundary,userId);
				
		var response = UsersRepository.save(entity);
		
		return convertor.UserEntityToBoundary(response);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSuperApp, String userEmail) {
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");

		UserEntity retrivedEntiry = GetUserEntityById(new UserId(userSuperApp,userEmail));
		
		return convertor.UserEntityToBoundary(retrivedEntiry);
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");

		if(!UsersRepository.existsById(update.getUserId()))
		{
			throw new RuntimeException("Could not find user with id : " + update.getUserId());
		}
		
		UserEntity updateAsEntity = convertor.UserBoundaryToEntity(update,update.getUserId());
		
		UserEntity returned = UsersRepository.save(updateAsEntity);

		return convertor.UserEntityToBoundary(returned);
	}


	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		return StreamSupport
				.stream(this.UsersRepository.findAll().spliterator(), false)
				.map(this.convertor::UserEntityToBoundary)
				.collect(Collectors.toList());
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(int size,int page) {
		return this.UsersRepository
				.findAll(PageRequest.of(page, size, Direction.DESC, "email","superapp"))
				.stream()
				.map(this.convertor::UserEntityToBoundary)
				.collect(Collectors.toList());
	}
	

	@Override
	@Transactional
	public void deleteAllUsers() {
		if(UsersRepository.count() == 0) {
			return;
		}
		
		UsersRepository.deleteAll();
	}
	
	private UserEntity GetUserEntityById(UserId id) {
		Optional<UserEntity> op = UsersRepository.findById(id);
		
		if (op.isEmpty()) {
			// TODO replace with exception that maps to HTTP Status 404 (not found)
			throw new RuntimeException("Could not find UserEntity by id: " + id);	
		}
		
		return op.get();

	}


}
