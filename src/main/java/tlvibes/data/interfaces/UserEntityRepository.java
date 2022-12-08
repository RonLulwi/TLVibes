package tlvibes.data.interfaces;

import org.springframework.data.repository.CrudRepository;

public interface UserEntityRepository<UserEntity> extends CrudRepository<UserEntity, String>{

}
