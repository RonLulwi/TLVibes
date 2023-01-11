package superapp.data;

public class Tier {
	
	private double lat;
	private double lng;
	private int batteryLevel;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	@Override
	public String toString() {
		return "Tier [lat=" + lat + ", lng=" + lng + ", batteryLevel=" + batteryLevel + "]";
	}
	
	
	
	
	

}
