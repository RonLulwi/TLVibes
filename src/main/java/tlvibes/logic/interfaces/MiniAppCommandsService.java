package tlvibes.interfaces;

import java.util.List;

import tlvibes.boundaries.MiniAppCommandBoundary;


public interface MiniAppCommandsService {
	
	public Object invokeCommand(MiniAppCommandBoundary command);
	
	public List<MiniAppCommandBoundary> getAllCommands();
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);
	
	public void deleteAllCommands();
}
