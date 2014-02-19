package uk.co.defra.ejb.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Service;

import uk.co.defra.ejb.remote.ProcessRemote;
import uk.co.defra.ejb.remote.TaskRemote;
import uk.co.defra.job.model.JmsTaskSummary;

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
	  
	  public List<JmsTaskSummary> retrieveTaskList(String actorId) throws Exception {
		  return taskBean.retrieveTaskList(actorId);
	  }
	  
	  public void approveTask(String actorId, long taskId) throws Exception {
		  taskBean.approveTask(actorId, taskId);
	  }
}