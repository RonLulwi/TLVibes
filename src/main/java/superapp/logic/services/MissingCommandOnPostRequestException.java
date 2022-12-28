package superapp.logic.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MissingCommandOnPostRequestException extends RuntimeException{

	
	private static final long serialVersionUID = 5413731958694526837L;

	public MissingCommandOnPostRequestException() {
	}

	public MissingCommandOnPostRequestException(String message) {
		super(message);
	}

	public MissingCommandOnPostRequestException(Throwable cause) {
		super(cause);
	}

	public MissingCommandOnPostRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
