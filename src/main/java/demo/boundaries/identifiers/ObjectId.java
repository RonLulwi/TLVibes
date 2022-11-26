package demo.boundaries.identifiers;

import demo.infrastructure.IdGenerator;

public class ObjectId {
	String supperApp;
	int internalObjectId;
	
	public ObjectId()
	{
		setSupperApp("2023a.demo");
		setInternalObjectId(IdGenerator.GenerateIntID());
	}

	public ObjectId(String superApp)
	{
		this();
		setSupperApp(superApp);
	}

	public ObjectId(String superApp, int internalObjectId)
	{
		setSupperApp(superApp);
		setInternalObjectId(internalObjectId);
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

	public int getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(int internalObjectId) {
		this.internalObjectId = internalObjectId;
	}
	
	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = Integer.parseInt(internalObjectId);
	}



	
}

