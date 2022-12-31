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

	@Autowired
	public ObjectService(ObjectConvertor convertor,ConfigProperties configProperties,
			IdGenerator idGenerator, SuperAppObjectRepository objectsRepository,
			UserEntityRepository userRepository) {
		this.convertor = convertor;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
		this.objectsRepository = objectsRepository;
		this.userRepository = userRepository;
	}


	@Override
	@Transactional
	public ObjectBoundary createObject(ObjectBoundary objWithoutId) {

		validateObjectBoundary(objWithoutId);

		UserId userId = objWithoutId.getCreatedBy().get("userId");
		Optional<UserEntity> userEntity = this.userRepository.findById(userId); 
		if(userEntity == null) {
			throw new EntityNotFoundException("Could not find user with id : " + userId);
		}
		if(userEntity.get().getRole() != UserRole.SUPERAPP_USER)
		{
			throw new UnAuthoriezedRoleRequestException("Only SuperApp User can create objects");
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



	@Override
	//@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {

		//		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		//		Guard.AgainstNull(internalObjectId, internalObjectId);
		//		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		//		
		//		if(!objectsRepositoy.existsById(objectBoundary.getObjectId()))
		//		{
		//			throw new RuntimeException("Could not find user with id : " + objectBoundary.getObjectId());
		//		}
		//		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,objectBoundary.getObjectId());
		//		
		//		SuperAppObjectEntity returned = this.objectsRepositoy.save(EntityUpdate);
		//				
		//		return convertor.toBoundary(returned);
		throw new DeprecatedFunctionException("Unsupported paging updateObject function is deprecated ");
	}



	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {	

		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);

		Optional<SuperAppObjectEntity> optional = this.objectsRepository.findById(objectId);

		if(optional.isEmpty())
		{
			throw new RuntimeException("could not find superAppObject with id : " + objectId.toString());
		}

		return convertor.toBoundary(optional.get());

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(int page, int size) {
		return this.objectsRepository
				.findAll(PageRequest.of(page, size, Direction.DESC,  "internalObjectId"))
				.stream()
				.map(entity -> convertor.toBoundary(entity))
				.collect(Collectors.toList());
	}


	@Override
	//@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		throw new DeprecatedFunctionException("Unsupported paging getAllObjects function is deprecated ");
	}
	@Override
	@Transactional
	public void deleteAllObjects() {
		if(this.objectsRepository.count() == 0) {
			return;
		}
		this.objectsRepository.deleteAll();
	}

	@Override
	@Transactional
	public void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary childId) {
		Guard.AgainstNull(childId, childId.getClass().getName());
		Guard.AgainstNull(parentSuperApp, parentSuperApp);
		Guard.AgainstNull(parentInternalId, parentInternalId);

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
	public List<ObjectBoundary> getAllChildrens(String superApp, String internalId, int page, int size) {

		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);

		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);

		Optional<SuperAppObjectEntity> optionalEntity =  this.objectsRepository.findById(entityId);

		Guard.AgainstNullOptinalIdNotFound(optionalEntity, optionalEntity.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity entity = optionalEntity.get();

		return this.objectsRepository
				.findAllByParent(entity,PageRequest.of(page, size, Direction.DESC, "superapp","internalObjectId"))
				.stream()
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());
	}


	@Override
	@Transactional(readOnly = true)
	public Set<SuperAppObjectIdBoundary> getParent(String superApp, String internalId,int page,int size) {
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);

		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);

		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepository.findById(entityId);

		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, entityId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity parent = optionalParentEntity.get().getParent();

		var parents = new HashSet<SuperAppObjectIdBoundary>();

		parents.add(convertor.toBoundary(parent).getObjectId());	

		return parents;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<ObjectBoundary> searchObjectsByCreationTimeStamp(CreationEnum creation, int page, int size) {
		Instant oneCreationUnitAgo = Instant.now().minus(1, CreationEnum.MapCreationEnumToChronoUnit(creation));

		List<SuperAppObjectEntity> entities = objectsRepository.
				findBycreationTimestampAfter(oneCreationUnitAgo);

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
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary,
			String userSuperApp, String userEmail) {
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNull(userSuperApp, userSuperApp);
		Guard.AgainstNull(userEmail, userEmail);
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());

		if(!objectsRepository.existsById(objectId))
		{
			throw new EntityNotFoundException("Could not find object with id : " + objectId);
		}

		UserId userId = new UserId(userSuperApp,userEmail);
		Optional<UserEntity> userEntity = this.userRepository.findById(userId); 
		if(userEntity == null) {
			throw new EntityNotFoundException("Could not find user with id : " + userId);
		}
		if(userEntity.get().getRole() != UserRole.SUPERAPP_USER)
		{
			throw new UnAuthoriezedRoleRequestException("Only SuperApp User can update objects");
		}
		

		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,objectId);
		var oldUserId = new HashMap<String, UserId>();
		oldUserId.put("userId", userId);
		EntityUpdate.setCreatedBy(oldUserId);

		SuperAppObjectEntity returned = this.objectsRepository.save(EntityUpdate);

		return convertor.toBoundary(returned);
	}


	@Override
	public List<ObjectBoundary> getAllObjectsByType(String type, String userSuperApp, String userEmail, int page,
			int size) {

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
	public List<ObjectBoundary> getAllObjectsByAlias(String alias, String userSuperApp, String userEmail, int page,
			int size) {
		
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
	public List<ObjectBoundary> getAllObjectsByAliasContainingText(String text, String userSuperApp, String userEmail,
			int page, int size) {
		
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










}
