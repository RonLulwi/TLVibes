package superapp.logic.boundaries.identifiers;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class CommandId implements Serializable{
	private static final long serialVersionUID = 1L;
    String superapp;
    String miniapp;
    String internalCommandId;


	public CommandId() {
	}

	public CommandId(String supperApp, String miniApp,String internalCommanId ) {
		this.superapp = supperApp;
		this.miniapp = miniApp;
		this.internalCommandId = internalCommanId;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String supperApp) {
		this.superapp = supperApp;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniApp) {
		this.miniapp = miniApp;
	}

	public String getInternalCommandId() {
		return internalCommandId;
	}

	public void setInternalCommandId(String internalCommanId) {
		this.internalCommandId = internalCommanId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(internalCommandId, miniapp, superapp);
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
		return Objects.equals(internalCommandId, other.internalCommandId) && Objects.equals(miniapp, other.miniapp)
				&& Objects.equals(superapp, other.superapp);
	}

	@Override
	public String toString() {
		return "CommandId [supperapp=" + superapp + ", miniapp=" + miniapp + ", internalCommanId=" + internalCommandId
				+ "]";
	}
	
	
	
	
}
