package superapp.logic.interfaces;

import java.util.List;

import superapp.logic.boundaries.MiniAppCommandBoundary;


public interface MiniAppCommandsService {
	
	public Object invokeCommand(MiniAppCommandBoundary command);
	
	public List<MiniAppCommandBoundary> getAllCommands();
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
	
	public void deleteAllCommands();
}
