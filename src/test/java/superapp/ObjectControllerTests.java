package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.infrastructure.ConfigProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ObjectControllerTests {
		private int port;
		private RestTemplate restTemplate;
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
			this.url = "http://localhost:" + this.port + "/superapp/objects/";
		}
		
		@AfterEach
		public void teardown() {
			this.restTemplate
				.delete("http://localhost:" + this.port + "/superapp/admin/users");
			this.restTemplate
				.delete("http://localhost:" + this.port + "/superapp/admin/objects");

		}
		
		@Test
		public void testCreateSuperAppObjectHappyFlow() throws JsonMappingException, JsonProcessingException
		{
			String objectboundaryAsString  = GetBaseObjectBoundartAsJson();
			
			ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);
			
			var response = this.restTemplate.postForObject(this.url, boundary, ObjectBoundary.class);
			
			assertEquals(boundary.getActive(),response.getActive());
			assertEquals(boundary.getAlias(),response.getAlias());
			assertEquals(boundary.getCreatedBy(),response.getCreatedBy());
			assertEquals(boundary.getType(),response.getType());
			assertNotEquals(boundary.getObjectId().getSuperapp(),response.getObjectId().getSuperapp());
			assertNotEquals(boundary.getObjectId().getInternalObjectId(),response.getObjectId().getInternalObjectId());
			assertThat(response.getCreationTimestamp().after(boundary.getCreationTimestamp()));

		}
		
		private String GetBaseObjectBoundartAsJson() {
			return "{\r\n"
					+ "    \"objectId\": {\r\n"
					+ "        \"superapp\" : \"nivsminiapp\",\r\n"
					+ "        \"internalObjectId\": \"1000\"\r\n"
					+ "    },\r\n"
					+ "    \"type\": \"dummyType\",\r\n"
					+ "    \"alias\": \"niv's demo instance\",\r\n"
					+ "    \"active\": true,\r\n"
					+ "    \"creationTimestamp\": \"2022-11-26T15:15:18.479+00:00\",\r\n"
					+ "    \"objectDetails\": {\r\n"
					+ "        \"key1\" : \"11\",\r\n"
					+ "        \"key2\" : \"12\"\r\n"
					+ "    },\r\n"
					+ "    \"createdBy\": {\r\n"
					+ "        \"userId\" : {\r\n"
					+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
					+ "            \"email\": \"niv@demo.org\"\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "}";
		}
		


}
