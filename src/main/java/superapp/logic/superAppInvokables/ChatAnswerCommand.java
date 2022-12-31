package superapp.logic.superAppInvokables;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import superapp.data.ChatAnswer;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.services.MissingCommandOnPostRequestException;


@Component(value ="chatAnswer")
public class ChatAnswerCommand implements ICommandInvokable {
	
	private String commandName = "chatAnswer";
	private String apiKey = "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";
	private RestTemplate restTemplate;
	private String gptApiUrl = "https://api.openai.com/v1/engines/text-davinci-003/completions";

	
	




	@Autowired
	public void setRestTemplate(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}




	@Override
	public Object Invoke(MiniAppCommandEntity command) {
		
		if(!command.getCommand().equals(commandName)){
			throw new RuntimeException("Invalid command name : " + commandName);
		}
		
//		if(!command.getCommandId().getMiniapp().equals("CHAT")){
//			throw new RuntimeException("command : " + commandName + "can be invoke only from MiniApp CHAT");
//		}
		
		
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(this.apiKey);

	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("prompt", command.getCommandAttributes().get("prompt"));
	    requestBody.put("max_tokens", 128);
	    requestBody.put("temperature", 0.5);
	    requestBody.put("top_p", 1.0);
	    ResponseEntity<ChatAnswer> response;

	    try {
		    HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		    response = restTemplate.postForEntity(gptApiUrl, request, ChatAnswer.class);
	    }
	    catch (Exception e) {
	    	throw new MissingCommandOnPostRequestException("");
	    }
	    
	    
		
		return response.getBody().getChoices().get(0).get("text");
		
	}

}
