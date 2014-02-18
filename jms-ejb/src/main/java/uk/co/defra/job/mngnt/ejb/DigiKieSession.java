package uk.co.defra.job.mngnt.ejb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
//import org.jbpm.deployment.util.RewardsApplicationScopedProducer;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.Context;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.defra.job.mngnt.engine.RewardsApplicationScopedProducer;

public class DigiKieSession  implements Serializable{
	
			/**
			 * 
			 */
			private static final long serialVersionUID = 5491005149276800982L;
			
			
	@Inject
	RewardsApplicationScopedProducer rewardsApplicationScopedProducer;
	
	private static Map <Long , KieSession> ksessionMap =  new HashMap<Long, KieSession>();
	
	
	/**
	 * @return the ksessionMap
	 */
	public static Map<Long, KieSession> getKsessionMap() {
		return ksessionMap;
	}


	/**
	 * return the process variables associated with a given process instance.
	 * this is retrieved from the KieSession by invoking a command object passing the
	 * processInstanceId.
	 * 
	 * 
	 * @param processId
	 */
	public Map<String, Object> viewVariables(final Long processId) {
		
	  KieSession ksession = ksessionMap.get(processId);
	 
		//String processId = rewardsApplicationScopedProducer.getJBPMRuntimeManager().getRuntimeEngine(null).getTaskService().get
		
	  // ksession = rewardsApplicationScopedProducer.getJBPMRuntimeManager().getRuntimeEngine(null).getKieSession();
      
	   
	   
		 Map<String, Object> variables = ksession.execute(new GenericCommand<Map<String, Object>>() {
		/**
			 * 
			 */
			private static final long serialVersionUID = 5491005149276800982L;

		public Map<String, Object> execute(Context context) {
		
		      KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
		
		      org.jbpm.process.instance.ProcessInstance processInstance = (org.jbpm.process.instance.ProcessInstance) ksession.getProcessInstance(processId);
		
		      VariableScopeInstance variableScope = (VariableScopeInstance) processInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
		
		      Map<String, Object> variables = variableScope.getVariables();
		
		      return variables;
		
		  }
		
		 });
		 return variables;
	}

}
