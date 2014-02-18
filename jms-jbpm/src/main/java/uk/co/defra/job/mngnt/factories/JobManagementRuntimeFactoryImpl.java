package uk.co.defra.job.mngnt.factories;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.api.runtime.manager.RuntimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import uk.co.defra.job.mngnt.engine.JBPNMapType;
import uk.co.defra.job.mngnt.engine.RewardsApplicationScopedProducer;

@ApplicationScoped
public class JobManagementRuntimeFactoryImpl implements JobManagementRuntimeFactory {

	//private static RewardsApplicationScopedProducer applicationScopedProducer ;
	
	private static JobManagementRuntimeFactory jobManagementRuntimeFactory = new JobManagementRuntimeFactoryImpl();
	
	static Logger logger = LoggerFactory.getLogger(JobManagementRuntimeFactoryImpl.class.getName());
	   
	public JobManagementRuntimeFactoryImpl() {
		super();
	}
		
	public synchronized RuntimeManager getJBPMRuntimeManager(JBPNMapType processType) {
		
		RewardsApplicationScopedProducer applicationScopedProducer = null;
		logger.info("&&&&&&&&&&&&&&&&  : " + processType);
		if(processType.equals(JBPNMapType.REWARDS)) {
			if(applicationScopedProducer == null) {
				applicationScopedProducer = new RewardsApplicationScopedProducer();
				logger.info("&&&&&&&&&&&&&&&&  rewards created "  );
			}
			
			return applicationScopedProducer.getJBPMRuntimeManager();
			
		}
		return null;
	}

	public static JobManagementRuntimeFactory getManagerInstance() {
		
		return jobManagementRuntimeFactory;
	}

}
