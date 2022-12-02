package demo.boundaries.identifiers;

import java.util.UUID;


public class CommandId {
	String supperApp;
	String miniApp;
	String internalCommanId;

	public CommandId() {}


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

	public String getInternalCommanId() {
		return internalCommanId;
	}

	public void setInternalCommanId(String internalCommanId) {
		this.internalCommanId = internalCommanId;
	}
}
