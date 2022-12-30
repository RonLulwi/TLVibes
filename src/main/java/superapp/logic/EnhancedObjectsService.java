package superapp.logic;

import java.util.List;
import java.util.Set;

import superapp.data.enums.CreationEnum;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface EnhancedObjectsService extends ObjectsService {
	void BindExistingObjectToAnExisitingChild(String parentSuperApp, String parentInternalId, SuperAppObjectIdBoundary objectId);
	
	List<ObjectBoundary> getAllChildrens(String parentSuperApp, String parentInternalId,int page,int size);
	
	Set<SuperAppObjectIdBoundary> getParent(String parentSuperApp, String parentInternalId,int page,int size);
	
	Set<ObjectBoundary> searchObjectsByCreationTimeStamp(CreationEnum creation, int page, int size);
	
	ObjectBoundary updateObject(String objectSuperApp,String internalObjectId, ObjectBoundary objectBoundary
			,String userSuperApp, String userEmail);
	
	List<ObjectBoundary> getAllObjects(int page, int size);

	List<ObjectBoundary> getAllObjectsByType(String type, String userSuperApp, String userEmail, int page, int size);

	List<ObjectBoundary> getAllObjectsByAlias(String alias, String userSuperApp, String userEmail, int page, int size);

	List<ObjectBoundary> getAllObjectsByAliasContainingText(String text, String userSuperApp, String userEmail,
			int page, int size);

}