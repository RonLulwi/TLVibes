package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.logic.boundaries.SuperAppObjectBoundary;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.convertes.ObjectConvertor;
import tlvibes.logic.interfaces.ObjectsService;

@Service
public class ObjectService implements ObjectsService {
	
	
	private ObjectConvertor convertor;
	private List<SuperAppObjectEntity> objects;
	
	@Autowired
	public ObjectService(ObjectConvertor convertor) {
		this.convertor = convertor;
	}
	
	
	@PostConstruct
	public void init() {
		this.objects = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary objWithotId) {
		
		//TODO : store object to DB
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		entity.setObjectId(new ObjectId());
		entity.setType(objWithotId.getType());
		entity.setAlias(objWithotId.getAlias());
		entity.setActive(objWithotId.getActive()!=null? objWithotId.getActive() : true);
		entity.setCreationTimestamp(new Date());
		entity.setCreatedBy(objWithotId.getCreatedBy());
		entity.setObjectDetails(objWithotId.getObjectDetails()!=null ? objWithotId.getObjectDetails() : new HashMap<String,Object>());
		this.objects.add(entity);
		
		return this.convertor.toBoundary(entity);
	}
	@Override
	public SuperAppObjectBoundary updateObject(String objectSuperApp, String internalObjectId, SuperAppObjectBoundary objectBoundary) {
		SuperAppObjectEntity entity = this.getEntityByObjectSuperAppAndInternalObjectIdOrThrowExceptionIfNotFound(objectSuperApp, internalObjectId);
		
		boolean isDirty = false;
		if(objectBoundary.getActive() != null && objectBoundary.getActive()!= entity.getActive()) {
			entity.setActive(objectBoundary.getActive());
			isDirty = true;
		}
		if(objectBoundary.getAlias()!=null && !objectBoundary.getAlias().equals(entity.getAlias())) {
			entity.setAlias(objectBoundary.getAlias());
			isDirty = true;
		}
		if(objectBoundary.getObjectDetails()!= null && !objectBoundary.getObjectDetails().equals(entity.getObjectDetails())) {
			entity.setObjectDetails(objectBoundary.getObjectDetails());
			isDirty = true;
		}
		if(objectBoundary.getType()!=null && !objectBoundary.getType().equals(entity.getType())) {
			entity.setType(objectBoundary.getType());
			isDirty = true;
		}
		if(objectBoundary.getCreatedBy()!=null && !objectBoundary.getCreatedBy().equals(entity.getCreatedBy())) {
			entity.setCreatedBy(objectBoundary.getCreatedBy());
			isDirty = true;
		}
		
		if(isDirty) {
			//TODO: Store in DB
			entity.setCreationTimestamp(new Date());
			this.objects.add(entity);
		}
		
		return this.convertor.toBoundary(entity);
	}
	@Override
	public SuperAppObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {
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
	public List<SuperAppObjectBoundary> getAllObjects() {
		return this.objects.stream().map(this.convertor::toBoundary).collect(Collectors.toList());
	}
	@Override
	public void deleteAllObjects() {
		
		this.objects.clear();
	}

	
}
