package superapp.logic.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;
import superapp.data.entities.SuperAppObjectEntity;
import superapp.data.entities.UserEntity;
import superapp.data.enums.CreationEnum;
import superapp.data.enums.Role;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.Guard;
import superapp.logic.infrastructure.IdGenerator;
import superapp.logic.interfaces.EnhancedObjectsService;

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
		
		try {
			validateObjectBoundary(objWithoutId);
		} catch (Exception e) {
			throw new RuntimeException("Invalid ObjectBoundary", e);
		}
		
		UserId userId = objWithoutId.getCreatedBy().get("userId");
		if (this.userRepository.findById(userId)
			.orElseThrow(() -> 
			new EntityNotFoundException("Could not find user with id : " + userId))
			.getRole() != Role.SUPERAPP_USER)
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
	@Transactional
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
		throw new UnimplementedObjectRelatedOperationException("Function is no longer in use");
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
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		//TODO: Complete the throw exception
		throw new UnimplementedObjectRelatedOperationException(
				"This url for this function is no longer in use,"
				+ " try to use pagination method instead");
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

	
	private HashSet<SuperAppObjectEntity> getRecursiveChildrens(Set<SuperAppObjectIdBoundary> childrenIds, UserEntity userEntity) {
		
		if(childrenIds == null || childrenIds.size() == 0)
			return new HashSet<SuperAppObjectEntity>();
		
		var childrensAsEntity = new HashSet<SuperAppObjectEntity>();
		
		for(var childId : childrenIds)
		{
			Optional<SuperAppObjectEntity> optionalEntity = objectsRepository.findById(childId);
			
			if(optionalEntity.isEmpty())
			{
				throw new RuntimeException("Cannot find SuperAppObject with id " + childId); 
			}
						
			childrensAsEntity.add(optionalEntity.get());
		}
		
		return childrensAsEntity;
	}
	
//	private Set<ObjectBoundary> getEntityChildrens(SuperAppObjectEntity entity,int page, int size) {
//		
//		if(entity== null || entity.getChildrens() == null || entity.getChildrens().isEmpty())
//		{
//			return new HashSet<ObjectBoundary>();
//		}
//		
//		
//		Set<SuperAppObjectIdBoundary> childrensAsBoundary = entity.getChildrens().stream()
//				.map(child -> child.getObjectId())
//				.collect(Collectors.toSet());
//		return childrensAsBoundary;
//	}

	private void validateObjectBoundary(ObjectBoundary objWithoutId) throws NoSuchFieldException, SecurityException {
		Map<String, Object> request = Map.of(objWithoutId.getClass().getName() ,objWithoutId,
				objWithoutId.getClass().getDeclaredField("createdBy").toString(),objWithoutId.getCreatedBy(),
				objWithoutId.getClass().getDeclaredField("active").toString(),objWithoutId.getActive(),
				objWithoutId.getClass().getDeclaredField("type").toString(),objWithoutId.getType(),
				objWithoutId.getClass().getDeclaredField("alias").toString(),objWithoutId.getAlias()
		);
		
		Guard.AgainstNullRequest(request);
	}


	@Override
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary,
			String userSuperApp, String userEmail) {
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		if(!objectsRepository.existsById(objectId))
		{
			throw new EntityNotFoundException("Could not find object with id : " + objectId);
		}
		
		UserId userId = objectBoundary.getCreatedBy().get("userId");
		if (this.userRepository.findById(userId)
			.orElseThrow(() -> 
			new EntityNotFoundException("Could not find user with id : " + userId))
			.getRole() != Role.SUPERAPP_USER)
		{
			throw new UnAuthoriezedRoleRequestException("Only SuperApp User can update objects");
		}
		
		
		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,objectId);
		
		//TODO: how to set createdBy without changes
		
		
		
		SuperAppObjectEntity returned = this.objectsRepository.save(EntityUpdate);
				
		return convertor.toBoundary(returned);
	}


	






	
}
