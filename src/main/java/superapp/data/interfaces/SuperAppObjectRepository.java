package tlvibes.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface SuperAppObjectRepository extends CrudRepository<SuperAppObjectEntity,SuperAppObjectIdBoundary> {

}	
