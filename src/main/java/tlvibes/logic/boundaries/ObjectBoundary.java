package tlvibes.logic.boundaries;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import tlvibes.data.enums.ObjectAlias;
import tlvibes.data.enums.ObjectType;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.boundaries.identifiers.UserId;

public class ObjectBoundary{
	
	private ObjectId objectId;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	private UserId createdBy;
	private Map<String, Object> objectDetails;	

	public ObjectBoundary() {
		this.objectId = new ObjectId();
		this.type = ObjectType.DUMMY;
		this.alias = ObjectAlias.DUMMY;
		this.active = true;
		this.creationTimestamp = new Date();
		this.createdBy = new UserId();
		this.objectDetails = new  HashMap<String, Object>();
	}

	public ObjectBoundary(ObjectId objectId) {
		this();
		this.objectId = objectId;
	}

	public ObjectBoundary(ObjectBoundary other) {
		this.objectId = other.getObjectId();
		this.type = other.getType();
		this.alias = other.getAlias();
		this.active = other.getActive();
		this.creationTimestamp = other.getCreationTimestamp();
		this.createdBy = other.getCreatedBy();
		this.objectDetails = other.getObjectDetails();
	}

	@PostConstruct
	private void ObjectDummyInit() {
		this.objectDetails.put("key1", "can be set to any value you wish");
		this.objectDetails.put("key2", "you can also name the attributes any name you like");
		this.objectDetails.put("key3", 9.99);
		this.objectDetails.put("key4", true);
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public UserId getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserId createdBy) {
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

}
