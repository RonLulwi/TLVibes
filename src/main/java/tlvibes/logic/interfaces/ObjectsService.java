package tlvibes.logic.interfaces;

import java.util.List;
import tlvibes.logic.boundaries.SuperAppObjectBoundary;


public interface ObjectsService {
	
	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary objectBoundary);
	public SuperAppObjectBoundary updateObject(String objectSuperApp,String internalObjectId, SuperAppObjectBoundary objectBoundary);
	public SuperAppObjectBoundary getSpecificObject(String objectSuperApp,String internalObjectId);
	public List<SuperAppObjectBoundary> getAllObjects();
	public void deleteAllObjects();
	
	
}
