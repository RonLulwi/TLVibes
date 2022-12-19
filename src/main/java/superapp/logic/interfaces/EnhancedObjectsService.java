package superapp.logic.interfaces;

import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface EnhancedObjectsService extends ObjectsService {
	void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary objectId);
	ObjectBoundary[] GetAllChildrens(String parentSuperApp, String parentInternalId);
	ObjectBoundary[] GetParent(String parentSuperApp, String parentInternalId);

}
