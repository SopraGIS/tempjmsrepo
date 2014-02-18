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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.defra.job.mngnt.ejb.inter.TaskRemote;
import uk.co.defra.job.mngnt.engine.RewardsApplicationScopedProducer;
//import org.jbpm.deployment.util.RewardsApplicationScopedProducer;
//import org.kie.api.cdi.KSession;

@Stateless
public class TaskBean implements TaskRemote {

	Logger logger = LoggerFactory.getLogger(TaskBean.class.getName());
	 
	    
    private TaskService taskService;
    
    @Inject
    private RewardsApplicationScopedProducer applicationScopedProducer;  
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<TaskSummary> retrieveTaskList(String actorId) throws Exception {
    	taskService = applicationScopedProducer.getJBPMRuntimeManager()
    			.getRuntimeEngine(EmptyContext.get()).getTaskService();

        
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
