package com.zgcns.leavemanagement.models;


import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empl_details")
public class ReportingManager {

	@Id
	private String reportsto;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(name = "reports_to", length = 10)

	public String getReportsTo() {
		return reportsto;
	}

	public void setReportsTo(String reportsto) {
		this.reportsto = reportsto;
	}

	
	@Override
	public String toString() {
		return "ReportingManager [reportsto=" + this.reportsto + "]";
	}
}