package tlvibes.logic.interfaces;

import tlvibes.logic.boundaries.ObjectBoundary;
import tlvibes.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface EnhancedObjectsService extends ObjectsService {
	void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary objectId);
	ObjectBoundary[] GetAllChildrens(String parentSuperApp, String parentInternalId);
	ObjectBoundary[] GetParent(String parentSuperApp, String parentInternalId);

}
