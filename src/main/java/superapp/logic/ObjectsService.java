package superapp.logic;

import java.util.List;

import superapp.logic.boundaries.ObjectBoundary;


public interface ObjectsService {
	
	
	public ObjectBoundary createObject(ObjectBoundary objWithotId);
	
	@Deprecated
	public ObjectBoundary updateObject(String objectSuperApp,String internalObjectId, ObjectBoundary objectBoundary);
	
	@Deprecated
	public ObjectBoundary getSpecificObject(String objectSuperApp,String internalObjectId);
	
	@Deprecated
	public List<ObjectBoundary> getAllObjects();
	
	@Deprecated
	public void deleteAllObjects();
	
	
}
