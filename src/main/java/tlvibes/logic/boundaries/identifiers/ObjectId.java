package tlvibes.logic.boundaries.identifiers;

import tlvibes.logic.infrastructure.IdGenerator;

public class ObjectId {
	String superApp;
	String internalObjectId;
	
	public ObjectId()
	{
		setSuperApp("2023a.demo");
		setInternalObjectId(Integer.toString(IdGenerator.GenerateIntID()));
	}

	public ObjectId(String superApp)
	{
		this();
		setSuperApp(superApp);
	}
	
	public ObjectId(String superApp, String internalObjectId)
	{
		setSuperApp(superApp);
		setInternalObjectId(internalObjectId);
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
}

