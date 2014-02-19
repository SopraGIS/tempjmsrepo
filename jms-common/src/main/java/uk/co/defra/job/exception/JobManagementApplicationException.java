package uk.co.defra.job.exception;

public class JobManagementApplicationException extends RuntimeException {

	private static final long serialVersionUID = -8183003770733525261L;

	public JobManagementApplicationException() {
		super();
	}

	public JobManagementApplicationException(String message, Throwable exc) {
		super(message, exc);
	}

	public JobManagementApplicationException(String message) {
		super(message);
	}

	public JobManagementApplicationException(Throwable exc) {
		super(exc);
	}
}