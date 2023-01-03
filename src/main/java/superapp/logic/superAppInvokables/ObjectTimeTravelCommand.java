package superapp.logic.superAppInvokables;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import superapp.data.MiniAppCommandEntity;
import superapp.data.SuperAppObjectEntity;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

@Component("TEST.objectTimeTravel")
public class ObjectTimeTravelCommand implements ICommandInvokable {

	private SuperAppObjectRepository objectsRepositoy;

	@Autowired
	public void setObjectsRepositoy(SuperAppObjectRepository objectsRepositoy) {
		this.objectsRepositoy = objectsRepositoy;
	}

	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		SuperAppObjectIdBoundary objectId = command.getTargetObject().get("objectId");
		
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
