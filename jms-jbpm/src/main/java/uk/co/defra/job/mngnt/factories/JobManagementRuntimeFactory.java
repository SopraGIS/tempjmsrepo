package uk.co.defra.job.mngnt.factories;

import org.kie.api.runtime.manager.RuntimeManager;

import uk.co.defra.job.mngnt.engine.JBPNMapType;

public interface JobManagementRuntimeFactory {
	public RuntimeManager getJBPMRuntimeManager(JBPNMapType processType);
}
