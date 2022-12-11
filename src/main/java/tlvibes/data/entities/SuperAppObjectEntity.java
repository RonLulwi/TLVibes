package tlvibes.data.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.infrastructure.SuperAppMapToJsonConverter;

@Entity
public class SuperAppObjectEntity {
	
	@EmbeddedId private ObjectId objectId;
	private String type;
	private String alias;
	private boolean active;
	private Date creationTimestamp;
	@JoinColumns({
		  @JoinColumn(name = "INVOKER_USER_SUPERAPP", referencedColumnName = "superApp"),
		  @JoinColumn(name = "INVOKER_USER_EMAIL", referencedColumnName = "email")
		 })
	@OneToOne
	private UserEntity createdBy;
	@Lob
	@Convert(converter = SuperAppMapToJsonConverter.class)
	private Map<String, Object> objectDetails;
	
	
	public SuperAppObjectEntity() {
		this.type = "undefined";
		this.alias = "undefined";
		this.active = false;
		this.objectDetails = new HashMap<String, Object>();
	}
	
	public SuperAppObjectEntity(ObjectId objectId, UserEntity createdBy) {
		this();
		this.objectId = objectId;
		this.createdBy = createdBy;
		this.creationTimestamp = new Date();
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		if(this.creationTimestamp == null) {
			this.creationTimestamp = creationTimestamp;
		}
		
	}

//	@JoinColumns({
//		  @JoinColumn(name = "CREATED_BY_USER_SUPERAPP", referencedColumnName = "superApp"),
//		  @JoinColumn(name = "CREATED_BY_USER_EMAIL", referencedColumnName = "email")
//		 })
//	@OneToOne
	public UserEntity getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(UserEntity createdBy) {
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
