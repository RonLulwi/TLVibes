package superapp.logic;

import java.util.List;

import superapp.logic.boundaries.MiniAppCommandBoundary;

public interface EnhancedMiniAppCommandsService extends MiniAppCommandsService {
	
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperApp, String userEmail, int size,int page);	
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String userSuperApp, String userEmail, String miniAppName,int size,int page);
	
	public void deleteAllCommands(String userSuperApp, String userEmail);
	
	public Object invokeCommand(String userSuperApp, String userEmail, String miniAppName, MiniAppCommandBoundary command);

}
