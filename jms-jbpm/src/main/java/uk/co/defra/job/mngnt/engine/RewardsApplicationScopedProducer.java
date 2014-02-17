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

package uk.co.defra.job.mngnt.engine;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewardsApplicationScopedProducer {

//    @Inject
//    private InjectableRegisterableItemsFactory factory;
//   
//    
	static Logger logger = LoggerFactory.getLogger(RewardsApplicationScopedProducer.class.getName());	
	public RewardsApplicationScopedProducer() {
		init();
	}
   
   private RuntimeManager runtimeManager;
   
   @PersistenceUnit(unitName = "com.sample.rewards")
    private EntityManagerFactory emf;

   
    public void init() {
    	logger.info("public void produceEntityManagerFactory() called");
        if (this.emf == null) {
            this.emf = Persistence
                    .createEntityManagerFactory("com.sample.rewards");
        }
        
        logger.info("&&&&&&&&&&&   Post Contruct called ");
        
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder
                .getDefault()
                .entityManagerFactory(emf)
                .addAsset(
                        ResourceFactory
                                .newClassPathResource("rewards-basic.bpmn"),
                        ResourceType.BPMN2).get();
        
        if(environment == null) {
        	logger.info("&&&&&&&&&&&  env null");
        }
        logger.info("&&&&&&&&&&&   creating the env ");
        
       this.runtimeManager= RuntimeManagerFactory.Factory.get()
		.newSingletonRuntimeManager(environment, "com.sample:example:1.0");
        
       logger.info("&&&&&&&&&&&   Post Contruct done ");
    }

	public RuntimeManager getJBPMRuntimeManager() {
		return this.runtimeManager;
	}


    

}
