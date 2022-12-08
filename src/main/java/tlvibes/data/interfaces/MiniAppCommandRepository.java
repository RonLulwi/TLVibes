package tlvibes.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.logic.boundaries.identifiers.CommandId;

public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity,CommandId> {

}
