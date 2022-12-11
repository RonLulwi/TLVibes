package tlvibes.logic.convertes;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.ObjectBoundary;

@Component
public class ObjectConvertor {

	public SuperAppObjectEntity toEntity(ObjectBoundary boundary, UserEntity userEntity) {
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		
		entity.setActive(boundary.getActive() != null ? boundary.getActive() : false);
		entity.setAlias(boundary.getAlias());
		entity.setCreatedBy(userEntity);
		entity.setCreationTimestamp(boundary.getCreationTimestamp() != null ? boundary.getCreationTimestamp() : new Date()); //TODO: create new time?
		entity.setObjectDetails(boundary.getObjectDetails() != null ? boundary.getObjectDetails() : new HashMap<String,Object>());
		entity.setObjectId(boundary.getObjectId());
		entity.setType(boundary.getType());
		
		return entity;
	}
	
	public ObjectBoundary toBoundary(SuperAppObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		boundary.setActive(entity.getActive());
		boundary.setAlias(entity.getAlias());
		boundary.setCreatedBy(entity.getCreatedBy().getUserId());
		boundary.setCreationTimestamp(entity.getCreationTimestamp()); //TODO : should changed?
		boundary.setObjectDetails(entity.getObjectDetails());
		boundary.setObjectId(entity.getObjectId());
		boundary.setType(entity.getType());
		
		return boundary;	
	}
}
