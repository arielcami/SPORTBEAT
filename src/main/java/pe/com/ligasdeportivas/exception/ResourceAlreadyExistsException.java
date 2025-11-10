package pe.com.ligasdeportivas.exception;

@SuppressWarnings("serial")
public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
