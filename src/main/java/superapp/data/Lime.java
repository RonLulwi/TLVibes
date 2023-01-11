package superapp.data;

public class Lime {
	
	private double longitude;
	private double latitude;
	private int battery_percentage;
	
	public Lime() {}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getBattery_percentage() {
		return battery_percentage;
	}

	public void setBattery_percentage(int battery_percentage) {
		this.battery_percentage = battery_percentage;
	}

	@Override
	public String toString() {
		return "Lime [longitude=" + longitude + ", latitude=" + latitude + ", battery_percentage=" + battery_percentage
				+ "]";
	}
	
	

	
	
	
	
	
	

}
