package uk.co.defra.ejb.remote;

import javax.ejb.Remote;

@Remote
public interface ProcessRemote {
	
	long startProcess(String recipient) throws Exception;
}
