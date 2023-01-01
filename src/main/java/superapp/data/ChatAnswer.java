package superapp.data;

import java.util.List;
import java.util.Map;

public class ChatAnswer {
	
	private String id;
	private String object;
	private int created;
	private String model;
	private List<Map<String,Object>> choices;
	private Map<String,Object> usage;
	
	
	public void setId(String id) {
		this.id = id;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setChoices(List<Map<String,Object>> choices) {
		this.choices = choices;
	}
	public void setUsage(Map<String, Object> usage) {
		this.usage = usage;
	}
	public String getId() {
		return id;
	}
	public String getObject() {
		return object;
	}
	public int getCreated() {
		return created;
	}
	public String getModel() {
		return model;
	}
	public List<Map<String,Object>> getChoices() {
		return choices;
	}
	public Map<String, Object> getUsage() {
		return usage;
	}
	
	
	
	
	
//	 "id": "cmpl-6TTnZHsPIsHhvPvYRHowtDygNkNOq",
//	    "object": "text_completion",
//	    "created": 1672484077,
//	    "model": "text-davinci-003",
//	    "choices": [
//	        {
//	            "text": "\n\nShalom!",
//	            "index": 0,
//	            "logprobs": null,
//	            "finish_reason": "stop"
//	        }
//	    ],
//	    "usage": {
//	        "prompt_tokens": 9,
//	        "completion_tokens": 6,
//	        "total_tokens": 15
//	    }

}
