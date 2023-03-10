package superapp.logic.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.logic.EnhancedMiniAppCommandsService;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.services.ObjectService;
import superapp.logic.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
	private UserService userService;
	private ObjectService objectService;
	private EnhancedMiniAppCommandsService commandService;
	
	public AdminController(UserService userService, ObjectService objectService, EnhancedMiniAppCommandsService commandService){
		this.commandService = commandService;
		this.objectService = objectService;
		this.userService = userService;
	}
	
	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.DELETE})
	public void DeleteAllUsers(
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail) {
		userService.deleteAllUsers(userSuperApp, userEmail);
	}
	@RequestMapping(
			path= {"/superapp/admin/objects"},
			method = {RequestMethod.DELETE})
	public void DeleteAllObjects(
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail) {
		objectService.deleteAllObjects(userSuperApp, userEmail);
	}
	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.DELETE})
	public void DeleteCommandHistory(
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail) {
		commandService.deleteAllCommands(userSuperApp, userEmail);
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp/{selectedappname}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] One_Command(
			@PathVariable("selectedappname") String selectedAppname,
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10")
			int size, 
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			int page) {
		return commandService.getAllMiniAppCommands(userSuperApp, userEmail, selectedAppname,size, page)
				.toArray(new MiniAppCommandBoundary[0]);
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] GetAllCommands(
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10")
			int size, 
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			int page) {
		return commandService.getAllCommands(userSuperApp, userEmail, size,page)
				.toArray(new MiniAppCommandBoundary[0]);
	}

	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary[] GetAllUsers(
			@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
			String userSuperApp, 
			@RequestParam(name = "userEmail", required = false, defaultValue = "")
			String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10")
			int size, 
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			int page) { 
		return userService.getAllUsers(userSuperApp, userEmail ,size, page)
				.toArray(new UserBoundary[0]);
	}

}
