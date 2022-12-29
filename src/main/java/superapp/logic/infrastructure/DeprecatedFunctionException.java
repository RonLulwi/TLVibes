package superapp.logic.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DeprecatedFunctionException extends RuntimeException{


	private static final long serialVersionUID = 4492063746557069240L;

	public DeprecatedFunctionException() {
	}

	public DeprecatedFunctionException(String message) {
		super(message);
	}

	public DeprecatedFunctionException(Throwable cause) {
		super(cause);
	}

	public DeprecatedFunctionException(String message, Throwable cause) {
		super(message, cause);
	}
}
