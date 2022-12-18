package tlvibes.logic.convertes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tlvibes.data.entities.UserEntity;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.infrastructure.EmailValidator;

@Component
public class UserConvertor {
	private EmailValidator emailValidator;
	@Autowired
	public UserConvertor(EmailValidator emailValidator) {
		this.emailValidator = emailValidator;
	}

	public UserEntity UserBoundaryToEntity(UserBoundary boundary)	{
		
		emailValidator.validate(boundary.getUserId().getEmail());
		
		UserEntity entity = new UserEntity();
		
		entity.setUserId(boundary.getUserId());
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(boundary.getRole());
		entity.setUsername(boundary.getUsername());
		
		return entity;
	}

	public UserBoundary UserEntityToBoundary(UserEntity entity) {
		
		UserBoundary boundary = new UserBoundary();
		
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getRole());
		boundary.setUserId(entity.getUserId());
		boundary.setUsername(entity.getUsername());
		
		return boundary;
		
	}
}
