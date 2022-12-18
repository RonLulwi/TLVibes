package tlvibes.logic.convertes;

import org.springframework.stereotype.Component;

import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;
import tlvibes.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;

@Component
public class MiniAppCommandsConverter {

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary, SuperAppObjectEntity targetObject, UserEntity invoker) {
		
		MiniAppCommandEntity entity =  new MiniAppCommandEntity();
		
		entity.setCommandId(boundary.getCommandId());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setCommand(boundary.getCommand());
		entity.setTargetObject(targetObject);
		entity.setInvokedBy(invoker);
		entity.setCommandAttributes(boundary.getCommandAttributes());
		
		return entity;
	}
	 

	
	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity,SuperAppObjectIdBoundary targetId, UserId invokerId) {
		MiniAppCommandBoundary boundary =  new MiniAppCommandBoundary();
		
		boundary.setCommandId(entity.getCommandId());
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		boundary.setCommand(entity.getCommand());
		boundary.setTargetObject(targetId);
		boundary.setInvokedBy(invokerId);
		boundary.setCommandAttributes(entity.getCommandAttributes());
		
		return boundary;
	}

}
