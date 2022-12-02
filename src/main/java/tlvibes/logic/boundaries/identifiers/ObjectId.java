package tlvibes.logic.boundaries.identifiers;

import tlvibes.logic.infrastructure.IdGenerator;

public class ObjectId {
	String supperApp;
	String internalObjectId; 
	
	public ObjectId()
	{
		setSupperApp("2023a.demo");
		setInternalObjectId(Integer.toString(IdGenerator.GenerateIntID()));
	}

	public ObjectId(String superApp)
	{
		this();
		setSupperApp(superApp);
	}
	
	public ObjectId(String superApp, String internalObjectId)
	{
		setSupperApp(superApp);
		setInternalObjectId(internalObjectId);
	}

	public String getSupperApp() {
		return supperApp;
	}

	public void setSupperApp(String supperApp) {
		this.supperApp = supperApp;
	}

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	@Override
	public String toString() {
		return "ObjectId [supperApp=" + supperApp + ", internalObjectId=" + internalObjectId + "]";
	}
	
	
}

