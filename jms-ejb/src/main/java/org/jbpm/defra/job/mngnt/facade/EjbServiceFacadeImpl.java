/**
 * 
 */
package org.jbpm.defra.job.mngnt.facade;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author user
 *
 */
@Stateless
public class EjbServiceFacadeImpl implements  SessionBean, EjbServiceFacade {
	
	static Logger logger = LoggerFactory.getLogger( EjbServiceFacadeImpl.class.getName());
	SessionContext ctx;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1105331403617524269L;

	/* (non-Javadoc)
	 * @see uk.co.defra.jbpm.ejb.facade.EjbServiceFacade#getServiceBean(java.lang.String)
	 */
	public Object getServiceBean(String name) throws NamingException {
		logger.info("&&&&&&&&&&&&&&&&&&&  returning bean jndi name : "+name + " $$$$$$$$$$$$$$$");
		
		InitialContext remoteContext = null;
		Properties env = new Properties();

	//	env.setProperty( "java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");

		env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//		env.setProperty( "java.naming.provider.url", "remote://localhost:4447");
//		env.put(Context.SECURITY_PRINCIPAL, "admin");
		env.put("jboss.naming.client.ejb.context", true);
//		env.put(Context.SECURITY_CREDENTIALS, "Allahu01*");
				
//		final Hashtable<String,String> jndiProperties = new Hashtable<String,String>();
//	    jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	    
//		final Properties env = new Properties();
//		env.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
//		env.put(Context.PROVIDER_URL, "remote://localhost:4447");
		try {
			logger.info("&&&&&&&&&&&&&&  Creating remote initial contex &&&&&&&&&&&&&&&&");
			remoteContext =  new InitialContext(env);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		Object obj = remoteContext.lookup(name);
		logger.info("&&&&&&&&&&&&&&  object type returned : "+obj.getClass().getName());
		
		return remoteContext.lookup(name);
	}

//	/* (non-Javadoc)
//	 * @see uk.co.defra.jbpm.ejb.facade.EjbServiceFacade#getServiceBean(java.lang.Class)
//	 */
//	@Override
//	public Object getSetrviceBean(Class clazz) {
//		StringBuilder buff = new StringBuilder(clazz.getName());
//		// TODO convention for registering name in jndi??
//		
//		return null;
//	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext sessionContext) throws EJBException,
			RemoteException {
		
		this.ctx = sessionContext;
		
	}

	/* (non-Javadoc)
	 * @see uk.co.defra.jbpm.ejb.facade.EjbServiceFacade#getServiceBean(java.lang.Class)
	 */
	public Object getServiceBean(Class clazz) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@PostConstruct
//	public void setRemoteContext() {
//		Properties env = new Properties();
//
//	//	env.setProperty( "java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
//
//		env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
////		env.setProperty( "java.naming.provider.url", "remote://localhost:4447");
////		env.put(Context.SECURITY_PRINCIPAL, "admin");
////		env.put("jboss.naming.client.ejb.context", true);
////		env.put(Context.SECURITY_CREDENTIALS, "Allahu01*");
//				
////		final Hashtable<String,String> jndiProperties = new Hashtable<String,String>();
////	    jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//	    
////		final Properties env = new Properties();
////		env.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
////		env.put(Context.PROVIDER_URL, "remote://localhost:4447");
//		try {
//			logger.info("&&&&&&&&&&&&&&  Creating remote initial contex &&&&&&&&&&&&&&&&");
//			remoteContext =  new InitialContext(env);
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
	
}
