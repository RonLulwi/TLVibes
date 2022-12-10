package tlvibes.logic.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.data.entities.UserEntity;
import tlvibes.data.interfaces.SuperAppObjectRepository;
import tlvibes.data.interfaces.UserEntityRepository;
import tlvibes.logic.boundaries.ObjectBoundary;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.convertes.ObjectConvertor;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.infrastructure.IdGenerator;
import tlvibes.logic.interfaces.ObjectsService;

@Service
public class ObjectService implements ObjectsService {
	
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
		Guard.AgainstNull(objWithoutId, objWithoutId.getClass().getName());
		Guard.AgainstNull(objWithoutId.getCreatedBy(), objWithoutId.getCreatedBy().getClass().getName());
		Guard.AgainstNull(objWithoutId.getActive(), objWithoutId.getActive().toString());
		Guard.AgainstNull(objWithoutId.getType(), objWithoutId.getType());
		Guard.AgainstNull(objWithoutId.getAlias(), objWithoutId.getAlias());
		Guard.AgainstNull(objWithoutId.getObjectDetails(), objWithoutId.getObjectDetails().getClass().getName());
				
		UserEntity createdBy = userRepository.findById(objWithoutId.getCreatedBy()).get();
		
		Guard.AgainstNull(createdBy, createdBy.getClass().getName());
		
		SuperAppObjectEntity entity = convertor.toEntity(objWithoutId,createdBy);
		
		var id = new ObjectId(configProperties.getSuperAppName(),idGenerator.GenerateUUID().toString());
		
		entity.setObjectId(id);
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(entity);
		
		return this.convertor.toBoundary(returned);
	}
	@Override
	@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {
	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		SuperAppObjectEntity objectEntity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp, internalObjectId);
				
		UserEntity createdBy = retrivedUserFromDatabaseIfNotEquals(objectBoundary, objectEntity);
				
		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary,createdBy);
		
		if(objectEntity.equals(EntityUpdate)){
			return objectBoundary;
		}
		
		SuperAppObjectEntity returned = this.objectsRepositoy.save(objectEntity);

		return this.convertor.toBoundary(returned);
	}


	private UserEntity retrivedUserFromDatabaseIfNotEquals(ObjectBoundary objectBoundary, SuperAppObjectEntity entity) {
		UserEntity createdBy;
		if(!entity.getCreatedBy().getUserId().equals(objectBoundary.getCreatedBy()))
		{
			createdBy = userRepository.findById(objectBoundary.getCreatedBy()).get();
			
			Guard.AgainstNull(createdBy, createdBy.getClass().getName());
			
		}
		else
		{
			createdBy = entity.getCreatedBy();
		}
		return createdBy;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {		
		SuperAppObjectEntity entity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp, internalObjectId);
		return this.convertor.toBoundary(entity);
	}
	
	
	
	@Override
	public List<ObjectBoundary> getAllObjects() {
		return StreamSupport
				.stream(this.objectsRepositoy.findAll().spliterator(), false)
				.map(this.convertor::toBoundary)
				.collect(Collectors.toList());

	}
	@Override
	public void deleteAllObjects() {
		if(this.objectsRepositoy.count() == 0) {
			return;
		}
		this.objectsRepositoy.deleteAll();
	}
	
	private SuperAppObjectEntity getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(String objectSuperApp, String internalObjectId) {
		
		var id = new ObjectId();
		id.setSuperapp(objectSuperApp);
		id.setInternalObjectId(internalObjectId);

		SuperAppObjectEntity dbSuperAppObject = this.objectsRepositoy.findById(id).get();
		
		if(dbSuperAppObject == null) {
			throw new RuntimeException("Cannot find for app: " + id.getSuperapp() + ", user with id: " + id.getInternalObjectId());			
		}
		
		return dbSuperAppObject;
	}



	
}
