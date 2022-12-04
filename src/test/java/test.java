



import tlvibes.data.enums.Role;
import tlvibes.logic.boundaries.UserBoundary;
import tlvibes.logic.boundaries.identifiers.UserId;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

/*
@EnableConfigurationProperties(value = ConfigProperties.class)
//@SpringJUnitConfig(initializers = ConfigProperties.class)
@TestPropertySource("classpath:application.properties")
public class test {
	


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
		var currentUser = new UserBoundary(new UserId("testUser","myMail"));
		var userService = new UserService();
		
		userService.createUser(currentUser);
		
		//Act
		userService.updateUser(null, currentUser.getUserId().getEmail(), update);
		UserBoundary currentUserAfterUpdate = userService.getAllUsers().get(0);
		
		//Assert
		assertFalse(currentUserAfterUpdate.getUserId().equals(update.getUserId()));
		assertTrue(currentUserAfterUpdate.getAvatar().equals(update.getAvatar()));
		assertTrue(currentUserAfterUpdate.getRole().equals(update.getRole()));
		assertTrue(currentUserAfterUpdate.getUsername().equals(update.getUsername()));

	}

}
*/
