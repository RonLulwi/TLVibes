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
	private String tierApiBaseUrl = "https://platform.tier-services.io/v1/vehicle?";
	
	
	private ObjectMapper jackson;
	
	private double lat = 32.085300;
	private double lng=34.781769;
	private int radius=200;
	
	@Value("${tier.header.key}")
	private String headerKey;
	
	@Value("${tier.header.value}")
	private String headerValue;
	
	
	
	
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
		ArrayList<Tier> tiers = null;
	    try {
	    	var header = new HttpHeaders();
	    	header.set(headerKey, headerValue);
	    	HttpEntity request = new HttpEntity(header);
	    	response = restTemplate.exchange(tierApiBaseUrl, HttpMethod.GET, request, String.class);
	    	
	    	JsonNode root = jackson.readTree(response.getBody());
	    	JsonNode innerNode = root.get("data");
	    	tiers = new ArrayList<>();
	    	
	    	Iterator<JsonNode> elements = innerNode.elements();
	    	int count=0;
	    	while(elements.hasNext() && count<20) {
	    		JsonNode element = elements.next().get("attributes");
	    		Tier tier = jackson.convertValue(element, Tier.class);
	    		tiers.add(tier);
	    		count++;
	    		
	    	}	

	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("Something Went wrong");
	    }
	    finishRequest();
	    return tiers;
	}
	
	private void finishRequest() {
		this.tierApiBaseUrl = "https://platform.tier-services.io/v1/vehicle?";
		this.lat = 32.085300;
		this.lng=34.781769;
		this.radius=200;
		
	}

	private void buildUrl(MiniAppCommandEntity command) {	
		if (command.getCommandAttributes().containsKey("lat") && command.getCommandAttributes().get("lat") != null) {
			this.lat = (double) command.getCommandAttributes().get("lat");
		}
		if(command.getCommandAttributes().containsKey("lng") && command.getCommandAttributes().get("lng") != null) {
			this.lng = (double) command.getCommandAttributes().get("lng");
		}
		if(command.getCommandAttributes().containsKey("radius") && command.getCommandAttributes().get("radius") != null) {
			this.radius = (int) command.getCommandAttributes().get("radius");
		}

		tierApiBaseUrl += "lat="+ this.lat +"&lng="+ this.lng +"&radius=" + this.radius;		
	}

}
