package superapp.logic.interfaces;

import java.util.List;

import superapp.logic.boundaries.MiniAppCommandBoundary;

public interface EnhancedMiniAppCommandsService extends MiniAppCommandsService {
	
	public List<MiniAppCommandBoundary> getAllCommands(int size,int page);	
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName,int size,int page);

}
