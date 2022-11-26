package demo.boundaries.identifiers;

import java.util.UUID;

import demo.infrastructure.IdGenerator;

public class ObjectId {
	String supperApp;
	UUID internalObjectId;
	
	public ObjectId()
	{
		supperApp = "2023a.demo";
		internalObjectId = IdGenerator.GenerateID();
	}

	public ObjectId(String superApp)
	{
		this();
		this.supperApp = superApp;
	}

	public String getSupperApp() {
		return supperApp;
	}

	public void setSupperApp(String supperApp) {
		this.supperApp = supperApp;
	}

	public UUID getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(UUID internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	
}

