package superapp.logic.superAppInvokables;

import java.security.InvalidParameterException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.bytebuddy.implementation.bind.annotation.Argument;
import superapp.data.entities.MiniAppCommandEntity;
import superapp.data.entities.SuperAppObjectEntity;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.convertes.MiniAppCommandsConverter;
import superapp.logic.convertes.ObjectConvertor;

@Component(value = "objectTimeTravel")
public class ObjectTimeTravelCommand implements ICommandInvokable {

	private String commandName = "objectTimeTravel";
	private SuperAppObjectRepository objectsRepositoy;

	@Autowired
	public void setObjectsRepositoy(SuperAppObjectRepository objectsRepositoy) {
		this.objectsRepositoy = objectsRepositoy;
	}

	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		if(!command.getCommand().equalsIgnoreCase(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		if(!command.getCommandId().getMiniapp().equals("TEST")){
			throw new RuntimeException("command : " + commandName + "can be invoke only from MiniApp TEST");
		}

		SuperAppObjectIdBoundary objectId = command.getTargetObject().get("targetObject");
		
		Optional<SuperAppObjectEntity> optionalEntity= objectsRepositoy.findById(objectId);
		
		if(optionalEntity.isEmpty())
		{
			throw new RuntimeException("Could not find superAppObject by id : " + objectId );
		}
		
		SuperAppObjectEntity current = optionalEntity.get();
		
		String newTimeStamp = command.getCommandAttributes().get("creationTimestamp").toString();
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Date updatedData;
		
		try {
			updatedData = sdf.parse(newTimeStamp);
		} catch (ParseException e) {
			throw new InvalidParameterException(newTimeStamp);
		}

		current.setCreationTimestamp(updatedData);
		
		return objectsRepositoy.save(current);
		
	}

}
