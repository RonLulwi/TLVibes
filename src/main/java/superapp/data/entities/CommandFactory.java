package superapp.data.entities;

import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

	public MiniAppCommandEntity GetCommand(String command) {
		switch(command)
		{
		case "objectTimeTravel":
			return new ObjectTimeTravelCommand();
		case "echo":
			//TODO: return new echo command;
			return new MiniAppCommandEntity();
		default:
			return new MiniAppCommandEntity();		
		}
	}
	
}
