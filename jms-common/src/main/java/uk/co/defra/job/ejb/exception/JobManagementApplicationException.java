package uk.co.defra.job.ejb.exception;

public class JobManagementApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8183003770733525261L;

	public JobManagementApplicationException() {
		super();
	}

	public JobManagementApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JobManagementApplicationException(String arg0) {
		super(arg0);
	}

	public JobManagementApplicationException(Throwable arg0) {
		super(arg0);
	}

}
