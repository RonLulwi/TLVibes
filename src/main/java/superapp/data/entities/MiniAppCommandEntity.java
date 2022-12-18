package tlvibes.data.entities;

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

import tlvibes.logic.boundaries.identifiers.CommandId;
import tlvibes.logic.infrastructure.SuperAppMapToJsonConverter;

@Entity
public class MiniAppCommandEntity {
	@EmbeddedId private CommandId commandId;
	
	@JoinColumns({
		  @JoinColumn(name = "TARGET_OBJECT_SUPERAPP", referencedColumnName = "superApp"),
		  @JoinColumn(name = "TARGET_OBJECT_INTERNAL_ID", referencedColumnName = "internalObjectId")
		 })
	@OneToOne
	private SuperAppObjectEntity targetObject;
	
	@JoinColumns({
		  @JoinColumn(name = "INVOKER_USER_SUPERAPP", referencedColumnName = "superApp"),
		  @JoinColumn(name = "INVOKER_USER_EMAIL", referencedColumnName = "email")
		 })
	@OneToOne
	private UserEntity invokedBy;	
	
	@Lob
	@Convert(converter = SuperAppMapToJsonConverter.class)
	
	private Map<String, Object> commandAttributes;
	private Date invocationTimestamp; 
	private String command;

	public MiniAppCommandEntity() {	
		this.command = "doSomething";
		this.invocationTimestamp = new Date();
		this.commandAttributes = new HashMap<>();
		this.commandAttributes.put("key1", "can be anything you wish, even a nested json");		
	}
	
	public MiniAppCommandEntity(CommandId commandId,SuperAppObjectEntity targetObject,UserEntity invokedBy) {	
		this();
		this.commandId = commandId;
		this.targetObject = targetObject;
		this.invokedBy = invokedBy;		
	}

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

	public SuperAppObjectEntity getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(SuperAppObjectEntity targetObject) {
		this.targetObject = targetObject;
	}

	@Temporal(TemporalType.TIMESTAMP) 
	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}


	public UserEntity getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserEntity invokedBy) {
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
