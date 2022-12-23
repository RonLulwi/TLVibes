package superapp.data.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import superapp.logic.boundaries.identifiers.CommandId;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.SuperAppMapToJsonConverter;

@Entity
public class MiniAppCommandEntity {
	@EmbeddedId private CommandId commandId;
	
	@Convert(converter = SuperAppMapToJsonConverter.class)
	private Map<String,SuperAppObjectIdBoundary> targetObject;
	
	@Convert(converter = SuperAppMapToJsonConverter.class)
	private Map<String,UserId> invokedBy;	
	
	@Lob
	@Convert(converter = SuperAppMapToJsonConverter.class)
	
	private Map<String, Object> commandAttributes;
	private Date invocationTimestamp; 
	private String command;

	public MiniAppCommandEntity() {	
		this.invocationTimestamp = new Date();
		this.commandAttributes = new HashMap<>();
		this.invokedBy = new HashMap<>();
		this.targetObject = new HashMap<>();
	}
	
	public void execute() {};
	
	public String getCommand() {
		return command;
	}

	public CommandId getCommandId() {
		return commandId;
	}

	public void setCommandId(CommandId commandId) {
		this.commandId = commandId;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Map<String,SuperAppObjectIdBoundary> getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Map<String,SuperAppObjectIdBoundary> targetObject) {
		this.targetObject = targetObject;
	}

	@Temporal(TemporalType.TIMESTAMP) 
	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}


	public Map<String,UserId> getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(Map<String,UserId> invokedBy) {
		this.invokedBy = invokedBy;
	}
	
	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	@Override
	public String toString() {
		return "MiniAppCommandEntity [commandId=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(commandId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MiniAppCommandEntity other = (MiniAppCommandEntity) obj;
		return Objects.equals(commandId, other.commandId);
	}
	
	

}
