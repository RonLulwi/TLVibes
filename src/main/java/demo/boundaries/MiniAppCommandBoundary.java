package demo.boundaries;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class MiniAppCommandBoundary {
	private Map<String, Object> commandId;
	private String command;
	private Map<String, Object> targetObject;
	private Date invocationTimeStamp;
	private Map<String, Object> invokedBy;
	private Map<String, Object> commandAttributes;
	
	
	public MiniAppCommandBoundary(String command_name) {
		ObjectDummyInit(command_name);
	}
	
	
	
	public Map<String, Object> getCommandId() {
		return commandId;
	}



	public void setCommandId(Map<String, Object> commandId) {
		this.commandId = commandId;
	}



	public String getCommand() {
		return command;
	}



	public void setCommand(String command) {
		this.command = command;
	}



	public Map<String, Object> getTargetObject() {
		return targetObject;
	}



	public void setTargetObject(Map<String, Object> targetObject) {
		this.targetObject = targetObject;
	}






	public Date getInvocationTimeStamp() {
		return invocationTimeStamp;
	}



	public void setInvocationTimeStamp(Date invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}



	public Map<String, Object> getInvokedBy() {
		return invokedBy;
	}



	public void setInvokedBy(Map<String, Object> invokedBy) {
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
		return "MiniAppCommandBoundary [commandId=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimeStamp=" + invocationTimeStamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
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
		this.invocationTimeStamp=new Date();
		
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
}
