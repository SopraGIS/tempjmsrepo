package uk.co.defra.job.exception;

public class ProcessOperationException extends Exception {
    
	private static final long serialVersionUID = 6828489027203431278L;

	public ProcessOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}