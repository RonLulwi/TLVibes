package demo;

// {"message":"Hello SuperApp World!"}
public class DemoBoundary {
	private String message;
	private int temp; //This is the change

	private double temp2;
	
	public DemoBoundary() {
	}

	public DemoBoundary(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Demo [message=" + message + "]";
	}
}
