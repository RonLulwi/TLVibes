package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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

import superapp.data.UserRole;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ObjectControllerTests {
	private int port;
	private RestTemplate restTemplate;
	private String url;
	private ConfigProperties configProperties;
	private ObjectMapper jackson;
	private ControllersTestsHelper helper;
	private String baseUrl;
	private String userPrefix;

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
		this.url = this.baseUrl + "/superapp/miniapp/" + configProperties.getSuperAppName();
		this.userPrefix = "/superapp/users/";


	}

	@AfterEach
	public void teardown() {
		helper.TeadDown();
		//			this.restTemplate
		//				.delete("http://localhost:" + this.port + "/superapp/admin/users");
		//			this.restTemplate
		//				.delete("http://localhost:" + this.port + "/superapp/admin/objects");

	}

	@Test
	public void testCreateSuperAppObjectHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();


		ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		var response = this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix, boundary, ObjectBoundary.class);

		assertEquals(boundary.getActive(),response.getActive());
		assertEquals(boundary.getAlias(),response.getAlias());
		assertEquals(boundary.getCreatedBy(),response.getCreatedBy());
		assertEquals(boundary.getType(),response.getType());
		assertNotEquals(boundary.getObjectId().getSuperapp(),response.getObjectId().getSuperapp());
		assertNotEquals(boundary.getObjectId().getInternalObjectId(),response.getObjectId().getInternalObjectId());
		assertThat(response.getCreationTimestamp().after(boundary.getCreationTimestamp()));

	}

	@Test
	public void testCreateSuperAppObjectCreatedByUserNotExistThrows() throws JsonMappingException, JsonProcessingException
	{			
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
			.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		});

		assertThat(exception.getMessage().contains("\"status\":400"));

	}

	@Test
	public void testGetAllObjectsByTypeForUserSuperApp() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setType("NonType");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);



		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byType/"
				+ boundary.getType()
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertThat(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}
	
	
	@Test
	public void testGetAllObjectsByTypeForMiniAppUser() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setType("NonType");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);

		userBoundary.setRole(UserRole.MINIAPP_USER);
		createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);


		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byType/"
				+ boundary.getType()
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertThat(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}
	
	
	@Test
	public void testGetAllObjectsByExactAliasForUserSuperApp() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setAlias("Nothing");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);



		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byAlias/"
				+ boundary.getAlias()
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertThat(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}
	
	@Test
	public void testGetAllObjectsByExactAliasForMiniAppUser() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setAlias("Nothing");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);

		userBoundary.setRole(UserRole.MINIAPP_USER);
		createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);

		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byAlias/"
				+ boundary.getAlias()
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertFalse(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}

	@Test
	public void testGetAllObjectsByContainingAliasForUserSuperApp() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setAlias("Nothing");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);



		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byAlias/"
				+ boundary.getAlias().substring(2)
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertThat(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}
	
	
	@Test
	public void testGetAllObjectsByContainingAliasForMiniAppUser() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		ObjectBoundary boundary2 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary2.setCreatedBy(createdBy);

		ObjectBoundary boundary3 = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		boundary3.setCreatedBy(createdBy);

		boundary2.setAlias("Nothing");
		boundary3.setActive(false);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary2, ObjectBoundary.class);
		this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary3, ObjectBoundary.class);

		userBoundary.setRole(UserRole.MINIAPP_USER);
		createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);


		var response = this.restTemplate.getForObject(
				this.baseUrl +
				helper.objectPrefix
				+ "search/byAlias/"
				+ boundary.getAlias().substring(2)
				+ "?userSuperapp="
				+ boundary.getCreatedBy().get("userId").getSuperapp()
				+ "&userEmail="
				+ boundary.getCreatedBy().get("userId").getEmail()
				, ObjectBoundary[].class);

		assertNotNull(response);
		assertThat(Arrays.asList(response).contains(boundary));
		assertFalse(Arrays.asList(response).contains(boundary3));
		assertFalse(Arrays.asList(response).contains(boundary2));

	}
	
	@Test
	public void testUpdateSuperAppObject() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);

		var createUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();


		ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		createdBy.put("userId", createUserRes.getUserId());

		boundary.setCreatedBy(createdBy);

		var oldObj = this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix, boundary, ObjectBoundary.class);
		
		boundary.setType("new Type");
		boundary.setAlias("new Alias");
		boundary.setActive(!oldObj.getActive());
		boundary.setCreationTimestamp(oldObj.getCreationTimestamp());
		
		Map<String, UserId> createdBy2 = new HashMap<>();
		UserId userId = new UserId("2023a","newMail@gmail.com");
		createdBy2.put("userId", userId);
		boundary.setCreatedBy(createdBy2);
		Map<String, Object> objectDetails = oldObj.getObjectDetails();
		objectDetails.put("key3", 3);
		boundary.setObjectDetails(objectDetails);
		var objectId =  boundary.getObjectId();
		objectId.setInternalObjectId(objectId.getInternalObjectId() + "11");
		objectId.setSuperapp(objectId.getSuperapp()+"new");
		boundary.setObjectId(objectId);
		
		this.restTemplate.put(this.baseUrl
				+helper.objectPrefix
				+oldObj.getObjectId().getSuperapp()
				+"/"
				+oldObj.getObjectId().getInternalObjectId()
				+"?userSuperapp="
				+oldObj.getCreatedBy().get("userId").getSuperapp()
				+"&userEmail="
				+oldObj.getCreatedBy().get("userId").getEmail()
				, boundary);
		
		var res = this.restTemplate
				.getForObject(this.baseUrl
						+helper.objectPrefix
						+oldObj.getObjectId().getSuperapp()
						+"/"
						+oldObj.getObjectId().getInternalObjectId()
						+"?userSuperapp="
						+oldObj.getCreatedBy().get("userId").getSuperapp()
						+"&userEmail="
						+oldObj.getCreatedBy().get("userId").getEmail(), 
						ObjectBoundary.class);
		
		assertNotNull(res);
		
		assertEquals(oldObj.getObjectId().getInternalObjectId(), res.getObjectId().getInternalObjectId());
		assertEquals(oldObj.getObjectId().getSuperapp(), res.getObjectId().getSuperapp());
		assertNotEquals(oldObj.getType(), res.getType());
		assertNotEquals(oldObj.getAlias(), res.getAlias());
		assertNotEquals(oldObj.getActive(), res.getActive());
		assertEquals(oldObj.getCreatedBy().get("userId").getEmail(), res.getCreatedBy().get("userId").getEmail());
		assertEquals(oldObj.getCreatedBy().get("userId").getSuperapp(), res.getCreatedBy().get("userId").getSuperapp());
		assertThat(oldObj.getCreationTimestamp().equals(res.getCreationTimestamp()));
		
	}


}
