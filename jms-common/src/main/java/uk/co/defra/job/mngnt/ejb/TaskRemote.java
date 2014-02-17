/**
 * 
 */
package uk.co.defra.job.mngnt.ejb;

import java.util.List;

import org.kie.api.task.model.TaskSummary;

/**
 * @author user
 *
 */
public interface TaskRemote {
	 public List<TaskSummary> retrieveTaskList(String actorId) throws Exception;
	 public void approveTask(String actorId, long taskId) throws Exception ;
		 
}
