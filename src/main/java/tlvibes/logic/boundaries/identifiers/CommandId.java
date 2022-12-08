package tlvibes.logic.boundaries.identifiers;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class CommandId implements Serializable{
	private static final long serialVersionUID = 1L;
	String supperApp;
	String miniApp;
	String internalCommanId;


	public CommandId() {
		this.supperApp = "undefined";
		this.miniApp = "undefined";
		this.internalCommanId = "undefind";
	}

	public CommandId(String internalCommanId, String supperApp, String miniApp ) {
		this.internalCommanId = internalCommanId;
		this.supperApp = supperApp;
		this.miniApp = miniApp;
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

	public String getInternalCommanId() {
		return internalCommanId;
	}

	public void setInternalCommanId(String internalCommanId) {
		this.internalCommanId = internalCommanId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(internalCommanId, miniApp, supperApp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandId other = (CommandId) obj;
		return Objects.equals(internalCommanId, other.internalCommanId) && Objects.equals(miniApp, other.miniApp)
				&& Objects.equals(supperApp, other.supperApp);
	}
	
	
}
