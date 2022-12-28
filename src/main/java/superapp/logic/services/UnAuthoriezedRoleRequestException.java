package superapp.logic.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnAuthoriezedRoleRequestException extends RuntimeException {


	private static final long serialVersionUID = 4492063746557069240L;

	public UnAuthoriezedRoleRequestException() {
	}

	public UnAuthoriezedRoleRequestException(String message) {
		super(message);
	}

	public UnAuthoriezedRoleRequestException(Throwable cause) {
		super(cause);
	}

	public UnAuthoriezedRoleRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
