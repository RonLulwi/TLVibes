package tlvibes.logic.boundaries.identifiers;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ObjectId implements Serializable {
    private static final long serialVersionUID = 1L;
	String superapp;
	String internalObjectId;
	
	public ObjectId()
	{
		this.internalObjectId = "Undefind";
		this.superapp = "Undefind";
	}
	
	public ObjectId(String superApp, String internalObjectId)
	{
		this.internalObjectId = internalObjectId;
		this.superapp = superApp;
	}

	public String getSuperapp() {
		return superapp;
	}
	
	public void setSuperapp(String supperApp) {
		this.superapp = supperApp;
	}

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}	
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		ObjectId objectId = (ObjectId) obj;
        return Equals(objectId);
	}

	private boolean Equals(ObjectId objectId) {
		return this.superapp.equals(objectId.getSuperapp()) &&
				this.internalObjectId.equals(objectId.getInternalObjectId());
	}

}

