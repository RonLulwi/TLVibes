
import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.NewUserBoundary;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class test {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigProperties configProperties;

	@Test
	public void UserIdCreatedWithDefaultAppNameAndEmail()
	{
		var userId = new UserId();
		String superAppName = System.getProperty("spring.application.name");

		assertNotNull(userId);
		assertNotNull(userId.getEmail());
		assertEquals(superAppName, userId.getSuperApp());
	}
	
	
	@Test
	public void UpdateUserEntityFromUserBoundery_OnlyMutableFieldsAreUpdated()
	{
		//Arrange
		var update = new UserBoundary(new UserId("newFirst newLast","testMail"),Role.ADMIN,"newUserName","newAvatar");
		var userWithDefualtValues = new NewUserBoundary();
		var userWithNewValues = new NewUserBoundary();
		userWithNewValues.setAvatar("new avatar");
		userWithNewValues.setRole(Role.ADMIN);
		
		userService.createUser(userWithDefualtValues);
		
		//Act
		userService.updateUser(configProperties.getSuperAppName(), userWithDefualtValues.getEmail(), update);
		UserBoundary currentUserAfterUpdate = userService.getAllUsers().get(0);
		
		//Assert
		assertFalse(currentUserAfterUpdate.getUserId().equals(update.getUserId()));
		assertTrue(currentUserAfterUpdate.getAvatar().equals(update.getAvatar()));
		assertTrue(currentUserAfterUpdate.getRole().equals(update.getRole()));
		assertTrue(currentUserAfterUpdate.getUsername().equals(update.getUsername()));

	}

}

