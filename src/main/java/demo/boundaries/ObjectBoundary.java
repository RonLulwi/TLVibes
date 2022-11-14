package demo.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.enums.ObjectType;
import demo.interfaces.Iuser;


public class ObjectBoundary{
	
	private Map<String, Object> objectId; //TODO : change to Iobject
	private ObjectType type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	private Iuser createdBy; //TODO: change to implements
	private Map<String, Object> objectDetails;	
	
	
	public ObjectBoundary() {
		ObjectDummyInit();
	}


	public Map<String, Object> getObjectId() {
		return objectId;
	}


	public void setObjectId(Map<String, Object> objectId) {
		this.objectId = objectId;
	}


	public ObjectType getType() {
		return type;
	}


	public void setType(ObjectType type) {
		this.type = type;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public boolean getActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Date getCreationTimestamp() {
		return creationTimestamp;
	}


	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}


	public Iuser getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Iuser createdBy) {
		this.createdBy = createdBy;
	}


	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}


	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}


	@Override
	public String toString() {
		return "ObjectBoundary [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}
	
	
	//This is a dummy initiation
	private void ObjectDummyInit() {
		this.objectId = new HashMap<>();
		objectId.put("superapp", "2023a.demo");
		objectId.put("internalObjectId", "99");
		this.type = ObjectType.dummyType;
		this.alias = "demo instance";
		this.active = true;
		this.creationTimestamp = new Date();
		this.createdBy = new Iuser() {
			
			@Override
			public Map<String, Object> getUserId() {
				Map<String, Object> o = new HashMap<>();
				o.put("superapp", "2023a.demo");
				o.put("email", "jane@demo.org");
				return o;
			}
		};
		this.objectDetails = new HashMap<>();
		this.objectDetails.put("key1", "can be set to any value you wish");
		this.objectDetails.put("key2", "you can also name the attributes any name you like");
		this.objectDetails.put("key3", 9.99);
		this.objectDetails.put("key4", true);
	}

		
}
