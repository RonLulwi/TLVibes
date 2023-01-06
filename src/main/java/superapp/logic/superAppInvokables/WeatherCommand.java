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
	private String apiKey2 = "639005825020473197c185446230301";
	private String commandName = "getTlvWeather";
	private String weatherApiBaseUrl2 = "http://api.weatherapi.com/v1/current.json?key=639005825020473197c185446230301&q=Tel%20Aviv&aqi=no";
	
	
	@Autowired
	public void setRestTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	
	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		

		ResponseEntity<Weather> response;

	    try {
	    	response = restTemplate.getForEntity(weatherApiBaseUrl2,Weather.class);
	    	
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("");
	    }
	    	
	    return response;
		
	}

	
	
		
	
	

	

}
