package superapp.data.entities;

import java.sql.Time;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

@Component
public class ObjectTimeTravelCommand extends MiniAppCommandEntity {

	private String commandName = "objectTimeTravel";
	private SuperAppObjectRepository objectsRepositoy;

	@Autowired
	public void setObjectsRepositoy(SuperAppObjectRepository objectsRepositoy) {
		this.objectsRepositoy = objectsRepositoy;
	}

	@Override
	public void execute() {
		
		if(this.getCommand() != commandName)
		{
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		SuperAppObjectIdBoundary objectId = this.getTargetObject().get("objectId");
		
		Optional<SuperAppObjectEntity> optionalEntity= objectsRepositoy.findById(objectId);
		
		if(optionalEntity.isEmpty())
		{
			throw new RuntimeException("Could not find superAppObject by id : " + objectId );
		}
		
		SuperAppObjectEntity current = optionalEntity.get();
		
		String newTimeStamp = this.getCommandAttributes().get("creationTimestamp").toString();
		
		current.setCreationTimestamp(Date.from(Time.valueOf(newTimeStamp).toInstant()));;
		
		objectsRepositoy.save(current);
		
	}

}
