package uk.co.defra.job.mngnt.ejb.inter;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.kie.api.task.model.TaskSummary;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Service;

@Service
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class JmsProcessFacade {


	  @EJB
	  private ProcessRemote processBean;
	  
	  @EJB
	  private TaskRemote taskBean;
	  
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
