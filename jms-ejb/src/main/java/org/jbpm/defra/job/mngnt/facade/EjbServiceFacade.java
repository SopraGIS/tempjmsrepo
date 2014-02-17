/**
 * 
 */
package org.jbpm.defra.job.mngnt.facade;

import javax.ejb.Local;
import javax.ejb.SessionBean;
import javax.naming.NamingException;

/**
 * @author user
 *
 */
@Local
public interface EjbServiceFacade {
	
	/**
	 * get the bean from jndi with the given name
	 * @param name
	 * @return
	 * @throws NamingException 
	 */
	public Object getServiceBean(String name) throws NamingException;
	
	/**
	 * find the bean by the class
	 * @param clazz
	 * @return
	 */
	public Object getServiceBean(Class clazz) throws NamingException;



}
