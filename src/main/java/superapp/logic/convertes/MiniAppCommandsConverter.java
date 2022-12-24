package superapp.logic.convertes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import superapp.data.entities.CommandFactory;
import superapp.data.entities.MiniAppCommandEntity;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.identifiers.CommandId;

@Component
public class MiniAppCommandsConverter {

	private CommandFactory commandFactory;
	
	@Autowired
	public void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary,CommandId commandId) {		
		
		MiniAppCommandEntity entity =  commandFactory.GetCommand(boundary.getCommand());
		
		entity.setCommandId(commandId);
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setCommand(boundary.getCommand());
		entity.setInvokedBy(boundary.getInvokedBy());
		entity.setTargetObject(boundary.getTargetObject());
		entity.setCommandAttributes(boundary.getCommandAttributes());
		
		return entity;
	}
	 

	
	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity){
		MiniAppCommandBoundary boundary =  new MiniAppCommandBoundary();
		
		boundary.setCommandId(entity.getCommandId());
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		boundary.setCommand(entity.getCommand());
		boundary.setTargetObject(entity.getTargetObject());
		boundary.setInvokedBy(entity.getInvokedBy());
		boundary.setCommandAttributes(entity.getCommandAttributes());
		
		return boundary;
	}



}
