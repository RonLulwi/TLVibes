package tlvibes.logic.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tlvibes.logic.boundaries.ObjectBoundary;
import tlvibes.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import tlvibes.logic.interfaces.EnhancedObjectsService;

@RestController
public class ObjectController {

	private EnhancedObjectsService  enhancedObjectsService;
	
	@Autowired
	public void setObjectService(EnhancedObjectsService enhancedObjectsService) {
		this.enhancedObjectsService = enhancedObjectsService;
	}

	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary retriveObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId
			){
		return this.enhancedObjectsService.getSpecificObject(superapp, InternalObjectId);
	}


	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ObjectBoundary> retriveAllObjects (){
		return this.enhancedObjectsService.getAllObjects();
	}


	@RequestMapping(
			path = "/superapp/objects",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary createObject (@RequestBody ObjectBoundary objWithoutId) {
			return this.enhancedObjectsService.createObject(objWithoutId);
	}


	@RequestMapping(
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateObject (
			@PathVariable("superapp") String superapp,
			@PathVariable("InternalObjectId") String InternalObjectId, 
			@RequestBody ObjectBoundary update) {
		this.enhancedObjectsService.updateObject(superapp, InternalObjectId, update);
		}
		
		@RequestMapping(
				path = "/superapp/objects/{superapp}/{InternalObjectId}/children",
				method = RequestMethod.PUT,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void updateObject (
				@PathVariable("superapp") String superapp,
				@PathVariable("InternalObjectId") String InternalObjectId, 
				@RequestBody SuperAppObjectIdBoundary update) {
			this.enhancedObjectsService.BindExistingObjectToAnExisitingChild(superapp, InternalObjectId, update);

	}
}
