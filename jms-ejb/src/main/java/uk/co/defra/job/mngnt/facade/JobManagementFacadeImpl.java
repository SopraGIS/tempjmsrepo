package uk.co.defra.job.mngnt.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.defra.job.engine.DigiKieSession;
import uk.co.defra.job.engine.RewardsApplicationScopedProducer;
import uk.co.defra.job.model.JmsTaskSummary;

@Stateless
public class JobManagementFacadeImpl implements JobManagementFacade {
	private static Logger logger = LoggerFactory.getLogger(JobManagementFacadeImpl.class.getName());

	
	public JobManagementFacadeImpl() {
	}

	@Inject
	private RewardsApplicationScopedProducer applicationScopedProducer;

	private TaskService taskService;

	public long startProcess(String recipient) throws Exception {
		logger.info("starting a process now ");
		RuntimeEngine runtime = applicationScopedProducer.getJBPMRuntimeManager().getRuntimeEngine(EmptyContext.get());

		KieSession ksession = runtime.getKieSession();

		long processInstanceId = -1;

		// start a new process instance
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("recipient", recipient);
		ProcessInstance processInstance = ksession.startProcess("com.sample.rewards-basic", params);

		processInstanceId = processInstance.getId();

		logger.info("Process started ... : processInstanceId = " + processInstanceId);

		DigiKieSession.getKsessionMap().put(processInstanceId, ksession);

		return processInstanceId;
	}

	public List<JmsTaskSummary> retrieveTaskList(String actorId)
			throws Exception {
		taskService = applicationScopedProducer.getJBPMRuntimeManager().getRuntimeEngine(EmptyContext.get()).getTaskService();

		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner(actorId, "en-UK");

		logger.info("retrieveTaskList by " + actorId);
		for (TaskSummary task : list) {
			logger.info(" task.gfetId() = " + task.getId());
		}
		
		return getJmsTaskSummary(list);

	}
	
private List<JmsTaskSummary> getJmsTaskSummary(List<TaskSummary> list) {
		
		List<JmsTaskSummary> jmsTaskSummary = new ArrayList<JmsTaskSummary>();
		
		for(TaskSummary summary : list) {
			JmsTaskSummary taskSummary = new JmsTaskSummary();
			taskSummary.setName(summary.getName());
			taskSummary.setId(summary.getId());
			taskSummary.setProcessInstanceId(summary.getProcessInstanceId());

			jmsTaskSummary.add(taskSummary);
		}
		
		return jmsTaskSummary;
	}

public void approveTask(String actorId, long taskId) throws Exception {
	try {
		logger.info("approveTask (taskId = " + taskId + ") by " + actorId);
		taskService.start(taskId, actorId);
		taskService.complete(taskId, actorId, null);

		// Thread.sleep(10000); // To test OptimisticLockException

	} catch (PermissionDeniedException e) {
		e.printStackTrace();
		// Transaction might be already rolled back by TaskServiceSession

		// Probably the task has already been started by other users
		throw new RuntimeException("The task (id = " + taskId + ") has likely been started by other users ", e);
	} catch (Exception e) {
		e.printStackTrace();
		// Transaction might be already rolled back by TaskServiceSession

		throw new RuntimeException(e);
	}
	
}


}
