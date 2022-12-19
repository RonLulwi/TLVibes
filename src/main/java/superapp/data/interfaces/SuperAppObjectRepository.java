package superapp.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import superapp.data.entities.SuperAppObjectEntity;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface SuperAppObjectRepository extends CrudRepository<SuperAppObjectEntity,SuperAppObjectIdBoundary> {

}	
