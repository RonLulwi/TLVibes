package superapp.logic.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import superapp.logic.boundaries.identifiers.CommandId;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;

public class MiniAppCommandBoundary {

	private CommandId commandId;
	private String command;
	private SuperAppObjectIdBoundary targetObject;
	private Date invocationTimestamp;
	private UserId invokedBy;
	private Map<String, Object> commandAttributes;

	public MiniAppCommandBoundary() {
		this.command = "doSomething";
		this.invocationTimestamp = new Date();
		this.commandAttributes = new HashMap<>();
	}
	
	public MiniAppCommandBoundary(CommandId commandId,SuperAppObjectIdBoundary targetObject,UserId invokedBy) {
		this();
		this.commandId = commandId;	
		this.targetObject = targetObject;
		this.invokedBy = invokedBy;
	}


	public MiniAppCommandBoundary(String miniAppName) {
		this();
		this.commandId = new CommandId();
	}
	
	public CommandId getCommandId() {
		return commandId;
	}

	public void setCommandId(CommandId commandId) {
		this.commandId = commandId;
	}

	public UserId getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserId invokedBy) {
		this.invokedBy = invokedBy;
	}

	public SuperAppObjectIdBoundary getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(SuperAppObjectIdBoundary targetObject) {
		this.targetObject = targetObject;
	}
	
	
	public String getCommand() { return command; }
	
	public void setCommand(String command) { this.command = command; }
	
	public Date getInvocationTimestamp() { return invocationTimestamp;}
	
	public void setInvocationTimestamp(Date invocationTimestamp) {this.invocationTimestamp = invocationTimestamp;}
	
	public Map<String, Object> getCommandAttributes() {	return commandAttributes;}
	
	public void setCommandAttributes(Map<String, Object> commandAttributes) { this.commandAttributes = commandAttributes;}

	@Override
	public String toString() {
		return "MiniAppCommandBoundary [commandId=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
