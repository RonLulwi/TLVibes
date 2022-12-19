package superapp.logic.interfaces;

import java.util.List;

import superapp.logic.boundaries.ObjectBoundary;


public interface ObjectsService {
	
	public ObjectBoundary createObject(ObjectBoundary objWithotId);
	public ObjectBoundary updateObject(String objectSuperApp,String internalObjectId, ObjectBoundary objectBoundary);
	public ObjectBoundary getSpecificObject(String objectSuperApp,String internalObjectId);
	public List<ObjectBoundary> getAllObjects();
	public void deleteAllObjects();
	
	
}
