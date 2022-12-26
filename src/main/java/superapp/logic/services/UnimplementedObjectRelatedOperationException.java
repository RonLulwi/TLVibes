package superapp.logic.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnimplementedObjectRelatedOperationException extends Exception {
	
	
		private static final long serialVersionUID = -879670889273545867L;

		public UnimplementedObjectRelatedOperationException() {
		}

		public UnimplementedObjectRelatedOperationException(String message) {
			super(message);
		}

		public UnimplementedObjectRelatedOperationException(Throwable cause) {
			super(cause);
		}

		public UnimplementedObjectRelatedOperationException(String message, Throwable cause) {
			super(message, cause);
		}

}