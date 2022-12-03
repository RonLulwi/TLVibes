package tlvibes.logic.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tlvibes.logic.boundaries.SuperAppObjectBoundary;
import tlvibes.logic.interfaces.ObjectsService;
import tlvibes.logic.services.ObjectService;

@RestController
public class ObjectController {

	private ObjectsService objectService;
	
	@Autowired
	public void setObjectService(ObjectService objectService) {
		this.objectService = objectService;
	}

	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public SuperAppObjectBoundary retriveObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId
			){
		//return new SuperAppObjectBoundary(new ObjectId(superapp,InternalObjectId));
		return this.objectService.getSpecificObject(superapp, InternalObjectId);
	}


	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public SuperAppObjectBoundary[] retriveAllObjects (){
		
//		SuperAppObjectBoundary[] objs = new SuperAppObjectBoundary[3];
//		for(int i = 0; i < objs.length; i++) {
//			
//			//objectId.setInternalObjectId(String.valueOf(100 + i));
//			objs[i] = new SuperAppObjectBoundary(new ObjectId());
//		}
//		return objs;
		
//		SuperAppObjectBoundary[] objs = new SuperAppObjectBoundary[2];
//		List<SuperAppObjectBoundary> l = this.objectService.getAllObjects();
//		objs[0] = l.get(0);
//		objs[1] = l.get(1);
		//return objs;
		
		var l = this.objectService.getAllObjects();
		SuperAppObjectBoundary[] objs = new SuperAppObjectBoundary[l.size()];
		
		for(int i = 0; i < objs.length; ++i) {
			objs[i] = l.get(i);
		}
		return objs;
	}


	@RequestMapping(
			path = "/superapp/objects",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public SuperAppObjectBoundary createObject (@RequestBody SuperAppObjectBoundary objWithoutId) {
		
//		SuperAppObjectBoundary copy =  new SuperAppObjectBoundary(obj);
//		copy.setObjectId(new ObjectId());
//		copy.setCreationTimestamp(new Date());
//		return copy;
		return this.objectService.createObject(objWithoutId);
		
	}




	@RequestMapping(
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId, 
			@RequestBody SuperAppObjectBoundary update) {
		// TODO update message if exists at database
		//System.err.println(update.getObjectId().getInternalObjectId());
		
		SuperAppObjectBoundary updated = this.objectService.updateObject(superapp, InternalObjectId, update); //TODO: figure bug
		System.err.println(updated);
		
	}






}
