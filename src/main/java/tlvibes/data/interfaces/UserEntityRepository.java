package tlvibes.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.identifiers.UserId;

public interface UserEntityRepository extends CrudRepository<UserEntity, UserId>{

}
