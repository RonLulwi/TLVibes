package superapp.logic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.interfaces.EnhancedMiniAppCommandsService;

@RestController
public class miniAppController {
	private EnhancedMiniAppCommandsService MiniAppCommand;

	@Autowired
	public void setMiniAppController(EnhancedMiniAppCommandsService MiniAppService) {
		this.MiniAppCommand = MiniAppService;
	}
	
	@RequestMapping(
			path = "/superapp/miniapp/{miniAppName}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public Object invokeMiniAppCommand(
				@PathVariable("miniAppName") String miniAppName,
				@RequestBody MiniAppCommandBoundary boundary,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail) {
			//TODO: Validate that the user is MINIAPP_USER 
			//		and the target object is exist in the database
			return this.MiniAppCommand.invokeCommand(miniAppName, boundary);
		}
	
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/miniapp/getAllCommands",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommands (
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail,
				@RequestParam(name = "size", required = false, defaultValue = "10")
				int size, 
				@RequestParam(name = "page", required = false, defaultValue = "0") 
				int page) {
		//TODO: Validate that the user is ADMIN
			return this.MiniAppCommand
					.getAllCommands(size, page)
					.toArray(new MiniAppCommandBoundary[0]);
		}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/miniapp/getAllCommandsOf/{miniAppName}",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommandsOfSpecificMiniApp (
				@PathVariable("miniAppName") String miniAppName,
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
				String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
				String userEmail,
				@RequestParam(name = "size", required = false, defaultValue = "10")
				int size, 
				@RequestParam(name = "page", required = false, defaultValue = "0") 
				int page) {
		//TODO: Validate that the user is ADMIN
			return this.MiniAppCommand
					.getAllMiniAppCommands(miniAppName, size, page)
					.toArray(new MiniAppCommandBoundary[0]);
		}
	
	@RequestMapping(
			path= {"/superapp/miniapp/deleteAllCommands"},
			method = {RequestMethod.DELETE})
		public void DeleteAllCommands(
				@RequestParam(name = "userSuperapp", required = false, defaultValue = "") 
					String userSuperApp, 
				@RequestParam(name = "userEmail", required = false, defaultValue = "")
					String userEmail) {
		//TODO: Validate that the user is ADMIN
			this.MiniAppCommand.deleteAllCommands();
		}
		
//	@RequestMapping(
//			path= {"/superapp/miniapp/TEST"},
//			method = {RequestMethod.POST},
//			produces = MediaType.APPLICATION_JSON_VALUE,
//			consumes = MediaType.APPLICATION_JSON_VALUE)
//		public Object objectTimeTravel(MiniAppCommandBoundary update) {
//			return this.MiniAppCommand.invokeCommand(update);
//		}

}
