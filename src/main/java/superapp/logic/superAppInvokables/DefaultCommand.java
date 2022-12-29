package superapp.logic.superAppInvokables;

import org.springframework.stereotype.Component;

import superapp.data.entities.MiniAppCommandEntity;

@Component("defaultCommand")
public class DefaultCommand implements ICommandInvokable{

	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		return "Command not found";
	}

}
