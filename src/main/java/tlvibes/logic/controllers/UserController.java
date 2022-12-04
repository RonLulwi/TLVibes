package tlvibes.logic.controllers;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;

@RestController
public class UserController {
	@RequestMapping(
			path= {"/superapp/users/login/{superapp}/{email}"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
		public UserBoundary loginAndRetreiveDetails(
				@PathVariable("superapp") String superApp,
				@PathVariable("email") String email ) {
			return new UserBoundary(new UserId(superApp,email));
		}

	@RequestMapping(
			path= {"/superapp/users"},
			method = {RequestMethod.POST},
			produces = {MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
		public UserBoundary createUser(
				@RequestBody UserBoundary newUserBoundary) {
		 	String email=newUserBoundary.getUserId().getEmail();
			return new UserBoundary(new UserId(email));
		}
	
	@RequestMapping(
			path = "/superapp/users/{superapp}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@PathVariable("superapp") String superapp,
			@PathVariable("userEmail") String userEmail, 
			@RequestBody UserBoundary update) {
		// TODO update user if exists at database
				
	}
}
