package superapp;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import superapp.data.UserRole;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
	private int port;
	private RestTemplate restTemplate;
	private String url;
	private ConfigProperties configProperties;
	private ControllersTestsHelper helper;
	
	@Autowired
	public void setConfigProperties(ConfigProperties configProperties) {
		this.configProperties = configProperties;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/superapp/users";
	}
	
	@Autowired
	public void setHelper(ControllersTestsHelper helper) {
		this.helper = helper;
	}
	
	@AfterEach
	public void teardown() {
		helper.TeadDown();
//		this.restTemplate
//			.delete("http://localhost:" + this.port + "/superapp/admin/users");
	}
	

	@Test
	public void testContext() throws Exception {
	}
	
	@Test
	public void testCreateNewUserHappyFlow (){
		
		NewUserBoundary newUser = createValidNewUserBoundary();
		
		UserBoundary expectedUser = new UserBoundary();
		
		expectedUser.setUserId(new UserId(configProperties.getSuperAppName(),newUser.getEmail()));
		expectedUser.setUsername(newUser.getUsername());
		expectedUser.setAvatar(newUser.getAvatar());
		expectedUser.setRole(newUser.getRole());

		UserBoundary response = this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    
		assertEquals(expectedUser,response);
	}

	@Test
	public void testLoginWithExistingUserHappyFlow()
	{
		NewUserBoundary newUser = createValidNewUserBoundary();
		
		UserBoundary createResponse = this.restTemplate
				.postForObject(this.url, newUser, UserBoundary.class);
		
		assertNotNull(createResponse);
		
		UserBoundary loginResponse = this.restTemplate
				.getForObject(this.url + "/login/" +
						createResponse.getUserId().getSuperapp() + "/" +
						createResponse.getUserId().getEmail()
						, UserBoundary.class);
		
		assertEquals(createResponse,loginResponse);	
	}
	
	@Test
	public void testCreateNewUserWithNotValidEmail$InsteadOfAtSignThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		
		newUser.setEmail("testUser$gmail.com");
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("Invalid email"));
	}

	@Test
	public void testCreateNewUserWithNotValidEmailWithOutAtSignThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		
		newUser.setEmail("testUsergmail.com");
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("Invalid email"));
	}

	@Test
	public void testCreateNewUserWithNotValidEmailWithOutDotComThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		
		newUser.setEmail("testUser@gmailcom");
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("Invalid email"));
	}
	
	@Test
	public void testCreateNewUserWithNullAvatarThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		newUser.setAvatar(null);
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("status\":500"));
	}
	
	@Test
	public void testCreateNewUserWithEmptyStringAvatarThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		newUser.setAvatar("");
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("status\":500"));
	}

	@Test
	public void testCreateNewUserWithNullUserNameThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		newUser.setUsername(null);
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("status\":500"));
	}

	@Test
	public void testCreateNewUserWithEmptyStringUserNameThrowException () throws Exception {
		NewUserBoundary newUser = createValidNewUserBoundary();
		newUser.setUsername("");
		
	    Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
					.postForObject(this.url, newUser, UserBoundary.class);
	    });

		assertTrue(exception.getMessage().contains("status\":500"));
	}

	private NewUserBoundary createValidNewUserBoundary() {
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setUsername("testUserName");
		newUser.setEmail("testUser@gmail.com");
		newUser.setAvatar("testAvatar");
		newUser.setRole(UserRole.ADMIN);
		return newUser;
	}

}
