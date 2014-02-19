package uk.co.defra.job.model;

import java.io.Serializable;

public class JmsTaskSummary implements Serializable {

	private String name;
	private long id;
	private long ProcessInstanceId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProcessInstanceId() {
		return ProcessInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		ProcessInstanceId = processInstanceId;
	}
}