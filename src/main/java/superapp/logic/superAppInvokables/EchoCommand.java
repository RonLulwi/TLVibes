package superapp.logic.superAppInvokables;

import org.springframework.stereotype.Component;

import superapp.data.entities.MiniAppCommandEntity;

@Component("echo")
public class EchoCommand implements ICommandInvokable{
	
	@Override
	public Object Invoke(MiniAppCommandEntity command) {
				
		return command;
	}

}
