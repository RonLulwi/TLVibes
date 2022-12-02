package demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.identifiers.CommandId;
import demo.interfaces.MiniAppCommandsService;

@RestController
public class miniAppController {
	private MiniAppCommandsService MiniAppCommand;
	// tt
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
		return MiniAppCommand.invokeCommand(boundary);
	}

}
