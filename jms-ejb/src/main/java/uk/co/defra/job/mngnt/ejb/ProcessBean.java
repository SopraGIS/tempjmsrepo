package uk.co.defra.job.mngnt.ejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import uk.co.defra.ejb.remote.ProcessRemote;
import uk.co.defra.job.mngnt.facade.JobManagementFacade;

@Stateless
public class ProcessBean implements ProcessRemote {


	// The EntityManager
	@PersistenceContext(unitName = "com.sample.rewards")
	private EntityManager entityManager;

	@Inject
	private JobManagementFacade jobManagementFacade;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long startProcess(String recipient) throws Exception {
		
		return jobManagementFacade.startProcess(recipient);
		
		}
}