package superapp.logic.superAppInvokables;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import superapp.data.MiniAppCommandEntity;
import superapp.data.SuperAppObjectEntity;
import superapp.data.identifiers.ObjectId;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.convertes.ObjectConvertor;

@Component("TEST.objectTimeTravel")
public class ObjectTimeTravelCommand implements ICommandInvokable {

	private SuperAppObjectRepository objectsRepositoy;
	private ObjectMapper jackson;
	private ObjectConvertor convertor;
	
	
	@Autowired
	public void setConvertor(ObjectConvertor convertor) {
		this.convertor = convertor;
	}
	
	@Autowired
	public void setObjectsRepositoy(SuperAppObjectRepository objectsRepositoy) {
		this.objectsRepositoy = objectsRepositoy;
	}
	
	@Autowired
	public void setJackson(ObjectMapper jackson) {
		this.jackson = jackson;
	}

	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		SuperAppObjectIdBoundary boundaryId = command.getTargetObject().get("objectId");
		
		ObjectId objectId = convertor.toEntityId(boundaryId);
		
		Optional<SuperAppObjectEntity> optionalEntity= objectsRepositoy.findById(objectId);
		
		if(optionalEntity.isEmpty())
		{
			throw new RuntimeException("Could not find superAppObject by id : " + objectId );
		}
		
		SuperAppObjectEntity current = optionalEntity.get();
		
		String newTimeStamp = "\"" + command.getCommandAttributes().get("creationTimestamp") +"\"" ;
				
		Object updatedData;
		
		try {
			jackson.registerModule(new JavaTimeModule());

			updatedData = jackson.readValue(newTimeStamp, String.class);
		} catch (Exception e) {
			throw new InvalidParameterException(newTimeStamp);
		}

		Date d = Date.from(Instant.parse(updatedData.toString()));
		
		current.setCreationTimestamp(d);
		
		return objectsRepositoy.save(current);
		
	}


	

}
