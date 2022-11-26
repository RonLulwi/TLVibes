package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.ObjectBoundary;
import demo.boundaries.identifiers.ObjectId;

@RestController
public class ObjectController {


	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary retriveObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId
			){
		return new ObjectBoundary(new ObjectId(superapp,InternalObjectId));
	}


	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] retriveAllObjects (){
		ObjectBoundary[] objs = new ObjectBoundary[3];
		for(int i = 0; i < objs.length; i++) {
			ObjectId objectId = new ObjectId();
			objectId.setInternalObjectId(String.valueOf(100 + i));
			objs[i] = new ObjectBoundary(objectId);
		}
		return objs;
	}


	@RequestMapping(
			path = "/superapp/objects",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary createObject (@RequestBody ObjectBoundary obj) {
		// TODO store message to database
		ObjectBoundary copy =  new ObjectBoundary(obj);
		copy.setObjectId(null);
		return copy;
	}




	@RequestMapping(
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId, 
			@RequestBody ObjectBoundary update) {
		// TODO update message if exists at database
		System.err.println(update.getObjectId().getInternalObjectId());
		
	}






}
