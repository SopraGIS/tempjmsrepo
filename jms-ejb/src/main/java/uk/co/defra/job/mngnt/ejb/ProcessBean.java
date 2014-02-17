/*
 * This file is part of websocktets-gl - simple WebSocket example
 * Copyright (C) 2012  Burt Parkers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.defra.job.mngnt.ejb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;






//import org.jbpm.deployment.util.RewardsApplicationScopedProducer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.defra.job.mngnt.engine.JBPNMapType;
import uk.co.defra.job.mngnt.factories.JobManagementRuntimeFactory;
import uk.co.defra.job.mngnt.factories.JobManagementRuntimeFactoryImpl;

@Stateless
@Remote(ProcessRemote.class)
@Local(ProcessLocal.class)
public class ProcessBean  implements ProcessRemote 
,ProcessLocal,Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4802654641812350406L;
	static Logger logger = LoggerFactory.getLogger(ProcessBean.class.getName());
	/** The EntityManager. */
    @PersistenceContext(unitName = "com.sample.rewards")
    private EntityManager entityManager;
    
    
    @PostConstruct
    public void configure() {
    	
    		
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long startProcess(String recipient) throws Exception {
	logger.info("starting a process now ");
	JobManagementRuntimeFactory jobManagementRuntimeFactory = JobManagementRuntimeFactoryImpl.getManagerInstance();
	RuntimeManager singletonManager = jobManagementRuntimeFactory.getJBPMRuntimeManager(JBPNMapType.REWARDS);
    RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
                .get());
    
        KieSession ksession = runtime.getKieSession();

        long processInstanceId = -1;
 
        // start a new process instance
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recipient", recipient);
        ProcessInstance processInstance = ksession.startProcess(
                "com.sample.rewards-basic", params);

        processInstanceId = processInstance.getId();

       logger.info("Process started ... : processInstanceId = "
                + processInstanceId);

        DigiKieSession.getKsessionMap().put(processInstanceId, ksession);
        
        return processInstanceId;
	
    }
}
