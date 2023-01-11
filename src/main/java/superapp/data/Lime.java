package superapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lime {
	
	private Map<String,Object> data;
	//private Map<String,Object> attributes;
	private ArrayList<Map<String,Object>> bikes; //TODO: Maybe change to Object
	private Map<String,Object> attributes;
	private int index = 0;
	

	public ArrayList<Map<String, Object>> getBikes() {
		return bikes;
	}

	public void setBikes(ArrayList<Map<String, Object>> bikes) {
		this.bikes = bikes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void initVars() {
		this.bikes = 
				(ArrayList<Map<String, Object>>) 
				(((Map<String, Object>) this.data.get("attributes"))
				.get("bikes"));
		
		this.index = 0;
		this.attributes = new HashMap<>();
		for(var element: this.bikes) {
			attributes.put(index++ + "", element);
		}
	}

	@Override
	public String toString() {
		return "Lime [bikes=" + bikes + "]";
	}
	
	
	
	
	

}
