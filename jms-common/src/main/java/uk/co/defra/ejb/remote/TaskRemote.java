package uk.co.defra.ejb.remote;

import java.util.List;

import javax.ejb.Remote;

import uk.co.defra.job.model.JmsTaskSummary;

@Remote
public interface TaskRemote {

	List<JmsTaskSummary> retrieveTaskList(String actorId) throws Exception;

	void approveTask(String actorId, long taskId) throws Exception;
}
