package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.ObjectBoundary;

@RestController
public class ObjectController {
	
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects/{superapp}/{InternalObjectId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary objectBoundary (
				@PathVariable("superapp") String superapp
				,@PathVariable("InternalObjectId") String InternalObjectId
				){
			System.err.println("supperapp=" + superapp +", InternalObjectId=" + InternalObjectId);
			return new ObjectBoundary();
		}
	
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/objects",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary[] objectBoundary (){
			ObjectBoundary[] objs = new ObjectBoundary[3];
			for(int i = 0; i < objs.length; ++i) {
				objs[i] = new ObjectBoundary();
				
			}
			return objs;
		}
	
}
