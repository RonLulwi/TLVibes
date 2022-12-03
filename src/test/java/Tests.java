
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.services.UserService;

public class Tests {
	
	@Test
	public void UpdateUserEntityFromUserBoundery_OnlyMutableFieldsAreUpdated()
	{
		//Arrange
		var update = new UserBoundary(new UserId("newFirst newLast","testMail"),Role.ADMIN,"newUserName","newAvatar");
		var currentUser = new UserBoundary(new UserId("testUser","myMail"));
		var userService = new UserService();
		
		userService.createUser(currentUser);
		
		//Act
		userService.updateUser(null, currentUser.getUserId().getEmail(), update);
		var currentUserAfterUpdate = userService.getAllUsers().get(0);
		
		//Assert
		assertFalse(currentUserAfterUpdate.getUserId().equals(update.getUserId()));
		assertTrue(currentUserAfterUpdate.getAvatar().equals(update.getAvatar()));
		assertTrue(currentUserAfterUpdate.getRole().equals(update.getRole()));
		assertTrue(currentUserAfterUpdate.getUsername().equals(update.getUsername()));

	}

}
