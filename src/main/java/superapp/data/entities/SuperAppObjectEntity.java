package superapp.data.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.infrastructure.SuperAppMapToJsonConverter;

@Entity
public class SuperAppObjectEntity {
	
	@EmbeddedId private SuperAppObjectIdBoundary objectId;
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
	
    @OneToMany(mappedBy="parent",fetch = FetchType.LAZY)
	private Set<SuperAppObjectEntity> childrens;
	
    @ManyToOne
	@JoinColumns({
		  @JoinColumn(name = "PARENT_USER_SUPERAPP", referencedColumnName = "superApp"),
		  @JoinColumn(name = "PARENT_INTERNAL_ID", referencedColumnName = "internalObjectId")
		 })
    private SuperAppObjectEntity parent;
	
	public SuperAppObjectEntity() {
		this.type = "undefined";
		this.alias = "undefined";
		this.active = false;
		this.objectDetails = new HashMap<String, Object>();
	}
	
	public SuperAppObjectEntity(SuperAppObjectIdBoundary objectId, UserEntity createdBy) {
		this();
		this.objectId = objectId;
		this.createdBy = createdBy;
		this.creationTimestamp = new Date();
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

	public Set<SuperAppObjectEntity> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<SuperAppObjectEntity> childrens) {
		this.childrens = childrens;
	}
	

	public void BindChild(SuperAppObjectEntity child) {
		this.childrens.add(child);
	}



	public SuperAppObjectEntity getParent() {
		return parent;
	}

	public void setParent(SuperAppObjectEntity parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "SuperAppObjectEntity [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active="
				+ active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuperAppObjectEntity other = (SuperAppObjectEntity) obj;
		return Objects.equals(objectId, other.objectId);
	}
	
	

	
	

}
