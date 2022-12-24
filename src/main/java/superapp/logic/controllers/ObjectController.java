package superapp.logic.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.data.enums.CreationEnum;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.interfaces.EnhancedObjectsService;

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
			@PathVariable("InternalObjectId") String InternalObjectId,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail){
		return this.enhancedObjectsService.getSpecificObject(superapp, InternalObjectId);
	}


	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ObjectBoundary> retriveAllObjects (
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10")
			int size){
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
			@RequestBody ObjectBoundary update,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail) {
		this.enhancedObjectsService.updateObject(superapp, InternalObjectId, update);
		}
		
		@RequestMapping(
				path = "/superapp/objects/{superapp}/{InternalObjectId}/children",
				method = RequestMethod.PUT,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void bindChild (
				@PathVariable("superapp") String superapp,
				@PathVariable("InternalObjectId") String InternalObjectId, 
				@RequestBody SuperAppObjectIdBoundary update,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail) {
			this.enhancedObjectsService.BindExistingObjectToAnExisitingChild(superapp, InternalObjectId, update);
		}
		
		@RequestMapping(
				path = "/superapp/objects/{superapp}/{InternalObjectId}/children",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary[] getChildrens (
				@PathVariable("superapp") String superapp,
				@PathVariable("InternalObjectId") String InternalObjectId,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail,
				@RequestParam(name = "page", required = false, defaultValue = "0") 
				int page, 
				@RequestParam(name = "size", required = false, defaultValue = "10")
				int size) {
			return this.enhancedObjectsService.GetAllChildrens(superapp, InternalObjectId)
					.toArray(new ObjectBoundary[0]);
		}
		
		@RequestMapping(
				path = "/superapp/objects/{superapp}/{InternalObjectId}/parents",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary[] getParents (
				@PathVariable("superapp") String superapp,
				@PathVariable("InternalObjectId") String InternalObjectId,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail,
				@RequestParam(name = "page", required = false, defaultValue = "0") 
				int page, 
				@RequestParam(name = "size", required = false, defaultValue = "10")
				int size) {
			return this.enhancedObjectsService.GetParent(superapp, InternalObjectId)
					.toArray(new ObjectBoundary[0]);
		}
		
		@RequestMapping(
				path = "/superapp/objects/search/byCreation/{creationEnum}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary[] searchObjectsByCreationTimeStamp (
				@PathVariable("creationEnum") CreationEnum creation,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail,
				@RequestParam(name = "page", required = false, defaultValue = "0") 
				int page, 
				@RequestParam(name = "size", required = false, defaultValue = "10")
				int size) {
			return this.enhancedObjectsService.SearchObjectsByCreationTimeStamp(creation, page, size)
					.toArray(new ObjectBoundary[0]);
		}

}
