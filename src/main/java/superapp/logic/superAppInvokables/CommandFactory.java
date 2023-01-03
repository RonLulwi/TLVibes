package superapp.logic.superAppInvokables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import superapp.data.MiniAppCommandEntity;


@Component
public class CommandFactory {
    @Autowired
    private ApplicationContext appContext;
    @Value("${command.defualt}")
    private String defualtCommand;

	public ICommandInvokable GetInvokableCommand(MiniAppCommandEntity command) {
		try {
			return (ICommandInvokable) appContext.getBean(command.getCommandId().getMiniapp() 
					+ "." + command.getCommand(),ICommandInvokable.class);
		}
		catch (Exception e) {
			return (ICommandInvokable) appContext.getBean(defualtCommand,ICommandInvokable.class);
		}
	}
	
}
