package com.zgcns.leavemanagement.models;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_details")
public class JobDetails {
	
	@EmbeddedId 
	private JobDetailsId id;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne
	@JoinColumn(name="dept_id")
	private Department deptId;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	@Column(name = "reports_to")
	private String reportsTo;
	
	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	public JobDetailsId getId() {
		return id;
	}

	public void setId(JobDetailsId id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Department getDeptId() {
		return deptId;
	}

	public void setDeptId(Department deptId) {
		this.deptId = deptId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public JobDetails(JobDetailsId id, String status, Department deptId, String jobTitle, String reportsTo,
			Date lastUpdateTime) {
		super();
		this.id = id;
		this.status = status;
		this.deptId = deptId;
		this.jobTitle = jobTitle;
		this.reportsTo = reportsTo;
		this.lastUpdateTime = lastUpdateTime;
	}

	public JobDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "JobDetails [id=" + id + ", status=" + status + ", deptId=" + deptId + ", jobTitle=" + jobTitle
				+ ", reportsTo=" + reportsTo + ", lastUpdateTime=" + lastUpdateTime + "]";
	}
	
	
	
	
}
