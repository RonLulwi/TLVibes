package superapp.logic.superAppInvokables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import superapp.data.Lime;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.services.MissingCommandOnPostRequestException;

@Component(value = "lime.getScooters")
public class LimeCommand implements ICommandInvokable {

	private RestTemplate restTemplate;
	private String commandName = "getScooters";
	private String limeApiBaseUrl = "https://web-production.lime.bike/api/rider/v1/views/map?";
	private ObjectMapper jackson;
	
	
	
	@Value("${lime.header.key}")
	private String headerKey;
	
	@Value("${lime.header.value}")
	private String headerValue;
	
	private double user_latitude = 32.066429;
	private double user_longitude = 34.780670;
	 
	
	@Autowired
	public void setRestTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	@Autowired
	public void setJackson(ObjectMapper jackson) {
		this.jackson = jackson;
	}

	
	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		buildUrl(command);
		ResponseEntity<String> response;
		ArrayList<Lime> limes = null;
	    try {
	    	var header = new HttpHeaders();
	    	header.set(headerKey, headerValue);
	    	HttpEntity request = new HttpEntity(header);
	    	response = restTemplate.exchange(limeApiBaseUrl, HttpMethod.GET, request, String.class);
	    	
	    	JsonNode root = jackson.readTree(response.getBody());
	    	JsonNode innerNode = root.get("data").get("attributes").get("bikes");
	    	
	    	limes = new ArrayList<>();
	    	
	    	Iterator<JsonNode> elements = innerNode.elements();
	    	int count =0;
	    	while(elements.hasNext() && count <20) {
	    		JsonNode element = elements.next().get("attributes");
	    		Lime lime = jackson.convertValue(element, Lime.class);
	    		limes.add(lime);
	    		count++;
	    	}	
	    	
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("Something Went wrong");
	    }
	    finishRequest();
	    return limes;
	}
	
	
	
	private void finishRequest() {
		this.limeApiBaseUrl = "https://web-production.lime.bike/api/rider/v1/views/map?";
		this.user_latitude = 32.066429;
		this.user_longitude = 34.780670;
		
	}

	private void buildUrl(MiniAppCommandEntity command) {	
		if (command.getCommandAttributes().containsKey("user_latitude") && command.getCommandAttributes().get("user_latitude") != null) {
			this.user_latitude = (double) command.getCommandAttributes().get("user_latitude");
		}
		if(command.getCommandAttributes().containsKey("user_longitude") && command.getCommandAttributes().get("user_longitude") != null) {
			this.user_longitude = (double) command.getCommandAttributes().get("user_longitude");
		}
		
		double ne_lat = this.user_latitude + 0.001012;
		double ne_lng = this.user_longitude + 0.001012;
		double sw_lat = this.user_latitude - 0.001309;
		double sw_lng = this.user_longitude - 0.001309;
		
		limeApiBaseUrl += "ne_lat="+ ne_lat +"&ne_lng="+ ne_lng +"&sw_lat="+ sw_lat +"&sw_lng="+ sw_lng +"&user_latitude="+ this.user_latitude +"&user_longitude="+ this.user_longitude +"&zoom=14";
	}

}
