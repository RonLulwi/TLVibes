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
	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary objectBoundary) {
		
		//TODO : store object to DB
		

		
		objectBoundary.setCreationTimestamp(new Date());
		SuperAppObjectEntity entity = this.convertor.toEntity(objectBoundary);
		entity.setObjectId(new ObjectId());
		this.objects.add(entity);
		System.err.println("entity:" + entity);
		SuperAppObjectBoundary boundary = this.convertor.toBoundary(entity);
		System.err.println("boundary:" + boundary);
		
		return boundary;
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
		if(objectBoundary.getObjectDetails().equals(entity.getObjectDetails())) {
			entity.setObjectDetails(objectBoundary.getObjectDetails());
			isDirty = true;
		}
		if(objectBoundary.getType()!=null && !objectBoundary.getType().equals(entity.getType())) {
			entity.setType(objectBoundary.getType());
			isDirty = true;
		}
		
		if(isDirty) {
			//TODO: update DB
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
					&& entity.getObjectId().getSupperApp().equals(objectSuperApp)) {
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
