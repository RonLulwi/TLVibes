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
	private String commandName = "getScooters"; //TODO: Change to final?
	private String limeApiBaseUrl = "https://web-production.lime.bike/api/rider/v1/views/map?ne_lat=32.067441&ne_lng=34.781682&sw_lat=32.06512&sw_lng=34.779361&user_latitude=32.066429&user_longitude=34.780670&zoom=14";
	private ObjectMapper jackson;
	
	
	@Value("${lime.header.key}")
	private String headerKey;
	
	@Value("${lime.header.value}")
	private String headerValue;
	

	/*
	 * private double user_latitude;
	 * private double user_longitude;
	 * private int zoom;
	 */
	
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
		
		ResponseEntity<String> response;

	    try {
	    	var header = new HttpHeaders();
	    	header.set(headerKey, headerValue);
	    	HttpEntity request = new HttpEntity(header);
	    	response = restTemplate.exchange(limeApiBaseUrl, HttpMethod.GET, request, String.class);
	    	
	    	JsonNode root = jackson.readTree(response.getBody());
	    	JsonNode innerNode = root.get("data").get("attributes").get("bikes");
	    	ArrayList<Lime> limes = new ArrayList<>();
	    	Iterator<JsonNode> elements = innerNode.elements();
	    	while(elements.hasNext()) {
	    		JsonNode element = elements.next().get("attributes");
	    		Lime lime = jackson.convertValue(element, Lime.class);
	    		limes.add(lime);
	    		
	    	}	
	    	System.err.println(limes.get(0));
	    	System.err.println(limes.get(1));
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("Something Went wrong");
	    }
	    	
	    return response;
	}

}