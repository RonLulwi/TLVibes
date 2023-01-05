package superapp.logic.superAppInvokables;

import org.springframework.stereotype.Component;

import superapp.data.MiniAppCommandEntity;

@Component("TEST.echo")
public class EchoCommand implements ICommandInvokable{

	@Override
	public Object Invoke(MiniAppCommandEntity command) {	
		return command;
	}

}
