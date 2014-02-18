package uk.co.defra.job.mngnt.ejb.inter;

import java.util.List;

import javax.ejb.EJB;

import org.kie.api.task.model.TaskSummary;

public class JmsProcessFacade {


	  @EJB(mappedName="java:module/ProcessBean")  	
	  private ProcessLocal processBean;
	  
	  @EJB(mappedName="java:module/TaskBean")
	  private TaskLocal taskBean;
	  
	  public long startProcess(String recipient) throws Exception {
		  
		  return processBean.startProcess(recipient);
	  }
	  
	  public List<TaskSummary> retrieveTaskList(String actorId) throws Exception{
		  return taskBean.retrieveTaskList(actorId);
	  }
	  
	  public void approveTask(String actorId, long taskId) throws Exception {
		  taskBean.approveTask(actorId, taskId);
	  }
}
