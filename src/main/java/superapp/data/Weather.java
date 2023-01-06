package superapp.data;

import java.util.Map;


public class Weather {
	
	private Map<String,Object> location;
	private Map<Object,Object> current;
	
	
	public Map<String, Object> getLocation() {
		return location;
	}
	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}
	public Map<Object, Object> getCurrent() {
		return current;
	}
	public void setCurrent(Map<Object, Object> current) {
		this.current = current;
	}
	

	
	
	

}
