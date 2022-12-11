package tlvibes.logic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tlvibes.data.entities.UserEntity;
import tlvibes.data.interfaces.UserEntityRepository;
import tlvibes.logic.boundaries.NewUserBoundary;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.convertes.UserConvertor;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.interfaces.UsersService;



@Service
public class UserService implements UsersService {
	
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
		
		UserBoundary boundary = user.ToUserBoudary(new UserId(configProperties.getSuperAppName(), user.getEmail()));
		
		UserEntity entity = convertor.UserBoundaryToEntity(boundary);
				
		UsersRepository.save(entity);
		
		return boundary;
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

		UserEntity updateAsEntity = convertor.UserBoundaryToEntity(update);
		
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
