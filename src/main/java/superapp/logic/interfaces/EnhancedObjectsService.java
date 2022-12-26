package superapp.logic.interfaces;

import java.util.List;
import java.util.Set;

import superapp.data.enums.CreationEnum;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface EnhancedObjectsService extends ObjectsService {
	void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary objectId);
	Set<SuperAppObjectIdBoundary> GetAllChildrens(String parentSuperApp, String parentInternalId);
	Set<SuperAppObjectIdBoundary> GetParent(String parentSuperApp, String parentInternalId);
	Set<ObjectBoundary> SearchObjectsByCreationTimeStamp(CreationEnum creation, int page, int size);
	List<ObjectBoundary> getAllObjects(int page, int size);

}
