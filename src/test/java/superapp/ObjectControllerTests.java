package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import superapp.logic.boundaries.NewUserBoundary;
import superapp.logic.boundaries.ObjectBoundary;
import superapp.logic.boundaries.UserBoundary;
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
			this.restTemplate
				.delete("http://localhost:" + this.port + "/superapp/admin/users");
			this.restTemplate
				.delete("http://localhost:" + this.port + "/superapp/admin/objects");

		}
		
		@Test
		public void testCreateSuperAppObjectHappyFlow() throws JsonMappingException, JsonProcessingException
		{
			String userBoundaryAsString  = helper.GetBaseUserBoundaryAsJson();
			
			NewUserBoundary userBoundary = jackson.readValue(userBoundaryAsString,NewUserBoundary.class);
			
			this.restTemplate
					.postForObject(this.baseUrl + helper.userPrefix, userBoundary, UserBoundary.class);	

			String objectboundaryAsString  = helper.GetBaseObjectBoundaryAsJson();
			
			ObjectBoundary boundary = jackson.readValue(objectboundaryAsString,ObjectBoundary.class);
			
			var response = this.restTemplate.postForObject(this.baseUrl + helper.objectPrefix , boundary, ObjectBoundary.class);
			
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
				


}
