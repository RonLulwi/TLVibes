package superapp.logic.superAppInvokables;

import org.springframework.stereotype.Component;

import superapp.data.MiniAppCommandEntity;

@Component("echo")
public class EchoCommand implements ICommandInvokable{
	private String commandName = "echo";

	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		if(!command.getCommandId().getMiniapp().equals("TEST")){
			throw new RuntimeException("command : " + commandName + "can be invoke only from MiniApp TEST");
		}
		
		return command;
	}

}
