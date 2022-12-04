package tlvibes.logic.boundaries.identifiers;

import org.springframework.beans.factory.annotation.Value;

import tlvibes.logic.infrastructure.IdGenerator;

public class ObjectId {
	@Value("${spring.application.name}")
	String superApp;
	String internalObjectId;
	
	public ObjectId()
	{
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

