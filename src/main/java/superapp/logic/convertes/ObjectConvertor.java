package superapp.logic.convertes;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import superapp.data.entities.SuperAppObjectEntity;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;

@Component
public class ObjectConvertor {
	
	public SuperAppObjectEntity toEntity(ObjectBoundary boundary,
			SuperAppObjectIdBoundary objectId) {
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		
		entity.setObjectId(objectId);
		entity.setActive(boundary.getActive() != null ? boundary.getActive() : false);
		entity.setAlias(boundary.getAlias());
		entity.setCreatedBy(boundary.getCreatedBy());
		entity.setObjectDetails(boundary.getObjectDetails() != null ? boundary.getObjectDetails() : new HashMap<String,Object>());
		entity.setType(boundary.getType());
		
		return entity;
	}
	
	
	public ObjectBoundary toBoundary(SuperAppObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		boundary.setActive(entity.getActive());
		boundary.setAlias(entity.getAlias());
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setObjectDetails(entity.getObjectDetails());
		boundary.setObjectId(entity.getObjectId());
		boundary.setType(entity.getType());		
		
		return boundary;	
	}
}
