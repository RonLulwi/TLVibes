package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.InvalidParameterException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
		
		var response = this.restTemplate.postForObject(this.url, boundary, MiniAppCommandBoundary.class);

		assertNotNull(response);
		assertEquals(boundary.getCommand(),response.getCommand());
		assertEquals(boundary.getInvokedBy(),response.getInvokedBy());
		assertEquals(boundary.getTargetObject(),response.getTargetObject());
		assertEquals(boundary.getCommandAttributes(),response.getCommandAttributes());
		assertEquals(boundary.getCommandAttributes(),response.getCommandAttributes());
		assertEquals(boundary.getCommandId().getMiniapp(),response.getCommandId().getMiniapp());
		assertEquals(boundary.getCommandId().getSuperapp(),response.getCommandId().getSuperapp());
		assertNotEquals(boundary.getCommandId().getInternalCommandId(),response.getCommandId().getInternalCommandId());
		assertThat(response.getInvocationTimestamp().after(boundary.getInvocationTimestamp()));

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
		
		createdBy.put("createdBy", createUserResponse.getUserId());
		
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
				.postForObject(this.url, commandboundary, MiniAppCommandBoundary.class);

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
	public void InvokeEchoCommand_returnCommandEntity() throws JsonMappingException, JsonProcessingException
	{
		String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
		
		NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
		
		var createUserResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.userPrefix, userBoundary, UserBoundary.class);	
		
		String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
		
		ObjectBoundary objectboundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);

		Map<String,UserId> createdBy = new HashMap<>();
		
		createdBy.put("createdBy", createUserResponse.getUserId());
		
		objectboundary.setCreatedBy(createdBy);
		
		var createObjectResponse  = this.restTemplate
				.postForObject(this.baseUrl + this.objectPrefix, objectboundary, ObjectBoundary.class);	

		String commandboundaryAsString  = helper.GetBaseCommandBoundaryAsJson();
		
		MiniAppCommandBoundary commandboundary = 
				jackson.readValue(commandboundaryAsString,MiniAppCommandBoundary.class);
		
		
		Map<String,SuperAppObjectIdBoundary> targetObject = new HashMap<>();
		
		targetObject.put("targetObject", createObjectResponse.getObjectId());
		
		Map<String,Object> commandAttributes = new HashMap<>();
		
		commandAttributes.put("echo", Instant.now().minus(2,ChronoUnit.DAYS));

		commandboundary.setTargetObject(targetObject);
		
		commandboundary.setInvokedBy(createdBy);
		
		commandboundary.setCommandAttributes(commandAttributes);

		Date beforeInvoking = createObjectResponse.getCreationTimestamp();
		
		var commandResponse  = this.restTemplate
				.postForObject(this.url, commandboundary, MiniAppCommandBoundary.class);

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
