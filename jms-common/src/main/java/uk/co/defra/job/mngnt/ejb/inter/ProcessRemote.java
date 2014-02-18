/**
 * 
 */
package uk.co.defra.job.mngnt.ejb.inter;

/**
 * @author user
 *
 */
public interface ProcessRemote {
	
	public long startProcess(String recipient) throws Exception;
}
