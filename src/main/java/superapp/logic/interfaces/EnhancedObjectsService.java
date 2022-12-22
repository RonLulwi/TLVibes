package superapp.logic.interfaces;

import java.util.Set;

import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface EnhancedObjectsService extends ObjectsService {
	void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary objectId);
	Set<SuperAppObjectIdBoundary> GetAllChildrens(String parentSuperApp, String parentInternalId);
	Set<SuperAppObjectIdBoundary> GetParent(String parentSuperApp, String parentInternalId);

}
