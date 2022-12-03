package tlvibes.data.entities;

import java.util.Date;
import java.util.Map;

import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.boundaries.identifiers.UserId;


public class SuperAppObjectEntity {
	
	
	private ObjectId objectId;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	private UserId createdBy;
	private Map<String, Object> objectDetails;
	
	
	public SuperAppObjectEntity() {
	
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
		return "SuperAppObjectEntity [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active="
				+ active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}
	
	

}
