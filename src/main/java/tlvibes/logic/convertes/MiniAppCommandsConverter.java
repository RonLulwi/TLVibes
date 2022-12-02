package tlvibes.logic.convertes;

import org.springframework.stereotype.Component;

import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;

@Component
public class MiniAppCommandsConverter {

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		
		MiniAppCommandEntity entity =  new MiniAppCommandEntity();
		
		entity.setCommandId(boundary.getCommandId());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setCommand(boundary.getCommand());
		entity.setTargetObject(boundary.getTargetObject());
		entity.setInvokedBy(boundary.getInvokedBy());
		entity.setCommandAttributes(boundary.getCommandAttributes());
		
		return entity;
	}
	 

	
	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {
		MiniAppCommandBoundary boundary =  new MiniAppCommandBoundary();
		
		return boundary;
	}

}
