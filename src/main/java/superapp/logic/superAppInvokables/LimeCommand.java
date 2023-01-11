package superapp.logic.superAppInvokables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import superapp.data.Lime;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.services.MissingCommandOnPostRequestException;

@Component(value = "lime.getScooters")
public class LimeCommand implements ICommandInvokable {

	private RestTemplate restTemplate;
	private String commandName = "getScooters"; //TODO: Change to final?
	private String limeApiBaseUrl = "https://web-production.lime.bike/api/rider/v1/views/map?ne_lat=32.067441&ne_lng=34.781682&sw_lat=32.06512&sw_lng=34.779361&user_latitude=32.066429&user_longitude=34.780670&zoom=14";
	/*
	 * private double user_latitude;
	 * private double user_longitude;
	 * private int zoom;
	 */
	
	@Autowired
	public void setRestTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	
	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		
		//TODO: Change Lon + Lat
		/*
		 * if (command.getCommandAttributes().containsKey("days")) {
			this.weatherApiBaseUrl+=("&days="+command.getCommandAttributes().getOrDefault("days", 0));
		}
		 */
		
		ResponseEntity<Lime> response;

	    try {
	    	response = restTemplate.getForEntity(limeApiBaseUrl,Lime.class);
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("Something Went wrong");
	    }
	    	
	    return response;
	}

}
