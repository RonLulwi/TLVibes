package superapp.logic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.entities.UserEntity;
import superapp.data.enums.Role;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.UserConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.DeprecatedFunctionException;
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
		
		UserEntity entity = convertor.toEntity(boundary,userId);
				
		var response = UsersRepository.save(entity);
		
		return convertor.toBoundary(response);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSuperApp, String userEmail) {
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");

		UserEntity retrivedEntiry = GetUserEntityById(new UserId(userSuperApp,userEmail));
		
		return convertor.toBoundary(retrivedEntiry);
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {
		
		Guard.AgainstNull(userSuperApp, "userSuperApp");
		Guard.AgainstNull(userEmail, "userEmail");		
		

		UserEntity updateAsEntity = convertor.toEntity(update,
				this.UsersRepository.
				findById(new UserId(userSuperApp,userEmail))
				.orElseThrow(() -> 
				new EntityNotFoundException("Could not find user with id : " + 
						new UserId(userSuperApp,userEmail)))
				.getUserId()
				);
				
		UserEntity returned = UsersRepository.save(updateAsEntity);

		return convertor.toBoundary(returned);
	}


	@Override
	//@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		throw new DeprecatedFunctionException("Unsupported paging getAllUsers function is deprecated ");
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(int size,int page) {
		return this.UsersRepository
				.findAll(PageRequest.of(page, size, Direction.DESC, "email","superapp"))
				.stream()
				.map(this.convertor::toBoundary)
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
