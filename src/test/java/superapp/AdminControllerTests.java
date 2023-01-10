package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserRole;
import superapp.data.enums.CreationEnum;
import superapp.logic.ObjectsService;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {
	private int port;
	private RestTemplate restTemplate;
	private String userURL;
	private String objectURL;
	private String url;
	private ConfigProperties configProperties;
	private ObjectMapper jackson;
	private ControllersTestsHelper helper;
	private String baseUrl;
	private ObjectConvertor objectConvertor;

	@Autowired
	public void setObjectConvertor(ObjectConvertor objectConvertor) {
		this.objectConvertor = objectConvertor;
	}

	@Autowired
	public void setConfigProperties(ConfigProperties configProperties) {
		this.configProperties = configProperties;
	}
	@Autowired
	public void setConfigObjectMapper(ObjectMapper jackson) {
		this.jackson = jackson;
	}

	@Autowired
	public void setHelper(ControllersTestsHelper helper) {
		this.helper = helper;
	}


	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.baseUrl = "http://localhost:" + this.port;
		this.url = this.baseUrl + helper.adminPrefix;

	}

	@AfterEach
	public void teardown() {
		helper.TeadDown();
	}


	private UserBoundary createAdminBoundary() throws JsonMappingException, JsonProcessingException {

		String adminUserBoundaryAsString  = helper.GetAdminUserBoundaryAsJson();
		NewUserBoundary adminUserBoundary = jackson.readValue(adminUserBoundaryAsString,NewUserBoundary.class);
		return this.restTemplate
				.postForObject(this.baseUrl  + helper.userPrefix, adminUserBoundary, UserBoundary.class);

	}


	private UserBoundary createSuperappUserBoundary() throws JsonMappingException, JsonProcessingException {

		String superappUserBoundaryAsString= helper.GetSuperAppUserBoundaryAsJson();
		NewUserBoundary superappUserBoundary = jackson.readValue(superappUserBoundaryAsString,NewUserBoundary.class);

		return this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superappUserBoundary, UserBoundary.class);

	}
	
	private UserBoundary createMiniappUserBoundary() throws JsonMappingException, JsonProcessingException {

		String miniappUserBoundryAsString = helper.GetMiniAppUserBoundaryAsJson();
		NewUserBoundary minappUserBoundary = jackson.readValue(miniappUserBoundryAsString,NewUserBoundary.class);
		return this.restTemplate
		.postForObject(this.baseUrl + helper.userPrefix, minappUserBoundary, UserBoundary.class);
	}
	

	private UserBoundary createUserBoundaryByRoleAndEmail(UserRole role, String userEmail) 
			throws JsonMappingException, JsonProcessingException {

		String miniappUserBoundryAsString = helper.GetMiniAppUserBoundaryAsJson();
		NewUserBoundary minappUserBoundary = jackson.readValue(miniappUserBoundryAsString,NewUserBoundary.class);
		minappUserBoundary.setRole(role);
		minappUserBoundary.setEmail(userEmail);
		return  this.restTemplate
			.postForObject(this.baseUrl + helper.userPrefix, minappUserBoundary, UserBoundary.class);
	}
	
	private ObjectBoundary createObjectBySuperappUser(UserId userId) throws JsonMappingException, JsonProcessingException {
		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		
		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", userId);

		objectBoundary.setCreatedBy(createdBy);


		return this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);
	}
	
	private Object createSpesificCommand(String miniAppName, String command, 
			UserId userId, SuperAppObjectIdBoundary targetObjectId) 
					throws JsonMappingException, JsonProcessingException {
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand(command);
		
		Map<String,Object> commandAttributes = new HashMap<>();

		commandAttributes.put("creationTimestamp", "1985-10-26T01:22:00.555+00:00");
		
		commandBoundary.setCommandAttributes(commandAttributes);

		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", userId);

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", targetObjectId);

		commandBoundary.setTargetObject(targetObject);
		
		
		return this.restTemplate.postForObject(
				this.baseUrl
				+ helper.miniappPrefix
				+ miniAppName
				, commandBoundary, Object.class);
	}
	
	

	
	@Test
	public void testGetAllUsersHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		
		UserBoundary adminRes = createAdminBoundary();

		UserBoundary minappRes = createMiniappUserBoundary();
				
		UserBoundary superappRes = createSuperappUserBoundary();
				
				
		var res = this.restTemplate.getForObject(
				this.url
				+ "users"
				+ "?userSuperapp="
				+ adminRes.getUserId().getSuperapp()
				+ "&userEmail="
				+ adminRes.getUserId().getEmail()
				, UserBoundary[].class);

		assertNotNull(res);
		assertThat(Arrays.asList(res).contains(minappRes));
		assertThat(Arrays.asList(res).contains(superappRes));
		assertThat(Arrays.asList(res).contains(adminRes));
	}



	@Test
	public void testGetAllUsersWithNoPermissionThrowsException() throws JsonMappingException, JsonProcessingException
	{
		createAdminBoundary();

		UserBoundary minappRes = createMiniappUserBoundary();
				
		createSuperappUserBoundary();

		var res = assertThrows(Exception.class, () -> {
			this.restTemplate.getForObject(
					this.url
					+ "users"
					+ "?userSuperapp="
					+ minappRes.getUserId().getSuperapp()
					+ "&userEmail="
					+ minappRes.getUserId().getEmail()
					, UserBoundary[].class);
		});

		assertTrue(res.getMessage().contains("Only ADMIN has permission"));
	}
	
	@Test
	public void testGetAllUsersUsingPagination() throws JsonMappingException, JsonProcessingException
	{
		
		UserBoundary adminRes = createAdminBoundary();
		UserBoundary minappRes = createMiniappUserBoundary();
		UserBoundary minappRes2 = createUserBoundaryByRoleAndEmail(
				minappRes.getRole(), "miniappTest@gmail.com");
		UserBoundary minappRes3 = createUserBoundaryByRoleAndEmail(
				minappRes.getRole(), "miniappTest2@gmail.com");
		UserBoundary superappRes = createSuperappUserBoundary();
		UserBoundary superappRes2 = createUserBoundaryByRoleAndEmail(
				superappRes.getRole(), "superappTest@gmail.com");
		UserBoundary superappRes3 = createUserBoundaryByRoleAndEmail(
				superappRes.getRole(), "superappTest@gmail.com");
		
		
		List<UserBoundary> lst = new ArrayList<>();
		lst.add(adminRes);
		lst.add(minappRes);
		lst.add(minappRes2);
		lst.add(minappRes3);
		lst.add(superappRes2);
		lst.add(superappRes3);
		
		var res = this.restTemplate.getForObject(
				this.url
				+ "users"
				+ "?userSuperapp="
				+ adminRes.getUserId().getSuperapp()
				+ "&userEmail="
				+ adminRes.getUserId().getEmail()
				+ "&size=3"
				+ "&page=0"
				, UserBoundary[].class);

		assertNotNull(res);
		assertNotEquals(lst, Arrays.asList(res));
		assertThat(lst.containsAll(Arrays.asList(res)));
	}
	
	
	@Test
	public void testGetAllCommandsForSpecificMiniAppHappyFlow() 
			throws JsonMappingException, JsonProcessingException
	{
		//TODO: Create equals method to Commands!
		
		var createAdminUserRes = createAdminBoundary();
		
		var createMiniAppUserRes = createMiniappUserBoundary();
		
		var createSuperAppRes = createSuperappUserBoundary();
		
		var createObjectResponse = createObjectBySuperappUser(createSuperAppRes.getUserId());
		
		var response = createSpesificCommand(
				"weather", "getTlvWeather",
				createMiniAppUserRes.getUserId(),createObjectResponse.getObjectId()); 
		
		var response2 = createSpesificCommand(
				"weather", "getTlvWeather",
				createMiniAppUserRes.getUserId(),createObjectResponse.getObjectId()); 
		
		var response3 = createSpesificCommand(
				"weather", "getTlvWeather",
				createMiniAppUserRes.getUserId(),createObjectResponse.getObjectId());
		
		var response4 = createSpesificCommand(
				"TEST", "objectTimeTravel",
				createMiniAppUserRes.getUserId(),createObjectResponse.getObjectId());
		 
		
		assertNotNull(response);
		assertNotNull(response2);
		assertNotNull(response3);
		assertNotNull(response4);		
		List<Object> responseLst = new ArrayList<>();
		responseLst.add(response);
		responseLst.add(response2);
		responseLst.add(response3);
		responseLst.add(response4);
		
		System.err.println(response4);
		
		
		var weatherCommandsRes = this.restTemplate.getForObject(
				this.baseUrl
				+ helper.adminPrefix
				+ "miniapp/weather"
				+ "?userSuperapp="
				+ createAdminUserRes.getUserId().getSuperapp()
				+ "&userEmail="
				+ createAdminUserRes.getUserId().getEmail()
				, MiniAppCommandBoundary[].class);
		
		assertNotNull(weatherCommandsRes);
		assertThat(Arrays.asList(weatherCommandsRes).containsAll(responseLst));

		
	}



	
	




}
