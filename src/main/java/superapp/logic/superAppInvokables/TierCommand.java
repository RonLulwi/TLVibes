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
import superapp.data.Tier;
import superapp.logic.services.MissingCommandOnPostRequestException;

@Component(value = "tier.getScooters")
public class TierCommand implements ICommandInvokable {

	private RestTemplate restTemplate;
	private String commandName = "getScooters"; //TODO: Change to final?
	private String tierApiBaseUrl = "https://platform.tier-services.io/v1/vehicle?lat=32.085300&lng=34.781769&radius=200";
	private ObjectMapper jackson;
	
	@Value("${tier.header.key}")
	private String headerKey;
	
	@Value("${tier.header.value}")
	private String headerValue;
	
	
	/*
	 * private double user_latitude;
	 * private double user_longitude;
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
	    	response = restTemplate.exchange(tierApiBaseUrl, HttpMethod.GET, request, String.class);
	    	
	    	JsonNode root = jackson.readTree(response.getBody());
	    	JsonNode innerNode = root.get("data");
	    	ArrayList<Tier> limes = new ArrayList<>();
	    	Iterator<JsonNode> elements = innerNode.elements();
	    	while(elements.hasNext()) {
	    		JsonNode element = elements.next().get("attributes");
	    		Tier lime = jackson.convertValue(element, Tier.class);
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
