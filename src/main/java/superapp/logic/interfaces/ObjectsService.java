package superapp.logic.interfaces;

import java.util.List;

import superapp.logic.boundaries.ObjectBoundary;


public interface ObjectsService {
	
	ObjectBoundary createObject(ObjectBoundary objWithotId);
	@Deprecated
	ObjectBoundary updateObject(String objectSuperApp,String internalObjectId, ObjectBoundary objectBoundary);
	ObjectBoundary getSpecificObject(String objectSuperApp,String internalObjectId);
	@Deprecated
	List<ObjectBoundary> getAllObjects();
	void deleteAllObjects();
	
	
}
