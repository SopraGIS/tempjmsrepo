/**
 * 
 */
package uk.co.defra.job.mngnt.ejb;

/**
 * @author user
 *
 */
public interface ProcessRemote {
	
	public long startProcess(String recipient) throws Exception;
}
