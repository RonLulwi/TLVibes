package superapp.logic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.EnhancedUsersService;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.convertes.UserConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.DeprecatedFunctionException;
import superapp.logic.infrastructure.Guard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class UserService implements EnhancedUsersService {
	
    private ConfigProperties configProperties;
    private UserConvertor convertor;
	private UserEntityRepository usersRepository;


	@Autowired
	public UserService(ConfigProperties configProperties,UserConvertor convertor,UserEntityRepository UsersRepository)
	{
		this.configProperties = configProperties;
		this.convertor = convertor;
		this.usersRepository = UsersRepository;

	}
	
	
	@Override
	@Transactional
	public UserBoundary createUser(NewUserBoundary user) {
		
		Guard.AgainstNull(user, user.getClass().getName());
		Guard.AgainstNull(user.getRole(), user.getRole().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getEmail(), user.getEmail().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getUsername(), user.getUsername().getClass().getName());
		Guard.AgainstNullOrEmpty(user.getAvatar(), user.getAvatar().getClass().getName());

		UserRole.ValidateEnumThrowsIfNotExists(user.getRole());

		UserBoundary boundary = user.ToUserBoudary(new UserId());
		
		UserId userId = new UserId(configProperties.getSuperAppName(), user.getEmail());
		
		UserEntity entity = convertor.toEntity(boundary,userId);
				
		var response = usersRepository.save(entity);
		
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
				this.usersRepository.
				findById(new UserId(userSuperApp,userEmail))
				.orElseThrow(() -> 
				new EntityNotFoundException("Could not find user with id : " + 
						new UserId(userSuperApp,userEmail)))
				.getUserId()
				);
				
		UserEntity returned = usersRepository.save(updateAsEntity);

		return convertor.toBoundary(returned);
	}


	@Override
	//@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		throw new DeprecatedFunctionException("Unsupported paging getAllUsers function is deprecated ");
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String userSuperApp, String userEmail, int size,int page) {
		
		UserBoundary user = login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.ADMIN)
			throw new UnAuthoriezedRoleRequestException("Only ADMIN has permission!");
		
		return this.usersRepository
				.findAll(PageRequest.of(page, size, Direction.DESC, "email","superapp"))
				.stream()
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());
	}
	

	
	@Override
	@Transactional
	public void deleteAllUsers() {
		throw new DeprecatedFunctionException("Unsupported paging deleteAllUsers function is deprecated ");
	}
	
	
	private UserEntity GetUserEntityById(UserId id) {
		
		Optional<UserEntity> op = usersRepository.findById(id);
		if (op.isEmpty()) 
			throw new RuntimeException("Could not find UserEntity by id: " + id);	
		
		return op.get();

	}


	@Override
	public void deleteAllUsers(String userSuperApp, String userEmail) {
		
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.usersRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.ADMIN) {
			throw new UnAuthoriezedRoleRequestException("Only ADMIN user can delete users!");			
		}
		
		usersRepository.deleteAll();
		
	}
	
	
	@Transactional(readOnly = true)
	public void validateObjectActive(String objectSuperApp, String internalObjectId
			, SuperAppObjectRepository objectsRepositoy,
			ObjectConvertor convertor) {	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		Optional<SuperAppObjectEntity> optional = objectsRepositoy.findById(objectId);
		if(optional.isEmpty())
			throw new RuntimeException("could not find superAppObject with id : " + objectId.toString());
		
		
		 if(!optional.get().getActive())
			 throw new MissingCommandOnPostRequestException("Target object is not Active");
		
	}


}
