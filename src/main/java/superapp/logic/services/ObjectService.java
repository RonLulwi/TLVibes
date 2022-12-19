package superapp.logic.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.entities.SuperAppObjectEntity;
import superapp.data.entities.UserEntity;
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
	private SuperAppObjectRepository objectsRepositoy;
	private UserEntityRepository userRepository;
	
	@Autowired
	public ObjectService(ObjectConvertor convertor,ConfigProperties configProperties,
			IdGenerator idGenerator, SuperAppObjectRepository objectsRepositoy,
			UserEntityRepository userRepository) {
		this.convertor = convertor;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
		this.objectsRepositoy = objectsRepositoy;
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
		
		SuperAppObjectEntity entity = convertObjectBoundaryToEntity(objWithoutId);
		
		var id = new SuperAppObjectIdBoundary(configProperties.getSuperAppName(),idGenerator.GenerateUUID().toString());
		
		entity.setObjectId(id);
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(entity);
		
		return convertObjectEntityToBoundary(returned);
	}



	@Override
	@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {
	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		SuperAppObjectEntity existingObjectEntity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound
				(objectSuperApp, internalObjectId);
						
		SuperAppObjectEntity EntityUpdate = convertObjectBoundaryToEntity(objectBoundary);

		if(existingObjectEntity.equals(EntityUpdate)){
			return objectBoundary;
		}
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(EntityUpdate);
				
		return convertObjectEntityToBoundary(returned);
	}


	
	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {		
		SuperAppObjectEntity entity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp, internalObjectId);
		
		return convertObjectEntityToBoundary(entity);
		
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		return StreamSupport
				.stream(this.objectsRepositoy.findAll().spliterator(), false)
				.map(entity -> convertObjectEntityToBoundary(entity))
				.collect(Collectors.toList());

	}
	@Override
	@Transactional
	public void deleteAllObjects() {
		if(this.objectsRepositoy.count() == 0) {
			return;
		}
		this.objectsRepositoy.deleteAll();
	}
	
	@Override
	@Transactional
	public void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary childId) {
		Guard.AgainstNull(childId, childId.getClass().getName());
		Guard.AgainstNull(parentSuperApp, parentSuperApp);
		Guard.AgainstNull(parentInternalId, parentInternalId);
		
		var parentId = new SuperAppObjectIdBoundary(parentSuperApp,parentInternalId);
		
		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepositoy.findById(parentId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, parentId.toString(), SuperAppObjectEntity.class.getName());
		
		SuperAppObjectEntity parent = optionalParentEntity.get();
		
		Optional<SuperAppObjectEntity> optionalChildEntity =  this.objectsRepositoy.findById(childId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalChildEntity, childId.toString(), SuperAppObjectEntity.class.getName());
		
		SuperAppObjectEntity child = optionalChildEntity.get();

		parent.BindChild(child);
		
		child.setParent(parent);
		
		objectsRepositoy.save(parent);
		
	}


	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary[] GetAllChildrens(String superApp, String internalId) {
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);
		
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);
		
		Optional<SuperAppObjectEntity> optionalEntity =  this.objectsRepositoy.findById(entityId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalEntity, optionalEntity.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity entity = optionalEntity.get();

		return new ObjectBoundary[] {convertObjectEntityToBoundary(entity)};
	}


	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary[] GetParent(String superApp, String internalId) {
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);
		
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);
		
		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepositoy.findById(entityId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, entityId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity parent = optionalParentEntity.get().getParent();
				
		return new ObjectBoundary[] {convertObjectEntityToBoundary(parent)};
	}

	private ObjectBoundary convertObjectEntityToBoundary(SuperAppObjectEntity entity) {
		
		Set<SuperAppObjectIdBoundary> childrensAsBoundary = GetEntityChildrens(entity);

		UserId createdBy = entity.getCreatedBy().getUserId();
		
		SuperAppObjectIdBoundary parentId = null;
		
		if(entity.getParent() != null)
		{
			parentId = entity.getParent().getObjectId();
		}
		
		return this.convertor.toBoundary(entity,childrensAsBoundary,createdBy,parentId);
		
	}
	private SuperAppObjectEntity convertObjectBoundaryToEntity(ObjectBoundary objWithoutId) {
		Optional<UserEntity> optionalCreatedBy = userRepository.findById(objWithoutId.getCreatedBy());
		
		Guard.AgainstNullOptinalIdNotFound(optionalCreatedBy,objWithoutId.getCreatedBy().toString(),UserEntity.class.getName());
		
		UserEntity createdBy = optionalCreatedBy.get();
		
		Set<SuperAppObjectEntity> chiledrens = getRecursiveChildrens(objWithoutId.getChilderns(), createdBy);

		SuperAppObjectEntity parent = null;
		
		if(objWithoutId.getParent() != null)
		{
			Optional<SuperAppObjectEntity> optionalParent = objectsRepositoy.findById(objWithoutId.getParent());
			
			Guard.AgainstNullOptinalIdNotFound(optionalParent,objWithoutId.getParent().toString(),SuperAppObjectEntity.class.getName());
			
			parent = optionalParent.get();
		}
		
		SuperAppObjectEntity entity = convertor.toEntity(objWithoutId, createdBy, chiledrens, parent);
		
		return entity;
	}


	private SuperAppObjectEntity getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(String objectSuperApp, String internalObjectId) {
		
		var id = new SuperAppObjectIdBoundary(objectSuperApp,internalObjectId);

		Optional<SuperAppObjectEntity> dbSuperAppObject = this.objectsRepositoy.findById(id);
		
		if(dbSuperAppObject.isEmpty()) {
			throw new RuntimeException("Cannot find for app: " + id.getSuperapp() + ", user with id: " + id.getInternalObjectId());			
		}
		
		return dbSuperAppObject.get();
	}
	
	private HashSet<SuperAppObjectEntity> getRecursiveChildrens(Set<SuperAppObjectIdBoundary> childernIds, UserEntity userEntity) {
		
		if(childernIds == null || childernIds.size() == 0)
			return new HashSet<SuperAppObjectEntity>();
		
		var childrensAsEntity = new HashSet<SuperAppObjectEntity>();
		
		for(var childId : childernIds)
		{
			Optional<SuperAppObjectEntity> optionalEntity = objectsRepositoy.findById(childId);
			
			if(optionalEntity.isEmpty())
			{
				throw new RuntimeException("Cannot find SuperAppObject with id " + childId); 
			}
						
			childrensAsEntity.add(optionalEntity.get());
		}
		
		return childrensAsEntity;
	}
	
	private Set<SuperAppObjectIdBoundary> GetEntityChildrens(SuperAppObjectEntity entity) {
		
		if(entity== null || entity.getChildrens() == null || entity.getChildrens().isEmpty())
		{
			return new HashSet<SuperAppObjectIdBoundary>();
		}
		
		Set<SuperAppObjectIdBoundary> childrensAsBoundary = entity.getChildrens().stream()
				.map(child -> child.getObjectId())
				.collect(Collectors.toSet());
		return childrensAsBoundary;
	}

	private void validateObjectBoundary(ObjectBoundary objWithoutId) throws NoSuchFieldException, SecurityException {
		Map<String, Object> request = Map.of(objWithoutId.getClass().getName() ,objWithoutId,
				objWithoutId.getClass().getDeclaredField("createdBy").toString(),objWithoutId.getCreatedBy(),
				objWithoutId.getClass().getDeclaredField("active").toString(),objWithoutId.getActive(),
				objWithoutId.getClass().getDeclaredField("type").toString(),objWithoutId.getType(),
				objWithoutId.getClass().getDeclaredField("alias").toString(),objWithoutId.getAlias()
		);
		
		Guard.AgainstNullRequest(request);
	}

	
}
