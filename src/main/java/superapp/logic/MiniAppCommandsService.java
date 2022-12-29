package superapp.logic;

import java.util.List;


import superapp.logic.boundaries.MiniAppCommandBoundary;


public interface MiniAppCommandsService {
	
	public Object invokeCommand(String miniAppName, MiniAppCommandBoundary command);
	
	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands();
	
	@Deprecated
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
	
	public void deleteAllCommands();
}
