package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.InvalidParameterException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.assertj.core.util.Arrays;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import superapp.data.Lime;
import superapp.data.MiniAppCommandEntity;
import superapp.data.Tier;
import superapp.data.UserRole;
import superapp.data.interfaces.MiniAppCommandRepository;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommandControllerTests {
	private RestTemplate restTemplate;
	private int port;
	private String url;
	private String baseUrl;
	private ConfigProperties configProperties;
	private ObjectMapper jackson;
	private ControllersTestsHelper helper;
	private String objectPrefix;
	private String commandPrefix;
	private String commandTestStr;
	private String specificCommandUrl;
	private String specificAdminUrl;
	
	@Autowired
	public void setHelper(ControllersTestsHelper helper) {
		this.helper = helper;
	}
	
	@Autowired
	public void setConfigProperties(ConfigProperties configProperties) {
		this.configProperties = configProperties;
	}

	@Autowired
	public void setConfigObjectMapper(ObjectMapper jackson) {
		this.jackson = jackson;
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
		this.specificAdminUrl = this.baseUrl + "/superapp/admin/miniapp";
		this.specificCommandUrl = this.baseUrl + "/superapp/miniapp/";
		this.objectPrefix = "/superapp/objects/";
		this.commandPrefix = "\"command\" :";
		this.commandTestStr = " \"doSomthing\",\r\n";
	}

	@AfterEach
	public void teardown() {		
		helper.TeadDown();
	}
	
	
	@Test
	public void testInvokeCommandForWeatherHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String adminUserAsString = helper.GetAdminUserBoundaryAsJson();
		
		NewUserBoundary adminUserBounday = jackson.readValue(adminUserAsString,NewUserBoundary.class);
		
		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBounday, UserBoundary.class);
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);


		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand("getTlvWeather");
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
				
		var response = this.restTemplate.postForObject(
				this.specificCommandUrl
				+ "weather"
				, commandBoundary, Object.class);
		assertNotNull(response);
		
		var getRes = this.restTemplate.getForObject(
				this.specificAdminUrl
				+ "?userSuperapp="
				+ createAdminUserRes.getUserId().getSuperapp()
				+ "&userEmail="
				+ createAdminUserRes.getUserId().getEmail()
				, MiniAppCommandBoundary[].class);
		
		assertNotNull(Arrays.asList(getRes).contains(response));

		
	}
	
	@Test
	public void testInvokeCommandHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		var response = this.restTemplate
				.postForObject(this.url, commandBoundary, String.class);
					
		assertEquals("Command not found",response);
	}
	
	@Test
	public void testInvokeCommandWithoutCommand() throws JsonMappingException, JsonProcessingException
	{
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson()
				.replace(this.commandPrefix + this.commandTestStr, "");
		
		MiniAppCommandBoundary boundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate.postForObject(this.url, boundary, MiniAppCommandBoundary.class);
		});

		assertThat(exception.getMessage().contains("\"status\":400"));
				
	}
	
	@Test
	public void InvokeObjectTimeTravelCommand_objectCreationDateHasChanged() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		
		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		
		
		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		
		
		
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		Map<String,Object> commandAttributes = new HashMap<>();
				
		commandAttributes.put("creationTimestamp", "1985-10-26T01:22:00.555+00:00");

		commandBoundary.setCommand("objectTimeTravel");
				
		commandBoundary.setCommandAttributes(commandAttributes);

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		var commandResponse  = this.restTemplate
				.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandBoundary, MiniAppCommandBoundary.class);

		var getObjectByIdResponse  = this.restTemplate
				.getForObject(
						this.baseUrl + 
						this.objectPrefix + 
						createObjectResponse.getObjectId().getSuperapp() + "/" +
						createObjectResponse.getObjectId().getInternalObjectId() 
						+ "?userSuperapp="
						+ createObjectResponse.getCreatedBy().get("userId").getSuperapp()
						+ "&userEmail="
						+ createObjectResponse.getCreatedBy().get("userId").getEmail()

						, ObjectBoundary.class);

		
		Date afterInvoking = getObjectByIdResponse.getCreationTimestamp();

		String newTimeStamp =
				"\"" +
				commandBoundary.getCommandAttributes().get("creationTimestamp").toString() +
				"\"";
		

		Date expectedData;
				
		try {
			jackson.registerModule(new JavaTimeModule());

			expectedData = jackson.readValue(newTimeStamp, Date.class);
		} catch (Exception e) {
			throw new InvalidParameterException(newTimeStamp);
		}

		assertEquals(afterInvoking,expectedData);
		assertNotEquals(afterInvoking,beforeInvoking);
	}
	
	@Test
	public void InvokeObjectTimeTravelCommandNotFromTESTMiniAppThrowException() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		Map<String,Object> commandAttributes = new HashMap<>();

		commandAttributes.put("creationTimestamp", "1985-10-26T01:22:00.555+00:00");

		commandBoundary.setCommand("objectTimeTravel");
		
		commandBoundary.setCommandAttributes(commandAttributes);

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		var response = this.restTemplate
				.postForObject(this.url, commandBoundary, String.class);
					
			assertEquals("Command not found",response);


	}

	@Test
	public void InvokeEchoCommand_returnCommandEntity() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		commandBoundary.setCommand("echo");
		
		var commandResponse  = this.restTemplate
				.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandBoundary, MiniAppCommandEntity.class);


		assertEquals(commandBoundary.getCommand(),commandResponse.getCommand());
		assertEquals(commandBoundary.getCommandAttributes(),commandResponse.getCommandAttributes());
		assertEquals(commandBoundary.getInvokedBy(),commandResponse.getInvokedBy());
		assertEquals(commandBoundary.getTargetObject(),commandResponse.getTargetObject());
		assertNotEquals(commandBoundary.getCommandId(),commandResponse.getCommand());

	}
	
	@Test
	public void InvokeEchoCommand20TimesGetAllCommandOfAllMiniAppsHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String adminAppUserAsString  = helper.GetAdminUserBoundaryAsJson();

		NewUserBoundary adminAppUserBoundary = jackson.readValue(adminAppUserAsString,NewUserBoundary.class);

		var createAdminAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
				
		commandBoundary.setCommand("echo");
		
		List<String> expected = new ArrayList<String>();
		
		IntStream.range(0, 20).forEach(i ->{
			
			Map<String,Object> commandAttributes = new HashMap<>();
			
			commandAttributes.put("echo", "Message " + i);

			expected.add("Message " + i);
			
			commandBoundary.setCommandAttributes(commandAttributes);

			this.restTemplate
			.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandBoundary, String.class);
		});
		
		var response = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/miniapp/getAllCommands" +
					"?size=20&userSuperapp=" + configProperties.getSuperAppName() +
					"&userEmail=" + adminAppUserBoundary.getEmail(),
					MiniAppCommandBoundary[].class);


		assertEquals(20,response.length);
		
		IntStream.range(0, 20).forEach(i ->{
		    assertTrue(expected.contains(response[i].getCommandAttributes().get("echo")));
		});
	}
	
	@Test
	public void InvokeEchoCommand20TimesGetAllCommandOfSpecificAppHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String adminAppUserAsString  = helper.GetAdminUserBoundaryAsJson();

		NewUserBoundary adminAppUserBoundary = jackson.readValue(adminAppUserAsString,NewUserBoundary.class);

		var createAdminAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
				
		commandBoundary.setCommand("echo");
		
		List<String> expected = new ArrayList<String>();
		
		IntStream.range(0, 20).forEach(i ->{
			
			Map<String,Object> commandAttributes = new HashMap<>();
			
			commandAttributes.put("echo", "Message " + i);

			expected.add("Message " + i);
			
			commandBoundary.setCommandAttributes(commandAttributes);

			this.restTemplate
			.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandBoundary, String.class);
		});
		
		var response = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/miniapp/getAllCommandsOf/" +
					"TEST" +
					"?size=20&userSuperapp=" + configProperties.getSuperAppName() +
					"&userEmail=" + adminAppUserBoundary.getEmail(),
					MiniAppCommandBoundary[].class);


		assertEquals(20,response.length);
		
		IntStream.range(0, 20).forEach(i ->{
		    assertTrue(expected.contains(response[i].getCommandAttributes().get("echo")));
		});
	}

	@Test
	public void InvokeEchoCommandNotFromTESTMiniAppInvokeDefaultCommand() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		Map<String,Object> commandAttributes = new HashMap<>();
		
		commandAttributes.put("echo", Instant.now());
		
		commandBoundary.setCommandAttributes(commandAttributes);
		
		commandBoundary.setCommand("echo");

		var response = this.restTemplate
			.postForObject(this.url, commandBoundary, String.class);
				
		assertEquals("Command not found",response);

	}
	
	@Test
	public void InvokeUnknownCommandInvokingDefaultCommandInstand() throws JsonMappingException, JsonProcessingException
	{
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);

		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		commandBoundary.setCommand(UUID.randomUUID().toString());	
				
		var response = this.restTemplate
				.postForObject(this.url, commandBoundary, String.class);
					
		assertEquals("Command not found",response);
		
		String adminUserAsString  = helper.GetAdminUserBoundaryAsJson();

		NewUserBoundary adminUserBoundary = jackson.readValue(adminUserAsString,NewUserBoundary.class);

		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBoundary, UserBoundary.class);	
		
		var getAllCommandsResponse = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/miniapp/getAllCommands/" +
					"?size=20&userSuperapp=" + configProperties.getSuperAppName() +
					"&userEmail=" +createAdminUserRes.getUserId().getEmail(),
					MiniAppCommandBoundary[].class);


		assertEquals(1,getAllCommandsResponse.length);		
	}
	
	@Test 
	public void testDateAsStringToDateObjectConvert()
	{
		String dateAsString = "1985-10-26T01:22:00.000+0000";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Date date = null;
		
		try {
			date = sdf.parse(dateAsString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(date);
	}
	
	
	
	@Test
	public void testInvokeCommandForLimeScooterHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String adminUserAsString = helper.GetAdminUserBoundaryAsJson();
		
		NewUserBoundary adminUserBounday = jackson.readValue(adminUserAsString,NewUserBoundary.class);
		
		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBounday, UserBoundary.class);
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);


		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand("getScooters");
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		var response = this.restTemplate.postForObject(
				this.specificCommandUrl
				+ "lime"
				, commandBoundary, Object.class);
		assertNotNull(response);

	}
	
	
	@Test
	public void testInvokeCommandForTierScooterHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String adminUserAsString = helper.GetAdminUserBoundaryAsJson();
		
		NewUserBoundary adminUserBounday = jackson.readValue(adminUserAsString,NewUserBoundary.class);
		
		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBounday, UserBoundary.class);
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);


		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand("getScooters");
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		var response = this.restTemplate.postForObject(
				this.specificCommandUrl
				+ "tier"
				, commandBoundary, Object.class);
		assertNotNull(response);
		

	}
	
	
	
	
	@Test
	public void testInvokeCommandForTierScooterWhitGivenParamsHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String adminUserAsString = helper.GetAdminUserBoundaryAsJson();
		
		NewUserBoundary adminUserBounday = jackson.readValue(adminUserAsString,NewUserBoundary.class);
		
		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBounday, UserBoundary.class);
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);


		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand("getScooters");
		
		
		Map<String, Object> commandAttributes = new HashMap<>();
		commandAttributes.put("lat", 32.106171);
		commandAttributes.put("lng", 34.815309);
		commandAttributes.put("radius", 20000);
		commandBoundary.setCommandAttributes(commandAttributes);
		
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		
		var response = this.restTemplate.postForObject(
				this.specificCommandUrl
				+ "tier"
				, commandBoundary, Object.class);
		assertNotNull(response);
	}
	
	@Test
	public void testInvokeCommandForLimeScooterWhitGivenParamsHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		
		String adminUserAsString = helper.GetAdminUserBoundaryAsJson();
		
		NewUserBoundary adminUserBounday = jackson.readValue(adminUserAsString,NewUserBoundary.class);
		
		var createAdminUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, adminUserBounday, UserBoundary.class);
		
		String miniAppUserAsString  = helper.GetMiniAppUserBoundaryAsJson();

		NewUserBoundary miniappUserBoundary = jackson.readValue(miniAppUserAsString,NewUserBoundary.class);

		var createMiniAppUserRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, miniappUserBoundary, UserBoundary.class);	

		String superAppUserAsString  = helper.GetSuperAppUserBoundaryAsJson();

		NewUserBoundary superAppUserBoundary = jackson.readValue(superAppUserAsString,NewUserBoundary.class);

		var createSuperAppRes = this.restTemplate
				.postForObject(this.baseUrl + helper.userPrefix, superAppUserBoundary, UserBoundary.class);	

		String objectBoundaryAsString  = helper.GetBaseObjectBoundaryAsJson();

		ObjectBoundary objectBoundary = jackson.readValue(objectBoundaryAsString,ObjectBoundary.class);
		

		Map<String, UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createSuperAppRes.getUserId());

		objectBoundary.setCreatedBy(createdBy);


		var createObjectResponse = this.restTemplate.postForObject(
				this.baseUrl + helper.objectPrefix , objectBoundary, ObjectBoundary.class);

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandBoundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandBoundary.setCommand("getScooters");
		
		Map<String, Object> commandAttributes = new HashMap<>();
		commandAttributes.put("user_latitude", 32.106171);
		commandAttributes.put("user_longitude", 34.815309);
		commandBoundary.setCommandAttributes(commandAttributes);
		
		
		Map<String, UserId> invokedBy = new HashMap<>();
		
		invokedBy.put("userId", createMiniAppUserRes.getUserId());

		commandBoundary.setInvokedBy(invokedBy);
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("objectId", createObjectResponse.getObjectId());

		commandBoundary.setTargetObject(targetObject);
		System.err.println(commandBoundary);
		var response = this.restTemplate.postForObject(
				this.specificCommandUrl
				+ "lime"
				, commandBoundary, Object.class);
		assertNotNull(response);
	}

}
