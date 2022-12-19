package superapp.logic.boundaries;



import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;

public class ObjectBoundary{
	
	private SuperAppObjectIdBoundary objectId;
	private String type;
	private String alias;
	private Boolean active;
	private Date creationTimestamp;
	private UserId createdBy;
	private Map<String, Object> objectDetails;
	private Set<SuperAppObjectIdBoundary> childerns;
	private SuperAppObjectIdBoundary parent;
	
	public ObjectBoundary() {
	}


	public ObjectBoundary(SuperAppObjectIdBoundary objectId, UserId createdBy) {
		this();
		this.objectId = objectId;
		this.createdBy = createdBy;
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

	public SuperAppObjectIdBoundary getObjectId() {
		return objectId;
	}

	public void setObjectId(SuperAppObjectIdBoundary objectId) {
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
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

	
	public Set<SuperAppObjectIdBoundary> getChilderns() {
		return childerns;
	}


	public void setChilderns(Set<SuperAppObjectIdBoundary> childerns) {
		this.childerns = childerns;
	}


	public SuperAppObjectIdBoundary getParent() {
		return parent;
	}


	public void setParent(SuperAppObjectIdBoundary parent) {
		this.parent = parent;
	}


	@Override
	public String toString() {
		return "ObjectBoundary [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}

}
