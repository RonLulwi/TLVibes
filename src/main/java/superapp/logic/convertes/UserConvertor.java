package superapp.logic.convertes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import superapp.data.entities.UserEntity;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.EmailValidator;

@Component
public class UserConvertor {
	private EmailValidator emailValidator;
	@Autowired
	public UserConvertor(EmailValidator emailValidator) {
		this.emailValidator = emailValidator;
	}

	public UserEntity toEntity(UserBoundary boundary, UserId userId)	{
		
		if(!emailValidator.validate(userId.getEmail()))
		{
			throw new IllegalArgumentException("Invalid email");
		}
		
		UserEntity entity = new UserEntity();
		
		entity.setUserId(userId);
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(boundary.getRole());
		entity.setUsername(boundary.getUsername());
		
		return entity;
	}

	public UserBoundary toBoundary(UserEntity entity) {
		
		UserBoundary boundary = new UserBoundary();
		
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getRole());
		boundary.setUserId(entity.getUserId());
		boundary.setUsername(entity.getUsername());
		
		return boundary;
		
	}
}
