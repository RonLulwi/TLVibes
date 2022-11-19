package demo.boundaries;



import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.enums.ObjectType;




public class ObjectBoundary{
	private final static String OBJECT_ID_KEY = "internalObjectId";

	private static int idGenerator = 99;

	private Map<String, Object> objectId;
	private ObjectType type;
	private Object alias;
	private boolean active;
	private Date creationTimestamp;
	private Map<String, Object> createdBy;
	private Map<String, Object> objectDetails;	


	public ObjectBoundary() {

	}

	public ObjectBoundary(Object objectId) {
		ObjectDummyInit(objectId);
	}

	public ObjectBoundary(ObjectBoundary ob) {
		//this.objectId = ob.objectId;
		this.type = ob.type;
		this.alias = ob.alias;
		this.active = ob.active;
		this.creationTimestamp = ob.creationTimestamp;
		this.createdBy =  ob.createdBy;
		this.objectDetails =  ob.objectDetails;	
	}



	public Map<String, Object> getObjectId() {
		return objectId;
	}



	public void setObjectId(Object objectId) {
		if(this.objectId==null||objectId == null) {
			this.objectId = new HashMap<String,Object>();
			this.objectId.put("superapp", "2023a.demo");
		}

		this.objectId.put(OBJECT_ID_KEY,
				objectId!=null ?
				((Map<String, Object>)objectId).get(OBJECT_ID_KEY) :
					(idGenerator++ + ""));
	}


	public ObjectType getType() {
		return type;
	}



	public void setType(ObjectType type) {
		this.type = type;
	}


	public Object getAlias() {
		return alias;
	}


	public void setAlias(Object alias) {
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




	public Map<String, Object> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Map<String, Object> createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}


	public void setObjectDetails(String key, Object value) {
		this.objectDetails.put(key, value);
	}


	@Override
	public String toString() {
		return "ObjectBoundary [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}

	//This is a dummy initiation
	private void ObjectDummyInit(Object objectId) {
		this.objectId = new HashMap<String,Object>();
		this.objectId.put("superapp", "2023a.demo");
		this.objectId.put(OBJECT_ID_KEY, (objectId)!= null? objectId :(idGenerator++ + ""));
		this.type = ObjectType.dummyType;
		this.alias = "demo instance";
		this.active = true;
		this.creationTimestamp = new Date();
		this.createdBy = Collections.singletonMap("userId", new HashMap<String,Object>());
		@SuppressWarnings("unchecked")
		HashMap<String, Object> userId = (HashMap<String, Object>) this.createdBy.get("userId");
		userId.put("superapp", "2023a.demo");
		userId.put("email", "jane@demo.org");
		this.objectDetails = new HashMap<String,Object>();
		this.objectDetails.put("key1", "can be set to any value you wish");
		this.objectDetails.put("key2", "you can also name the attributes any name you like");
		this.objectDetails.put("key3", 9.99);
		this.objectDetails.put("key4", true);
	}




}
