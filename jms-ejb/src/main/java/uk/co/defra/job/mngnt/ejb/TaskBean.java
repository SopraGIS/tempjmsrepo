/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.defra.job.mngnt.ejb;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;










//import org.jbpm.deployment.util.RewardsApplicationScopedProducer;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.Context;
import org.kie.api.runtime.manager.RuntimeEngine;
//import org.kie.api.cdi.KSession;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.defra.job.mngnt.ejb.inter.TaskLocal;
import uk.co.defra.job.mngnt.ejb.inter.TaskRemote;
import uk.co.defra.job.mngnt.engine.JBPNMapType;
import uk.co.defra.job.mngnt.engine.RewardsApplicationScopedProducer;
import uk.co.defra.job.mngnt.factories.JobManagementRuntimeFactory;
import uk.co.defra.job.mngnt.factories.JobManagementRuntimeFactoryImpl;

@Stateless
@Remote(TaskRemote.class)
@Local(TaskLocal.class)
public class TaskBean implements TaskRemote, TaskLocal {

	Logger logger = LoggerFactory.getLogger(TaskBean.class.getName());
	 
	JobManagementRuntimeFactory jobManagementRuntimeFactory;  
	    
    private TaskService taskService;
    
    
    
    @PostConstruct
    private void configure(){
    	jobManagementRuntimeFactory = JobManagementRuntimeFactoryImpl.getManagerInstance();
    	taskService = jobManagementRuntimeFactory.getJBPMRuntimeManager(JBPNMapType.REWARDS)
    						.getRuntimeEngine(EmptyContext.get()).getTaskService();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<TaskSummary> retrieveTaskList(String actorId) throws Exception {

        
        List<TaskSummary> list = null;
        
            list = taskService.getTasksAssignedAsPotentialOwner(actorId, "en-UK");

        logger.info("retrieveTaskList by " + actorId);
        for (TaskSummary task : list) {
            logger.info(" task.gfetId() = " + task.getId());
        }
        
        

        return list;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void approveTask(String actorId, long taskId) throws Exception {
 
        try {
            logger.info("approveTask (taskId = " + taskId + ") by " + actorId);
            taskService.start(taskId, actorId);
            taskService.complete(taskId, actorId, null);

            //Thread.sleep(10000); // To test OptimisticLockException

         
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
            // Transaction might be already rolled back by TaskServiceSession
            
            // Probably the task has already been started by other users
            throw new RuntimeException("The task (id = " + taskId
                    + ") has likely been started by other users ", e);
        } catch (Exception e) {
            e.printStackTrace();
            // Transaction might be already rolled back by TaskServiceSession

            throw new RuntimeException(e);
        } 
    }

}
