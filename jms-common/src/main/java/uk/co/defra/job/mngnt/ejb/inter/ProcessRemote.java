/**
 * 
 */
package uk.co.defra.job.mngnt.ejb.inter;

import javax.ejb.Remote;

/**
 * @author user
 *
 */
@Remote
public interface ProcessRemote {
	
	public long startProcess(String recipient) throws Exception;
}
