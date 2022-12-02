package tlvibes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.services.UserService;

public class Tests {
	
	@Test
	public void UpdateUserEntityFromUserBoundery_OnlyMutableFieldsAreUpdated()
	{
		//Arrange
		var update = new UserBoundary(new UserId("newUser","testMail"),Role.ADMIN,"newUserName","newAvatar");
		var currentUser = new UserBoundary(new UserId("testUser","testMail"));
		var userService = new UserService();
		
		userService.createUser(currentUser);
		
		//Act
		userService.updateUser(null, currentUser.getUserId().getEmail(), update);
		//Assert
		assertFalse(currentUser.getUserId().getEmail().equals(update.getUserId().getEmail()));
		assertTrue(currentUser.getUserId().getSuperApp().equals(update.getUserId().getSuperApp()));
		assertTrue(currentUser.getrole().equals(update.getrole()));
		assertTrue(currentUser.getusername().equals(update.getusername()));

	}

}
