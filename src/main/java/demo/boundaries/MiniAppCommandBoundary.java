package demo.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;




public class MiniAppCommandBoundary {

	private Map<String, Object> commandId;
	private String command;
	//private Map<String, Object> targetObject;
	private Map<String,Object> targetObject;
	private Date invocationTimestamp;
	private Map<String,Object> invokedBy; // TODO change to implements
	private Map<String, Object> commandAttributes;
	
	public MiniAppCommandBoundary(String command_name) {
		ObjectDummyInit(command_name);
	}
	
	private void ObjectDummyInit(String command_name) {
		//config dummy
		String superapp_name="2023.a.demo";
		String type = "dummyApp";
		String commandId = "121";
		String objectId= "99";
		String email= "jill@demo.org";
		String my_key_1= "my key";
		//*************************************************
		
		this.commandId = new HashMap<>();
		this.commandId.put("superapp", superapp_name);
		this.commandId.put("miniapp", type);
		this.commandId.put("internalCommandId", commandId);
		
		//**************************************************
		this.command = command_name;
		Map<String, Object> object_id=  new HashMap<>();
		object_id.put("superapp", superapp_name);
		object_id.put("internalObjectId", objectId);
		this.targetObject = new HashMap<>();
		this.targetObject.put("objectId", object_id);
		
		//**************************************************
		this.invocationTimestamp=new Date();
		
		//**************************************************
		this.invokedBy = new HashMap<>();
		Map<String, Object> userId=  new HashMap<>();
		userId.put("superapp", superapp_name);
		userId.put("email", email);
		this.invokedBy.put("userId", userId);
		
		//**************************************************
		this.commandAttributes = new HashMap<>();
		Map<String, Object> keys=  new HashMap<>();
		keys.put("key2subkey1", my_key_1);
		this.commandAttributes.put("key1", keys);
				
	}
	
	public MiniAppCommandBoundary() {
		this.commandId = new HashMap<>();
		this.commandId.put("superapp", "2023a.demo");
		this.commandId.put("miniapp", "dummyApp");
		this.commandId.put("internalCommandId", "121");
		
		this.command = "doSomething";
		this.targetObject = new HashMap<String,Object>();
		HashMap<String,Object> hm=new HashMap<String,Object>();
		this.targetObject.put("objectId", hm);
		
		hm.put("superapp", "2023a.demo");	
		hm.put("internalObjectId", "99");	
			
		
//		this.targetObject = new HashMap<>();
//		this.targetObject.put("suoerapp", "2023a.demo");
//		this.targetObject.put("internalObjectId", "99");
		
		this.invocationTimestamp = new Date();
		HashMap<String,Object> hm2 =new HashMap<String,Object>();

		this.invokedBy = new HashMap<String,Object>();
		this.invokedBy.put("userId", hm2);
	    hm2.put("superapp", "2023a.demo");
		hm2.put("email", "jill@demo.org");
				
		
		this.commandAttributes = new HashMap<>();
		this.commandAttributes.put("key1", "can be anything you wish, even a nested json");		
	}

	public Map<String, Object> getCommandId() { return commandId; }
	public void setCommandId(Map<String, Object> commandId) { this.commandId = commandId; }
	
	public String getCommand() { return command; }
	public void setCommand(String command) { this.command = command; }
	
	
	
	public Map<String, Object> getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Map<String, Object> targetObject) {
		this.targetObject = targetObject;
	}

	public Map<String, Object> getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(Map<String, Object> invokedBy) {
		this.invokedBy = invokedBy;
	}

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
