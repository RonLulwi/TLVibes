package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;
import demo.boundaries.identifiers.UserId;
import demo.enums.Role;

@RestController
public class AdminController {
	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.DELETE})
	public void DeleteAllUsers() {
		// TODO delete all users in database
	}
	@RequestMapping(
			path= {"/superapp/admin/objects"},
			method = {RequestMethod.DELETE})
	public void DeleteAllObjects() {
		// TODO delete all objects in database
	}
	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.DELETE})
	public void DeleteCommandHistory() {
		//TODO delete later
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp/{selectedappname}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] One_Command(
			@PathVariable("selectedappname") String selectedappname
			) {
		return new MiniAppCommandBoundary[] {new MiniAppCommandBoundary(selectedappname)};
	}

	@RequestMapping(
			path= {"/superapp/admin/miniapp"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public MiniAppCommandBoundary[] Commands() {
		MiniAppCommandBoundary command_1=new MiniAppCommandBoundary("miniApp1");
		MiniAppCommandBoundary command_2=new MiniAppCommandBoundary("miniApp2");
		MiniAppCommandBoundary command_3=new MiniAppCommandBoundary("miniApp3");
		MiniAppCommandBoundary[] allCommands = {command_1,command_2,command_3};
		return allCommands;
	}

	@RequestMapping(
			path= {"/superapp/admin/users"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public UserBoundary[] GetAllUsers() {
		UserBoundary[] users = new UserBoundary[3];
		for(int i = 0; i <3 ; i++) {
			String miniAppName = "miniApp" + i;
			String userName = "User" + i;
			users[i] = new UserBoundary(
					new UserId(userName, userName + "@demo.com"),
					Role.MINIAPP_USER,
					miniAppName,
					Character.toString(65 + i)
					);
		}
		return users;
	}

}
