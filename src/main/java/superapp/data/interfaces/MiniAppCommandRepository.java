package superapp.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import superapp.data.entities.MiniAppCommandEntity;
import superapp.logic.boundaries.identifiers.CommandId;

public interface MiniAppCommandRepository extends CrudRepository<MiniAppCommandEntity,CommandId> {

}
