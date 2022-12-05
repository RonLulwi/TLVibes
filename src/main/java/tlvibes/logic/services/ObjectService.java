package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.logic.boundaries.ObjectBoundary;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.convertes.ObjectConvertor;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.infrastructure.IdGenerator;
import tlvibes.logic.infrastructure.ImutableField;
import tlvibes.logic.interfaces.ObjectsService;

@Service
public class ObjectService implements ObjectsService {
	
	private ConfigProperties configProperties;
	private ObjectConvertor convertor;
	private IdGenerator idGenerator;
	private List<SuperAppObjectEntity> objects;
	
	@Autowired
	public ObjectService(ObjectConvertor convertor,ConfigProperties configProperties,IdGenerator idGenerator) {
		this.convertor = convertor;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
	}
	
	
	@PostConstruct
	public void init() {
		this.objects = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public ObjectBoundary createObject(ObjectBoundary objWithoutId) {
		
		//TODO : store object to DB
		Guard.AgainstNull(objWithoutId, objWithoutId.getClass().getName());
		Guard.AgainstNull(objWithoutId.getCreatedBy(), objWithoutId.getCreatedBy().getClass().getName());
		Guard.AgainstNull(objWithoutId.getActive(), objWithoutId.getActive().toString());
		Guard.AgainstNull(objWithoutId.getType(), objWithoutId.getType());
		Guard.AgainstNull(objWithoutId.getAlias(), objWithoutId.getAlias());
		Guard.AgainstNull(objWithoutId.getObjectDetails(), objWithoutId.getObjectDetails().getClass().getName());
		
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		var id = new ObjectId(configProperties.getSuperAppName(),idGenerator.GenerateUUID().toString());
		
		entity.setObjectId(id);
		entity.setType(objWithoutId.getType());
		entity.setAlias(objWithoutId.getAlias());
		entity.setActive(objWithoutId.getActive());
		entity.setCreationTimestamp(new Date());
		entity.setCreatedBy(objWithoutId.getCreatedBy());
		entity.setObjectDetails(objWithoutId.getObjectDetails());
		
		this.objects.add(entity);
		
		return this.convertor.toBoundary(entity);
	}
	@Override
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary objectBoundary) {
	
		Guard.AgainstNull(objectSuperApp, objectSuperApp);
		Guard.AgainstNull(internalObjectId, internalObjectId);
		Guard.AgainstNull(objectBoundary, objectBoundary.getClass().getName());
		
		SuperAppObjectEntity entity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp, internalObjectId);
		
		SuperAppObjectEntity EntityUpdate = convertor.toEntity(objectBoundary);
		
		if(entity.equals(EntityUpdate)){
			return objectBoundary;
		}
		
		boolean isDirty = false;
		
		for (var field : entity.getClass().getDeclaredFields()) {
			if(!(field.isAnnotationPresent(ImutableField.class)))
			{
			    field.setAccessible(true);
				try {
					field.set(entity, field.get(EntityUpdate));
					isDirty = true;
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		if(isDirty) {
			//TODO: Store in DB
			entity.setCreationTimestamp(new Date());
			this.objects.add(entity);
		}
		
		return this.convertor.toBoundary(entity);
	}
	@Override
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {
		SuperAppObjectEntity entity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp,internalObjectId);
		return this.convertor.toBoundary(entity);
	}
	
	
	
	private SuperAppObjectEntity getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(
			String objectSuperApp, String internalObjectId) {
		for(SuperAppObjectEntity entity : this.objects) {
			if(entity.getObjectId().getInternalObjectId().equals(internalObjectId)
					&& entity.getObjectId().getSuperApp().equals(objectSuperApp)) {
				return entity;
			}
		}
		throw new RuntimeException("Cannot find for app: " + objectSuperApp + ", user with id: " + internalObjectId);
	}



	@Override
	public List<ObjectBoundary> getAllObjects() {
		return this.objects.stream().map(this.convertor::toBoundary).collect(Collectors.toList());
	}
	@Override
	public void deleteAllObjects() {
		
		this.objects.clear();
	}

	
}
