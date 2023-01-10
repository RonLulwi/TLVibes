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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserRole;
import superapp.data.enums.CreationEnum;
import superapp.data.identifiers.ObjectId;
import superapp.logic.ObjectsService;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.SuperAppMapToJsonConverter;

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
		this.url = this.baseUrl + "/superapp/miniapp/" + configProperties.getSuperAppName();
		this.userPrefix = "/superapp/users/";


	}

	@AfterEach
	public void teardown() {
		helper.TeadDown();

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
	public void testCreateSuperAppObjectWithNullType() throws JsonMappingException, JsonProcessingException
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

		boundary.setActive(null);
		
		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate.
				postForObject(this.baseUrl + helper.objectPrefix, boundary, ObjectBoundary.class);		});

		assertThat(exception.getMessage().contains("\"status\":400"));

	}
	
	@Test
	public void testCreateSuperAppObjectWithNullAlias() throws JsonMappingException, JsonProcessingException
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

		boundary.setAlias(null);
		
		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate.
				postForObject(this.baseUrl + helper.objectPrefix, boundary, ObjectBoundary.class);		});

		assertThat(exception.getMessage().contains("\"status\":400"));

	}

	@Test
	public void testCreateSuperAppObjectWithEmptyStringAlias() throws JsonMappingException, JsonProcessingException
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

		boundary.setAlias("");
		
		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate.
				postForObject(this.baseUrl + helper.objectPrefix, boundary, ObjectBoundary.class);		});

		assertThat(exception.getMessage().contains("\"status\":400"));

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
	
	@Test
	public void testGetAllObjectThatCreatedInTheLastMinuts() throws JsonMappingException, JsonProcessingException
	{
		var createMiniAppUserRes = addMiniAppUserToDatabase();	
		
		var createSuperAppRes = addSuperAppUserToDatabase();	
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

		IntStream.range(0, 20).forEach(i ->{
			
			ObjectBoundary createObjectResponse;
			try {
				createObjectResponse = addSuperAppUserByUserToDatabase(createSuperAppRes);
			
				MiniAppCommandBoundary commandBoundary = createCommandOnTargetByUser(createMiniAppUserRes,
					createObjectResponse);
			
				if(i % 4 == 0) {
					
					Map<String,Object> commandAttributes = new HashMap<>();
					

					var date = 	ZonedDateTime.now()
									.minus(30,ChronoUnit.SECONDS)
									.format( DateTimeFormatter.ISO_OFFSET_DATE_TIME);
					
					commandAttributes.put("creationTimestamp", date);
	
					commandBoundary.setCommand("objectTimeTravel");
							
					commandBoundary.setCommandAttributes(commandAttributes);
	
					var getObjectByIdResponse = this.restTemplate
							.getForObject(
									this.baseUrl + 
									helper.objectPrefix + 
									createObjectResponse.getObjectId().getSuperapp() + "/" +
									createObjectResponse.getObjectId().getInternalObjectId() 
									+ "?userSuperapp="
									+ createObjectResponse.getCreatedBy().get("userId").getSuperapp()
									+ "&userEmail="
									+ createObjectResponse.getCreatedBy().get("userId").getEmail()
									, ObjectBoundary.class);
					
					this.restTemplate
					.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandBoundary, String.class);
				}
			}
			catch(Exception e)
			{}
		});
		
		var allObjectsFromLastMinuts = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/objects/search/byCreation/" +
						CreationEnum.LAST_MINUTE.toString() 
				+ "?userSuperapp="
				+ createSuperAppRes.getUserId().getSuperapp()
				+ "&userEmail="
				+ createSuperAppRes.getUserId().getEmail()
				, ObjectBoundary[].class);

		assertEquals(5, allObjectsFromLastMinuts.length);
	}

	private MiniAppCommandBoundary createCommandOnTargetByUser(UserBoundary createMiniAppUserRes,
			ObjectBoundary createObjectResponse) throws JsonProcessingException, JsonMappingException {
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		return commandBoundary;
	}

	private ObjectBoundary addSuperAppUserByUserToDatabase(UserBoundary createSuperAppRes)
			throws JsonProcessingException, JsonMappingException {
		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);
		return createObjectResponse;
	}

	private UserBoundary addSuperAppUserToDatabase() throws JsonProcessingException, JsonMappingException {
		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);
		return createSuperAppRes;
	}

	private UserBoundary addMiniAppUserToDatabase() throws JsonProcessingException, JsonMappingException {
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);
		return createMiniAppUserRes;
	}
	
	@Test
	public void testConvertObjectBoundaryToEntity() throws JsonMappingException, JsonProcessingException{
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		ObjectId objectId = new ObjectId(
				configProperties.getSuperAppName(),
				UUID.randomUUID().toString());
		
		SuperAppObjectEntity entity = objectConvertor.toEntity(boundary, objectId);
		
		assertNotEquals(entity.getObjectId().getInternalObjectId(),boundary.getObjectId().getInternalObjectId());
		assertNotEquals(entity.getObjectId().getSuperapp(),boundary.getObjectId().getSuperapp());
		assertEquals(entity.getActive(),boundary.getActive());
		assertEquals(entity.getAlias(), boundary.getAlias());
		assertEquals(entity.getType(), boundary.getType());
		assertEquals(entity.getCreatedBy(),boundary.getCreatedBy());
		assertEquals(entity.getCreationTimestamp(),boundary.getCreationTimestamp());
		assertEquals(entity.getObjectDetails(), boundary.getObjectDetails());
	}
	
	@Test
	public void testConvertObjectEntityToboundary() throws JsonMappingException, JsonProcessingException{
		
		SuperAppObjectEntity entity = new SuperAppObjectEntity();
		
		ObjectId objectId = new ObjectId(
				configProperties.getSuperAppName(),
				UUID.randomUUID().toString());

		entity.setObjectId(objectId);
		entity.setActive(true);
		entity.setAlias("testEntity");
		entity.setCreationTimestamp(Date.from(Instant.now()));
		entity.setType("testObject");
		
		Map<String, UserId> createdBy = new HashMap<>();
		UserId userId = new UserId("test","test@gmail.com");
		createdBy.put("userId", userId);

		entity.setCreatedBy(createdBy);
		
		Map<String, Object> objectDetails = new HashMap<>();
		objectDetails.put("testDetails","checkConvertionFromEntityToBoundary");
		entity.setObjectDetails(objectDetails);
		
		ObjectBoundary boundary = objectConvertor.toBoundary(entity);
		
		assertEquals(entity.getObjectId().getInternalObjectId(),boundary.getObjectId().getInternalObjectId());
		assertEquals(entity.getObjectId().getSuperapp(),boundary.getObjectId().getSuperapp());
		assertEquals(entity.getActive(),boundary.getActive());
		assertEquals(entity.getAlias(), boundary.getAlias());
		assertEquals(entity.getType(), boundary.getType());
		assertEquals(entity.getCreatedBy(),boundary.getCreatedBy());
		assertEquals(entity.getCreationTimestamp(),boundary.getCreationTimestamp());
		assertEquals(entity.getObjectDetails(), boundary.getObjectDetails());
	}
	@Test
	public void testBindTwoObjectsAndGetChildObjectHisParentObjectHappyFlow() throws JsonMappingException, JsonProcessingException {
	
		// create superAppUser
		UserBoundary superAppUser = addSuperAppUserToDatabase();
		
		// create two objects father and child
		ObjectBoundary father = addSuperAppUserByUserToDatabase(superAppUser);
		ObjectBoundary child = addSuperAppUserByUserToDatabase(superAppUser);
		
		// extract objectId from child
		SuperAppObjectIdBoundary childObjectId = child.getObjectId();
		
		// bindChild URL  
		String myURL = this.baseUrl + helper.objectPrefix + father.getObjectId().getSuperapp() + "/" + father.getObjectId().getInternalObjectId() + "/children"
		+"?userSuperapp="
				+father.getCreatedBy().get("userId").getSuperapp()
				+"&userEmail="
				+father.getCreatedBy().get("userId").getEmail() ;
		
		// bind father and child
		this.restTemplate.put(myURL, childObjectId);

		// getParents URL
		String myURL2 = this.baseUrl + helper.objectPrefix + child.getObjectId().getSuperapp() + "/" + child.getObjectId().getInternalObjectId() + "/parents"
				+"?userSuperapp="
						+superAppUser.getUserId().getSuperapp()
						+"&userEmail="
						+superAppUser.getUserId().getEmail() ;
		// get parent from DB
		ObjectBoundary[] childParentFromDB = this.restTemplate.getForObject(myURL2, ObjectBoundary[].class);
		
		
		// check if father is equals to childParentFromDB
		assertEquals(childParentFromDB[0].getObjectId(),father.getObjectId());
		assertEquals(childParentFromDB[0].getActive(),father.getActive());
		assertEquals(childParentFromDB[0].getAlias(), father.getAlias());
		assertEquals(childParentFromDB[0].getType(), father.getType());
		assertEquals(childParentFromDB[0].getCreatedBy(),father.getCreatedBy());
		assertEquals(childParentFromDB[0].getCreationTimestamp(),father.getCreationTimestamp());
		assertEquals(childParentFromDB[0].getObjectDetails(), father.getObjectDetails());

	}
	
	@Test
	public void testBindTwoObjectsAndGetParentObjectHisChildObjectHappyFlow() throws JsonMappingException, JsonProcessingException {
	
		// create superAppUser
		UserBoundary superAppUser = addSuperAppUserToDatabase();
		
		// create two objects father and child
		ObjectBoundary father = addSuperAppUserByUserToDatabase(superAppUser);
		ObjectBoundary child = addSuperAppUserByUserToDatabase(superAppUser);
		
		// extract objectId from child
		SuperAppObjectIdBoundary childObjectId = child.getObjectId();
		
		// bindChild URL  
		String myURL = this.baseUrl + helper.objectPrefix + father.getObjectId().getSuperapp() + "/" + father.getObjectId().getInternalObjectId() + "/children"
		+"?userSuperapp="
				+father.getCreatedBy().get("userId").getSuperapp()
				+"&userEmail="
				+father.getCreatedBy().get("userId").getEmail() ;
		
		// bind father and child
		this.restTemplate.put(myURL, childObjectId);

		// getParents URL
		String myURL2 = this.baseUrl + helper.objectPrefix + father.getObjectId().getSuperapp() + "/" + father.getObjectId().getInternalObjectId() + "/children"
				+"?userSuperapp="
						+superAppUser.getUserId().getSuperapp()
						+"&userEmail="
						+superAppUser.getUserId().getEmail() ;
		// get child from DB
		ObjectBoundary[] fatherChildFromDB = this.restTemplate.getForObject(myURL2, ObjectBoundary[].class);
		
		
		// check if father is equals to fatherChildFromDB
		assertEquals(fatherChildFromDB[0].getObjectId(),child.getObjectId());
		assertEquals(fatherChildFromDB[0].getActive(),child.getActive());
		assertEquals(fatherChildFromDB[0].getAlias(), child.getAlias());
		assertEquals(fatherChildFromDB[0].getType(), child.getType());
		assertEquals(fatherChildFromDB[0].getCreatedBy(),child.getCreatedBy());
		assertEquals(fatherChildFromDB[0].getCreationTimestamp(),child.getCreationTimestamp());
		assertEquals(fatherChildFromDB[0].getObjectDetails(), child.getObjectDetails());

	}
	
	
	

}
