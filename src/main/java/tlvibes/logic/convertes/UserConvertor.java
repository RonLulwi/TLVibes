package tlvibes.logic.convertes;

import org.springframework.stereotype.Component;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.UserBoundary;

@Component
public class UserConvertor {

	public UserEntity UserBoundaryToEntity(UserBoundary boundary)
	{
		UserEntity entity = new UserEntity();
		
		entity.setEmail(boundary.getUserId().getEmail());
		entity.setFirstName(boundary.getusername().split(" ")[0]);
		entity.setLastName(boundary.getusername().split(" ")[1]);
		entity.setUserName(boundary.getavatar());

		return entity;
	}
}
