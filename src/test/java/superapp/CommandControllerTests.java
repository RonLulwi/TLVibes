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

import superapp.data.MiniAppCommandEntity;
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
	private String userPrefix;
	private ConfigProperties configProperties;
	private ObjectMapper jackson;
	private ControllersTestsHelper helper;
	private String objectPrefix;
	private String commandPrefix;
	private String commandTestStr;
	
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
		this.userPrefix = "/superapp/users/";
		this.objectPrefix = "/superapp/objects/";
		this.commandPrefix = "\"command\" :";
		this.commandTestStr = " \"doSomthing\",\r\n";
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + this.port + "/superapp/admin/users");
		this.restTemplate.delete("http://localhost:" + this.port + "/superapp/admin/objects");
		this.restTemplate.delete("http://localhost:" + this.port + "/superapp/admin/miniapp");

	}
	
	
	
	@Test
	public void testInvokeCommandHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary boundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		var response = this.restTemplate.postForObject(this.url, boundary, String.class);

		assertNotNull(response);
	}
	
	@Test
	public void testInvokeCommandWithoutCommand() throws JsonMappingException, JsonProcessingException
	{
		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson()
				.replace(this.commandPrefix + this.commandTestStr, "");
		
		MiniAppCommandBoundary boundary = jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		MiniAppCommandBoundary response = null;
		try {
			response = this.restTemplate.postForObject(this.url, boundary, MiniAppCommandBoundary.class);
		}
		catch(Exception e)
		{
			
		}
		assertNull(response);
		
	}
	
	@Test
	public void InvokeObjectTimeTravelCommand_objectCreationDateHasChanged() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
				
		commandAttributes.put("creationTimestamp", "1985-10-26T01:22:00.000+0000");

		commandboundary.setCommand("objectTimeTravel");
		
		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		var commandResponse  = this.restTemplate
				.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandboundary, MiniAppCommandBoundary.class);

		var getObjectByIdResponse  = this.restTemplate
				.getForObject(
						this.baseUrl + 
						this.objectPrefix + 
						createObjectResponse.getObjectId().getSuperapp() + "/" +
						createObjectResponse.getObjectId().getInternalObjectId()
						, ObjectBoundary.class);

		
		Date afterInvoking = getObjectByIdResponse.getCreationTimestamp();

		String newTimeStamp = commandboundary.getCommandAttributes().get("creationTimestamp").toString();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Date expectedData;
		
		try {
			expectedData = sdf.parse(newTimeStamp);
		} catch (ParseException e) {
			throw new InvalidParameterException(newTimeStamp);
		}
		
		assertEquals(afterInvoking,expectedData);
		assertNotEquals(afterInvoking,beforeInvoking);
	}
	
	@Test
	public void InvokeObjectTimeTravelCommandNotFromTESTMiniAppThrowException() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
				
		commandAttributes.put("creationTimestamp", "1985-10-26T01:22:00.000+0000");

		commandboundary.setCommand("objectTimeTravel");
		
		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
			.postForObject(this.url, commandboundary, String.class);
			});

		assertTrue(exception.getMessage().contains("can be invoke only from MiniApp TEST"));


	}

	@Test
	public void InvokeEchoCommand_returnCommandEntity() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
		
		commandAttributes.put("echo", Instant.now());

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);
		
		commandboundary.setCommand("echo");

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		var commandResponse  = this.restTemplate
				.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandboundary, String.class);

		var getObjectByIdResponse  = this.restTemplate
				.getForObject(
						this.baseUrl + 
						this.objectPrefix + 
						createObjectResponse.getObjectId().getSuperapp() + "/" +
						createObjectResponse.getObjectId().getInternalObjectId()
						, ObjectBoundary.class);

		
		Date afterInvoking = getObjectByIdResponse.getCreationTimestamp();

		assertThat(afterInvoking.before(beforeInvoking));
	}
	
	@Test
	public void InvokeEchoCommand20TimesGetAllCommandOfAllMiniAppsHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
				
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommand("echo");
		
		List<String> expected = new ArrayList<String>();
		
		IntStream.range(0, 20).forEach(i ->{
			
			Map<String,Object> commandAttributes = new HashMap<>();
			
			commandAttributes.put("echo", "Message " + i);

			expected.add("Message " + i);
			
			commandboundary.setCommandAttributes(commandAttributes);

			this.restTemplate
			.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandboundary, String.class);
		});
		
		var response = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/miniapp/getAllCommands" +
					"?size=20&userSuperapp=" + configProperties.getSuperAppName() +
					"&userEmail=" + userBoundary.getEmail(),
					MiniAppCommandBoundary[].class);


		assertEquals(20,response.length);
		
		IntStream.range(0, 20).forEach(i ->{
		    assertTrue(expected.contains(response[i].getCommandAttributes().get("echo")));
		});
	}
	
	@Test
	public void InvokeEchoCommand20TimesGetAllCommandOfSpecificAppHappyFlow() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
				
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommand("echo");
		
		List<String> expected = new ArrayList<String>();
		
		IntStream.range(0, 20).forEach(i ->{
			
			Map<String,Object> commandAttributes = new HashMap<>();
			
			commandAttributes.put("echo", "Message " + i);

			expected.add("Message " + i);
			
			commandboundary.setCommandAttributes(commandAttributes);

			this.restTemplate
			.postForObject(this.baseUrl + "/superapp/miniapp/TEST/", commandboundary, String.class);
		});
		
		var response = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/miniapp/getAllCommandsOf/" +
					"TEST" +
					"?size=20&userSuperapp=" + configProperties.getSuperAppName() +
					"&userEmail=" + userBoundary.getEmail(),
					MiniAppCommandBoundary[].class);


		assertEquals(20,response.length);
		
		IntStream.range(0, 20).forEach(i ->{
		    assertTrue(expected.contains(response[i].getCommandAttributes().get("echo")));
		});
	}

	@Test
	public void InvokeEchoCommandNotFromTESTMiniAppThrowException() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
		
		commandAttributes.put("echo", Instant.now());

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);
		
		commandboundary.setCommand("echo");

		Exception exception = assertThrows(Exception.class, () -> {
			this.restTemplate
			.postForObject(this.url, commandboundary, String.class);
			});

		assertTrue(exception.getMessage().contains("can be invoke only from MiniApp TEST"));

	}
	
	@Test
	public void InvokeUnknownCommandInvokingDefaultCommandInstand() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("userId", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		commandboundary.setCommand(UUID.randomUUID().toString());
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
		
		commandAttributes.put("echo", Instant.now().minus(2,ChronoUnit.DAYS));

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);
		
		var commandResponse  = this.restTemplate
				.postForObject(this.url, commandboundary, String.class);

		assertEquals(commandResponse.toString(), "Command not found");

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
	

}
