package demo.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.interfaces.Iobject;
import demo.interfaces.Iuser;


public class MiniAppCommandBoundary {

	private Map<String, Object> commandId;
	private String command;
	//private Map<String, Object> targetObject;
	private Iobject targetObject;
	private Date invocationTimestamp;
	private Iuser invokedBy; // TODO change to implements
	private Map<String, Object> commandAttributes;
	
	public MiniAppCommandBoundary() {
		this.commandId = new HashMap<>();
		this.commandId.put("superapp", "2023a.demo");
		this.commandId.put("miniapp", "dummyApp");
		this.commandId.put("internalCommandId", "121");
		
		this.command = "doSomething";
		this.targetObject = new Iobject() {
			
			@Override
			public Map<String, Object> getObjectId() {
				Map<String, Object> objectId = new HashMap<>();
				objectId.put("superapp", "2023a.demo");
				objectId.put("internalObjectId", "99");
				return objectId;
			}
		};
		
//		this.targetObject = new HashMap<>();
//		this.targetObject.put("suoerapp", "2023a.demo");
//		this.targetObject.put("internalObjectId", "99");
		
		this.invocationTimestamp = new Date();
		
		this.invokedBy = new Iuser() {
			@Override
			public Map<String, Object> getUserId() {
				Map<String, Object> userId = new HashMap<>();
				userId.put("superapp", "2023a.demo");
				userId.put("email", "jill@demo.org");
				return userId;
			}
		};
		
		this.commandAttributes = new HashMap<>();
		this.commandAttributes.put("key1", "can be anything you wish, even a nested json");		
	}

	public Map<String, Object> getCommandId() { return commandId; }
	public void setCommandId(Map<String, Object> commandId) { this.commandId = commandId; }
	
	public String getCommand() { return command; }
	public void setCommand(String command) { this.command = command; }
	
	public Iobject getTargetObject() { return targetObject; }
	public void setTargetObject(Iobject targetObject) {	this.targetObject = targetObject;}
	
	public Date getInvocationTimestamp() { return invocationTimestamp;}
	public void setInvocationTimestamp(Date invocationTimestamp) {this.invocationTimestamp = invocationTimestamp;}
	
	public Iuser getInvokedBy() {	return invokedBy;}
	public void setInvokedBy(Iuser invokedBy) { this.invokedBy = invokedBy;}
	
	public Map<String, Object> getCommandAttributes() {	return commandAttributes;}
	public void setCommandAttributes(Map<String, Object> commandAttributes) { this.commandAttributes = commandAttributes;}

	@Override
	public String toString() {
		return "MiniAppCommandBoundary [commandId=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
