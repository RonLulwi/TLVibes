package superapp.logic.superAppInvokables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class CommandFactory {
    @Autowired
    private ApplicationContext appContext;
    @Value("${command.defualt}")
    private String defualtCommand;

	public ICommandInvokable GetCommand(String command) {
		try {
			return (ICommandInvokable) appContext.getBean(command);
		}
		catch (Exception e) {
			return (ICommandInvokable) appContext.getBean(defualtCommand);
		}
	}
	
}
