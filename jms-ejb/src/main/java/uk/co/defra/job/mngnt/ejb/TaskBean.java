package uk.co.defra.job.mngnt.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import uk.co.defra.ejb.remote.TaskRemote;
import uk.co.defra.job.mngnt.facade.JobManagementFacade;
import uk.co.defra.job.model.JmsTaskSummary;

@Stateless
public class TaskBean implements TaskRemote {

	@Inject
	private JobManagementFacade jobManagementFacade;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JmsTaskSummary> retrieveTaskList(String actorId) throws Exception {
		
		return jobManagementFacade.retrieveTaskList(actorId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void approveTask(String actorId, long taskId) throws Exception {
		jobManagementFacade.approveTask(actorId, taskId);
	}
}