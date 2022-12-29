package superapp.logic.superAppInvokables;

import superapp.data.entities.MiniAppCommandEntity;

public interface ICommandInvokable {
	public Object Invoke(MiniAppCommandEntity command);
}
