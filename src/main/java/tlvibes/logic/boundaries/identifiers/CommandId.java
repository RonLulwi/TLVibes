package tlvibes.logic.boundaries.identifiers;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class CommandId implements Serializable{
	private static final long serialVersionUID = 1L;
    String supperapp;
    String miniapp;
    String internalCommanId;


	public CommandId() {
		this.supperapp = "undefined";
		this.miniapp = "undefined";
		this.internalCommanId = "undefind";
	}

	public CommandId(String internalCommanId, String supperApp, String miniApp ) {
		this.internalCommanId = internalCommanId;
		this.supperapp = supperApp;
		this.miniapp = miniApp;
	}

	public String getSupperapp() {
		return supperapp;
	}

	public void setSupperapp(String supperApp) {
		this.supperapp = supperApp;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniApp) {
		this.miniapp = miniApp;
	}

	public String getInternalCommanId() {
		return internalCommanId;
	}

	public void setInternalCommanId(String internalCommanId) {
		this.internalCommanId = internalCommanId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(internalCommanId, miniapp, supperapp);
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
		return Objects.equals(internalCommanId, other.internalCommanId) && Objects.equals(miniapp, other.miniapp)
				&& Objects.equals(supperapp, other.supperapp);
	}
	
	
}
