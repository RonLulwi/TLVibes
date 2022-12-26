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
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
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
	
	@Autowired
	public ObjectService(ObjectConvertor convertor,ConfigProperties configProperties,
			IdGenerator idGenerator, SuperAppObjectRepository objectsRepositoy,
			UserEntityRepository userRepository) {
		this.convertor = convertor;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
		this.objectsRepositoy = objectsRepositoy;
	}
	
		
	@Override
	@Transactional
	public ObjectBoundary createObject(ObjectBoundary objWithoutId) {
		
		try {
			validateObjectBoundary(objWithoutId);
		} catch (Exception e) {
			throw new RuntimeException("Invalid ObjectBoundary", e);
		}
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(
				configProperties.getSuperAppName(),
				idGenerator.GenerateUUID().toString());
		
		
		SuperAppObjectEntity entity = convertor.toEntity(objWithoutId,objectId);
		
		var id = new SuperAppObjectIdBoundary(configProperties.getSuperAppName(),idGenerator.GenerateUUID().toString());
		
		entity.setObjectId(id);
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(entity);
		
		return convertor.toBoundary(returned);
	}



	@Override
	@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {
	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		if(!objectsRepositoy.existsById(objectBoundary.getObjectId()))
		{
			throw new RuntimeException("Could not find user with id : " + objectBoundary.getObjectId());
		}
		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,objectBoundary.getObjectId());
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(EntityUpdate);
				
		return convertor.toBoundary(returned);
	}


	
	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {	
		
		SuperAppObjectIdBoundary objectId = new SuperAppObjectIdBoundary(objectSuperApp, internalObjectId);
		
		Optional<SuperAppObjectEntity> optional = this.objectsRepositoy.findById(objectId);
		
		if(optional.isEmpty())
		{
			throw new RuntimeException("could not find superAppObject with id : " + objectId.toString());
		}
		
		return convertor.toBoundary(optional.get());
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(int page, int size) {
		return this.objectsRepositoy
				.findAll(PageRequest.of(page, size, Direction.DESC,  "objectId"))
				.stream()
				.map(entity -> convertor.toBoundary(entity))
				.collect(Collectors.toList());
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		//TODO: Complete the throw exception
		//throw new UnimplementedObjectRelatedOperationException();
		return StreamSupport
				.stream(this.objectsRepositoy.findAll().spliterator(), false)
				.map(entity -> convertor.toBoundary(entity))
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
	public Set<SuperAppObjectIdBoundary> GetAllChildrens(String superApp, String internalId) {
		
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);
		
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);
		
		Optional<SuperAppObjectEntity> optionalEntity =  this.objectsRepositoy.findById(entityId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalEntity, optionalEntity.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity entity = optionalEntity.get();

		return GetEntityChildrens(entity);
	}


	@Override
	@Transactional(readOnly = true)
	public Set<SuperAppObjectIdBoundary> GetParent(String superApp, String internalId) {
		Guard.AgainstNull(superApp, superApp);
		Guard.AgainstNull(internalId, internalId);
		
		var entityId = new SuperAppObjectIdBoundary(superApp,internalId);
		
		Optional<SuperAppObjectEntity> optionalParentEntity =  this.objectsRepositoy.findById(entityId);
		
		Guard.AgainstNullOptinalIdNotFound(optionalParentEntity, entityId.toString(), SuperAppObjectEntity.class.getName());

		SuperAppObjectEntity parent = optionalParentEntity.get().getParent();
			
		var parents = new HashSet<SuperAppObjectIdBoundary>();

		parents.add(convertor.toBoundary(parent).getObjectId());	
		
		return parents;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<ObjectBoundary> SearchObjectsByCreationTimeStamp(CreationEnum creation, int page, int size) {
		Instant oneCreationUnitAgo = Instant.now().minus(1, CreationEnum.MapCreationEnumToChronoUnit(creation));
		
		List<SuperAppObjectEntity> entities = objectsRepositoy.
				findBycreationTimestampAfter(oneCreationUnitAgo);
		
		return entities.stream()
				.map(e -> convertor.toBoundary(e))
				.collect(Collectors.toSet());
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
