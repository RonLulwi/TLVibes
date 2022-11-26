package demo.boundaries.identifiers;

import java.util.UUID;

import demo.infrastructure.IdGenerator;

public class CommandId {
	String supperApp;
	String miniApp;
	UUID internalCommanId;

	public CommandId() {
		supperApp = "2023a.demo";
		miniApp = "dummyApp";
		internalCommanId = IdGenerator.GenerateUUID();
		
	}

	public CommandId(String miniAppName) {
		this();
		miniApp = miniAppName;
	}

	public String getSupperApp() {
		return supperApp;
	}

	public void setSupperApp(String supperApp) {
		this.supperApp = supperApp;
	}

	public String getMiniApp() {
		return miniApp;
	}

	public void setMiniApp(String miniApp) {
		this.miniApp = miniApp;
	}

	public UUID getInternalCommanId() {
		return internalCommanId;
	}

	public void setInternalCommanId(UUID internalCommanId) {
		this.internalCommanId = internalCommanId;
	}
}
