package superapp.logic.convertes;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import superapp.data.SuperAppObjectEntity;
import superapp.data.identifiers.ObjectId;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;


@Component
public class ObjectConvertor {
	
	public SuperAppObjectEntity toEntity(ObjectBoundary boundary,
			ObjectId objectId) {
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		
		entity.setObjectId(objectId);
		entity.setActive(boundary.getActive() != null ? boundary.getActive() : false);
		entity.setAlias(boundary.getAlias());
		entity.setCreatedBy(boundary.getCreatedBy());
		entity.setCreationTimestamp(boundary.getCreationTimestamp());
		entity.setObjectDetails(boundary.getObjectDetails() != null ? boundary.getObjectDetails() : new HashMap<String,Object>());
		entity.setType(boundary.getType());
		
		return entity;
	}
	
	public ObjectBoundary toBoundary(SuperAppObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();

		boundary.setObjectId(toBoundaryId(entity.getObjectId()));
		boundary.setActive(entity.getActive());
		boundary.setAlias(entity.getAlias());
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setObjectDetails(entity.getObjectDetails());
		boundary.setType(entity.getType());		
		
		return boundary;	
	}
	
	public ObjectId toEntityId(SuperAppObjectIdBoundary boundaryId) {
		
		ObjectId objectId = new ObjectId();
		objectId.setInternalObjectId(boundaryId.getInternalObjectId());
		objectId.setSuperapp(boundaryId.getSuperapp());
		
		return objectId;
	}
	
	public SuperAppObjectIdBoundary toBoundaryId(ObjectId objectId) {
		SuperAppObjectIdBoundary boundaryId = new SuperAppObjectIdBoundary();
		boundaryId.setInternalObjectId(objectId.getInternalObjectId());
		boundaryId.setSuperapp(objectId.getSuperapp());
		return boundaryId;
	}
}
