package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;

@RestController
public class AdminController {
	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.DELETE})
	public void DeleteAllUsers() {
		// TODO delete all objects in database
	}
	@RequestMapping(
			path= {"/superapp/admin/objects"},
			method = {RequestMethod.DELETE})
	public void DeleteAllObjects() {
		// TODO delete all objects in database
	}
	@RequestMapping(
			path= {"/superapp/admin/miniapp/{selectedappname}"},
			method = {RequestMethod.DELETE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public String DeleteSelectedMiniappHistory(@PathVariable("selectedappname") String name) {
		return "All The History Of MiniApp With Name \"" + name + "\" Was Deleted Successfuly";
	}
	
	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
			public MiniAppCommandBoundary GetAllUsers() {
			return  null; // need return array of user boundaries
	}
}
