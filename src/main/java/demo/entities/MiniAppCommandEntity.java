package demo.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.boundaries.identifiers.CommandId;
import demo.boundaries.identifiers.ObjectId;
import demo.boundaries.identifiers.UserId;

public class MiniAppCommandEntity {
	private CommandId commandId;
	private String command;
	private ObjectId targetObject;
	private Date invocationTimestamp;
	private UserId invokedBy;
	private Map<String, Object> commandAttributes;

	
	
	public MiniAppCommandEntity() {	
		this.commandId = new CommandId();
		this.targetObject = new ObjectId();
		this.invokedBy = new UserId();		
		this.command = "doSomething";
		this.invocationTimestamp = new Date();
		this.commandAttributes = new HashMap<>();
		this.commandAttributes.put("key1", "can be anything you wish, even a nested json");		
	}

	public String getCommand() {
		return command;
	}



	public CommandId getCommandId() {
		return commandId;
	}



	public void setCommandId(CommandId commandId) {
		this.commandId = commandId;
	}



	public void setCommand(String command) {
		this.command = command;
	}



	public ObjectId getTargetObject() {
		return targetObject;
	}



	public void setTargetObject(ObjectId targetObject) {
		this.targetObject = targetObject;
	}



	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}



	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}



	public UserId getInvokedBy() {
		return invokedBy;
	}



	public void setInvokedBy(UserId invokedBy) {
		this.invokedBy = invokedBy;
	}



	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}



	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}



	@Override
	public String toString() {
		return "MiniAppCommandEntity [commandId=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}



	
	
	

}
