package uk.co.defra.job.mngnt.facade;

import java.util.List;

import uk.co.defra.job.model.JmsTaskSummary;

public interface JobManagementFacade {

	long startProcess(String recipient) throws Exception;
	public List<JmsTaskSummary> retrieveTaskList(String actorId) throws Exception ;
	public void approveTask(String actorId, long taskId) throws Exception ;


}
