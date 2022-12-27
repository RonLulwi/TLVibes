package superapp.data.interfaces;

import org.springframework.data.repository.PagingAndSortingRepository;

import superapp.data.entities.UserEntity;
import superapp.logic.boundaries.identifiers.UserId;

public interface UserEntityRepository extends PagingAndSortingRepository<UserEntity, UserId>{

}
