package superapp.logic.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.interfaces.EnhancedUsersService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	private EnhancedUsersService userService;
	
	@Autowired
	public void setUserService(EnhancedUsersService userService) {
		this.userService = userService;
	}


	
	@RequestMapping(
			path= {"/superapp/users/login/{superapp}/{email}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
		public UserBoundary loginAndRetreiveDetails(
				@PathVariable("superapp") String superApp,
				@PathVariable("email") String email ) {
			return userService.login(superApp, email);
		}

	@RequestMapping(
			path= {"/superapp/users"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
		public UserBoundary createUser(
				@RequestBody NewUserBoundary newUserBoundary) {
			return userService.createUser(newUserBoundary);
		}
	
	@RequestMapping(
			path = "/superapp/users/{superapp}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@PathVariable("superapp") String superapp,
			@PathVariable("userEmail") String userEmail, 
			@RequestBody UserBoundary update) {
		userService.updateUser(superapp, userEmail, update);
				
	}
}
