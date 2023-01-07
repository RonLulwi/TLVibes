package superapp.data;

import java.util.Map;


public class Weather {
	
	private Map<String,Object> location;
	private Map<Object,Object> current;
	private Map<Object,Object> forecast;
	
	
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
	public Map<Object,Object> getForecast() {
		return forecast;
	}
	public void setForecast(Map<Object,Object> forecast) {
		this.forecast = forecast;
	}
	@Override
	public String toString() {
		return "Weather [location=" + location + ", current=" + current + ", forecast=" + forecast + "]";
	}
	

	
	
	

}
