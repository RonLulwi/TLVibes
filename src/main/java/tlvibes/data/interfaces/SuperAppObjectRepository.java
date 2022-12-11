package tlvibes.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.logic.boundaries.identifiers.ObjectId;

public interface SuperAppObjectRepository extends CrudRepository<SuperAppObjectEntity,ObjectId> {

}	
