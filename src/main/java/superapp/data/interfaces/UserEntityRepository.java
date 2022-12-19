package superapp.data.interfaces;

import org.springframework.data.repository.CrudRepository;

import superapp.data.entities.UserEntity;
import superapp.logic.boundaries.identifiers.UserId;

public interface UserEntityRepository extends CrudRepository<UserEntity, UserId>{

}
