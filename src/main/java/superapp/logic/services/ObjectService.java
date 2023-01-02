package superapp.logic.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.data.enums.CreationEnum;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.EnhancedObjectsService;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.DeprecatedFunctionException;
import superapp.logic.infrastructure.Guard;
import superapp.logic.infrastructure.IdGenerator;

@Service
public class ObjectService implements EnhancedObjectsService {

	private ConfigProperties configProperties;
	private ObjectConvertor convertor;
	private IdGenerator idGenerator;
	private SuperAppObjectRepository objectsRepository;
	private UserEntityRepository userRepository;
	private UserService userService;

	@Autowired
	public ObjectService(ObjectConvertor convertor,ConfigProperties configProperties,
			IdGenerator idGenerator, SuperAppObjectRepository objectsRepository,UserService userService,
			UserEntityRepository userRepository) {
		this.convertor = convertor;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
		this.objectsRepository = objectsRepository;
		this.userRepository = userRepository;
		this.userService = userService;

	}



	@Override
	//@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {
		throw new DeprecatedFunctionException("Unsupported paging updateObject function is deprecated ");
	}


	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String userSuperApp, String userEmail, String objectSuperApp, String internalObjectId) {	

		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.MINIAPP_USER && 
				userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only MINIAPP_USER and SUPERAPP_USER has permission!");			
		}
		
		if(userEntity.get().getRole() == UserRole.MINIAPP_USER)
			userService.validateObjectActive(objectSuperApp, internalObjectId, objectsRepository, convertor);
			
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		
		Optional<SuperAppObjectEntity> optional = this.objectsRepository.findById(objectId);
		
		if(optional.isEmpty())
			throw new RuntimeException("could not find superAppObject with id : " + objectId.toString());
		
		
		return convertor.toBoundary(optional.get());

	}
	
	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {	
		throw new DeprecatedFunctionException("Unsupported paging getSpecificObject function is deprecated ");
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(String userSuperApp, String userEmail, int page, int size) {
		
		UserBoundary user = userService.login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.MINIAPP_USER && user.getRole() != UserRole.SUPERAPP_USER)
			throw new UnAuthoriezedRoleRequestException("Only MINIAPP_USER and SUPERAPP_USER has permission!");
		
		boolean addNonActive = (user.getRole() == UserRole.SUPERAPP_USER);
		return (addNonActive) ?
				this.objectsRepository
				.findAll(PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(entity -> convertor.toBoundary(entity)).collect(Collectors.toList())
				:
				this.objectsRepository
				.findAllByActive(true, PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(this.convertor::toBoundary).collect(Collectors.toList());
	}


	@Override
	//@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		throw new DeprecatedFunctionException("Unsupported paging getAllObjects function is deprecated ");
	}
	@Override
	@Transactional
	public void deleteAllObjects() {
		throw new DeprecatedFunctionException("Unsupported paging deleteAllObjects function is deprecated ");
	}
	
	@Override
	@Transactional
	public void deleteAllObjects(String userSuperApp, String userEmail) {
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.ADMIN) {
			throw new UnAuthoriezedRoleRequestException("Only ADMIN user can delete users!");			
		}
		this.objectsRepository.deleteAll();
		
	}

	@Override
	@Transactional
	public void BindExistingObjectToAnExisitingChild(String userSuperApp, String userEmail, String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary childId) {
		Guard.AgainstNull(childId, childId.getClass().getName());
		Guard.AgainstNull(parentSuperApp, parentSuperApp);
		Guard.AgainstNull(parentInternalId, parentInternalId);
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_USER has permission!");			
		}
		
		
		var parentId = new SuperAppObjectIdBoundary(parentSuperApp,parentInternalId);

		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepository.findById(parentId);

		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, parentId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity parent = optionalParentEntity.get();

		Optional<SuperAppObjectEntity> optionalChildEntity =  this.objectsRepository.findById(childId);

		Guard.AgainstNullOptinalIdNotFound(optionalChildEntity, childId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity child = optionalChildEntity.get();

		parent.BindChild(child);

		child.setParent(parent);

		objectsRepository.save(parent);

	}


	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllChildrens(String userSuperApp, String userEmail, String superApp, String internalId, int page, int size) {
		
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.MINIAPP_USER && 
				userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only MINIAPP_USER and SUPERAPP_USER has permission!");			
		}
		
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);
		Optional<SuperAppObjectEntity> optionalEntity =  this.objectsRepository.findById(entityId);
		Guard.AgainstNullOptinalIdNotFound(optionalEntity, optionalEntity.toString(), SuperAppObjectEntity.class.getName());
		SuperAppObjectEntity entity = optionalEntity.get();
		
		boolean addNonActive = (userEntity.get().getRole() == UserRole.SUPERAPP_USER);
		return (addNonActive) ?
				this.objectsRepository
				.findAllByParent(entity,PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(this.convertor::toBoundary).collect(Collectors.toList())
				:
				this.objectsRepository
				.findAllByParentAndActive(entity, true, PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(this.convertor::toBoundary).collect(Collectors.toList());		
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getParent(String userSuperApp, String userEmail, String superApp, String internalId,int page,int size) {
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.MINIAPP_USER && 
				userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only MINIAPP_USER and SUPERAPP_USER has permission!");			
		}
				
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);

		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepository.findById(entityId);

		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, entityId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity parent = optionalParentEntity.get();

		
		boolean addNonActive = (userEntity.get().getRole() == UserRole.SUPERAPP_USER);
		return (addNonActive) ?
				this.objectsRepository
				.findAllByChildrens(parent,PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(this.convertor::toBoundary).collect(Collectors.toList())
				:
				this.objectsRepository
				.findAllByChildrensAndActive(parent, true, PageRequest.of(page, size, Direction.DESC, "objectId")).stream().map(this.convertor::toBoundary).collect(Collectors.toList());		

	}

	@Override
	@Transactional(readOnly = true)
	public Set<ObjectBoundary> searchObjectsByCreationTimeStamp(String userSuperApp, String userEmail, CreationEnum creation, int page, int size) {
		
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.MINIAPP_USER && 
				userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only MINIAPP_USER and SUPERAPP_USER has permission!");			
		}
		Instant oneCreationUnitAgo = Instant.now().minus(1, CreationEnum.MapCreationEnumToChronoUnit(creation));
		
		List<SuperAppObjectEntity> entities;
		if(userEntity.get().getRole() == UserRole.MINIAPP_USER)
			 entities = objectsRepository.findBycreationTimestampAfterAndActive(true, oneCreationUnitAgo);
		else
			entities = objectsRepository.findBycreationTimestampAfter(oneCreationUnitAgo);
		
		return entities.stream()
				.map(e -> convertor.toBoundary(e))
				.collect(Collectors.toSet());
	}


	private void validateObjectBoundary(ObjectBoundary objWithoutId) throws RuntimeException {
		Map<String, Object> request;
		try {
			request = Map.of(objWithoutId.getClass().getName() ,objWithoutId,
					objWithoutId.getClass().getDeclaredField("createdBy").toString(),objWithoutId.getCreatedBy(),
					objWithoutId.getClass().getDeclaredField("active").toString(),objWithoutId.getActive(),
					objWithoutId.getClass().getDeclaredField("type").toString(),objWithoutId.getType(),
					objWithoutId.getClass().getDeclaredField("alias").toString(),objWithoutId.getAlias()
					);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}

		Guard.AgainstNullRequest(request);
	}


	@Override
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, 
			ObjectBoundary objectBoundary, String userSuperApp, String userEmail) {	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNullOrEmpty(userSuperApp, userSuperApp);
		Guard.AgainstNullOrEmpty(userEmail, userEmail);
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		UserId userId = new UserId(userSuperApp, userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_APP user can update objects!");			
		}

		if(!objectsRepository.existsById(objectId))
			throw new EntityNotFoundException("Could not find object with id : " + objectId);
		
		
		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,objectId);
		var oldUserId = new HashMap<String, UserId>();
		oldUserId.put("userId", userId);
		EntityUpdate.setCreatedBy(oldUserId);

		SuperAppObjectEntity returned = this.objectsRepository.save(EntityUpdate);

		return convertor.toBoundary(returned);
	}


	@Override
	public List<ObjectBoundary> getAllObjectsByType(String type, String userSuperApp, String userEmail, int page, int size) {

		//USER_SUPERAPP , MINI_APPUSER - only active
		Guard.AgainstNull(type, type);
		Guard.AgainstNull(userSuperApp, userSuperApp);
		Guard.AgainstNull(userEmail, userEmail);

		UserId userId = new UserId(userSuperApp,userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity == null)
			new EntityNotFoundException("Could not find user with id : " + userId);

		UserRole userRole = userEntity.get().getRole();
		if(userRole!= UserRole.MINIAPP_USER && userRole!= UserRole.SUPERAPP_USER)
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_USER and MINIAPP_USER can retrieve objects");

		boolean addNonActive = (userRole == UserRole.SUPERAPP_USER);

		List<SuperAppObjectEntity> allObjects = (addNonActive) ?
				this.objectsRepository
				.findAllByType(type,PageRequest.of(page, size, Direction.DESC, "objectId"))
				:
				this.objectsRepository
				.findAllByTypeAndActive(type,true,PageRequest.of(page, size, Direction.DESC, "objectId"));
		
		return allObjects
				.stream()
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());

	}

	@Override
	public List<ObjectBoundary> getAllObjectsByAlias(String alias, String userSuperApp, String userEmail, int page, int size) {
		
		Guard.AgainstNull(alias, alias);
		Guard.AgainstNull(userSuperApp, userSuperApp);
		Guard.AgainstNull(userEmail, userEmail);

		UserId userId = new UserId(userSuperApp,userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity == null)
			new EntityNotFoundException("Could not find user with id : " + userId);

		UserRole userRole = userEntity.get().getRole();
		if(userRole!= UserRole.MINIAPP_USER && userRole!= UserRole.SUPERAPP_USER)
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_USER and MINIAPP_USER can retrieve objects");

		boolean addNonActive = (userRole == UserRole.SUPERAPP_USER);

		List<SuperAppObjectEntity> allObjects = (addNonActive) ?
				this.objectsRepository
				.findAllByAlias(alias,PageRequest.of(page, size, Direction.DESC, "objectId"))
				:
				this.objectsRepository
				.findAllByAliasAndActive(alias,true,PageRequest.of(page, size, Direction.DESC, "objectId"));
		
		return allObjects
				.stream()
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());
	}


	@Override
	public List<ObjectBoundary> getAllObjectsByAliasContainingText(String text, String userSuperApp, String userEmail, int page, int size) {
		
		Guard.AgainstNull(text, text);
		Guard.AgainstNull(userSuperApp, userSuperApp);
		Guard.AgainstNull(userEmail, userEmail);

		UserId userId = new UserId(userSuperApp,userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity == null)
			new EntityNotFoundException("Could not find user with id : " + userId);

		UserRole userRole = userEntity.get().getRole();
		if(userRole!= UserRole.MINIAPP_USER && userRole!= UserRole.SUPERAPP_USER)
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_USER and MINIAPP_USER can retrieve objects");

		boolean addNonActive = (userRole == UserRole.SUPERAPP_USER);

		List<SuperAppObjectEntity> allObjects = (addNonActive) ?
				this.objectsRepository
				.findAllByAliasContaining(text,PageRequest.of(page, size, Direction.DESC, "objectId"))
				:
				this.objectsRepository
				.findAllByAliasContainingAndActive(text,true,PageRequest.of(page, size, Direction.DESC, "objectId"));
		
		return allObjects
				.stream()
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());
	}


	@Override
	public ObjectBoundary createObject(ObjectBoundary objWithoutId) {
		
		validateObjectBoundary(objWithoutId);
		
		UserId userId = objWithoutId.getCreatedBy().get("userId");
		Optional<UserEntity> userEntity = this.userRepository.findById(userId);
		if(userEntity==null) {
			throw new EntityNotFoundException("No user with id " + userId);
		}
		
		if(userEntity.get().getRole() != UserRole.SUPERAPP_USER) {
			throw new UnAuthoriezedRoleRequestException("Only SUPERAPP_APP user can create objects!");			
		}
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(
				configProperties.getSuperAppName(),
				idGenerator.GenerateUUID().toString());


		SuperAppObjectEntity entity = convertor.toEntity(objWithoutId,objectId);

		var id = new SuperAppObjectIdBoundary(configProperties.getSuperAppName(),idGenerator.GenerateUUID().toString());

		entity.setObjectId(id);

		SuperAppObjectEntity returned = this.objectsRepository.save(entity);

		return convertor.toBoundary(returned);
	}





	










}
