package superapp.logic.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.services.MiniAppCommandService;
import superapp.logic.services.ObjectService;
import superapp.logic.services.UserService;

@RestController
public class AdminController {
	private UserService userService;
	private ObjectService objectService;
	private MiniAppCommandService commandService;
	
	public AdminController(UserService userService, ObjectService objectService, MiniAppCommandService commandService){
		this.commandService = commandService;
		this.objectService = objectService;
		this.userService = userService;
	}
	
	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.DELETE})
	public void DeleteAllUsers() {
		userService.deleteAllUsers();
	}
	@RequestMapping(
			path= {"/superapp/admin/objects"},
			method = {RequestMethod.DELETE})
	public void DeleteAllObjects() {
		objectService.deleteAllObjects();
	}
	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.DELETE})
	public void DeleteCommandHistory() {
		commandService.deleteAllCommands();
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp/{selectedappname}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] One_Command(
			@PathVariable("selectedappname") String selectedAppname
			) {
		return commandService.getAllMiniAppCommands(selectedAppname)
				.toArray(new MiniAppCommandBoundary[0]);
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] GetAllCommands() {
		return commandService.getAllCommands()
				.toArray(new MiniAppCommandBoundary[0]);
	}

	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary[] GetAllUsers() {
		return userService.getAllUsers()
				.toArray(new UserBoundary[0]);
	}

}
