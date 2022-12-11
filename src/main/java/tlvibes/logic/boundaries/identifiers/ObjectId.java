package tlvibes.logic.boundaries.identifiers;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ObjectId implements Serializable {
    private static final long serialVersionUID = 1L;
	String superApp;
	String internalObjectId;
	
	public ObjectId()
	{
		this.internalObjectId = "Undefind";
		this.superApp = "Undefind";
	}
	
	public ObjectId(String superApp, String internalObjectId)
	{
		this.internalObjectId = internalObjectId;
		this.superApp = superApp;
	}

	public String getSuperApp() {
		return superApp;
	}
	
	public void setSuperApp(String supperApp) {
		this.superApp = supperApp;
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
		return this.superApp.equals(objectId.getSuperApp()) &&
				this.internalObjectId.equals(objectId.getInternalObjectId());
	}

}

