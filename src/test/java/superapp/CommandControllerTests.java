package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommandControllerTests {
	private RestTemplate restTemplate;
	private int port;
	private String url;
	private ConfigProperties configProperties;
	private ObjectMapper jackson;

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
		this.url = "http://localhost:" + this.port + "/superapp/miniapp/" + configProperties.getSuperAppName();
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
		String commandboundaryAsString  = GetBaseCommandBoundartAsJson();
		
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

	private String GetBaseCommandBoundartAsJson() {
		return "{\r\n"
				+ "    \"commandId\" : {\r\n"
				+ "        \"superapp\" : \"2023a.Assaf.Ariely\",\r\n"
				+ "        \"miniapp\" : \"dummyApp\",\r\n"
				+ "        \"internalCommandId\" : \"490edcaf-08db-4eac-a1e3-2cbf7932c412\"\r\n"
				+ "    },\r\n"
				+ "    \"command\" : \"doSomthing\",\r\n"
				+ "    \"targetObject\" : {\r\n"
				+ "        \"objectId\" : {\r\n"
				+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
				+ "            \"internalObjectId\" : \"8a7257dd-b534-4747-ae3c-9f551f93eeaa\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"invocationTimeStamp\" : \"2022-11-26T15:15:18.479+00:00\",\r\n"
				+ "    \"invokedBy\" : {\r\n"
				+ "        \"userId\" : {\r\n"
				+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
				+ "            \"email\" : \"niv@demo.org\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"commandAttributes\" : {\r\n"
				+ "        \"key1\" : {\r\n"
				+ "            \"key2subkey1\" : \"can be anything you wish, even a nested json\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}\r\n";
	}
}
