package tlvibes.logic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;
import tlvibes.logic.boundaries.identifiers.CommandId;
import tlvibes.logic.interfaces.MiniAppCommandsService;

@RestController
public class miniAppController {
	private MiniAppCommandsService MiniAppCommand;

	@Autowired
	public void setMiniAppController(MiniAppCommandsService MiniAppService) {
		this.MiniAppCommand = MiniAppService;
	}
	
	@RequestMapping(
			path = "/superapp/miniapp/{miniAppName}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public Object invokeMiniAppCommand(
				@PathVariable("miniAppName") String miniAppName,
				@RequestBody MiniAppCommandBoundary boundary) {
			// TODO test if massage is in the database
			if(boundary.getCommandId() == null)
				boundary.setCommandId(new CommandId());
			boundary.getCommandId().setMiniApp(miniAppName);
			return this.MiniAppCommand.invokeCommand(boundary);
		}
	
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/miniapp/getAllCommands",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommands () {
			return this.MiniAppCommand
					.getAllCommands()
					.toArray(new MiniAppCommandBoundary[0]);
		}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/superapp/miniapp/getAllCommandsOf/{miniAppName}",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommandsOfSpecificMiniApp (@PathVariable("miniAppName") String miniAppName) {
			return this.MiniAppCommand
					.getAllMiniAppCommands(miniAppName)
					.toArray(new MiniAppCommandBoundary[0]);
		}
	
	@RequestMapping(
			path= {"/superapp/miniapp/deleteAllCommands"},
			method = {RequestMethod.DELETE})
		public void DeleteAllCommands() {
			this.MiniAppCommand.deleteAllCommands();
		}
		

}
