package superapp.logic.superAppInvokables;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import superapp.data.MiniAppCommandEntity;
import superapp.data.Weather;
import superapp.logic.services.MissingCommandOnPostRequestException;

@Component(value = "weather.getTlvWeather")
public class WeatherCommand implements ICommandInvokable{
	

	private RestTemplate restTemplate;
	private String commandName = "getTlvWeather";
	private String weatherApiBaseUrl = "http://api.weatherapi.com/v1/forecast.json?key=639005825020473197c185446230301&q=Tel%20Aviv&aqi=no";
	
	
	@Autowired
	public void setRestTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	
	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
		
		if (command.getCommandAttributes().containsKey("days")) {
			this.weatherApiBaseUrl+=("&days="+command.getCommandAttributes().getOrDefault("days", 0));
		}
		ResponseEntity<Weather> response;

	    try {
	    	response = restTemplate.getForEntity(weatherApiBaseUrl,Weather.class);
	    	
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("");
	    }
	    	
	    return response;
		
	}

	
	
		
	
	

	

}
