package superapp.logic.superAppInvokables;

import superapp.data.MiniAppCommandEntity;

public interface ICommandInvokable {
	public Object Invoke(MiniAppCommandEntity command);
}
