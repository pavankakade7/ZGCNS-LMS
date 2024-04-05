package com.zgcns.leavemanagement.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;



import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;



@Entity
@Table(name = "empl_details")
public class EmployeeDetails {

	@Id
	@Column(name = "emplid")
	private String emplId;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "buss_email")
	private String businessEmail;
	
	@Column(name = "date_of_hiring")
	private Date dateOfHiring;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	@ManyToOne
	@JoinColumn(name = "reports_to")
	private PersonDetails reportsTo;    // manager
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "last_login")
	private Date lastLogin;
	

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "emplid"), inverseJoinColumns = @JoinColumn(name = "role"))
	private Set<Roles> roles = new HashSet<Roles>();


	public String getEmplId() {
		return emplId;
	}


	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getBusinessEmail() {
		return businessEmail;
	}


	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}


	public Date getDateOfHiring() {
		return dateOfHiring;
	}


	public void setDateOfHiring(Date dateOfHiring) {
		this.dateOfHiring = dateOfHiring;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getJobTitle() {
		return jobTitle;
	}


	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}


	public PersonDetails getReportsTo() {
		return reportsTo;
	}


	public void setReportsTo(PersonDetails reportsTo) {
		this.reportsTo = reportsTo;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Date getLastLogin() {
		return lastLogin;
	}


	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}


	public Set<Roles> getRoles() {
		return roles;
	}


	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	
	

	public EmployeeDetails() {
		super();
		// TODO Auto-generated constructor stub
	}


	public EmployeeDetails(String emplId, String type, String name, String businessEmail, Date dateOfHiring,
			String department, String jobTitle, PersonDetails reportsTo, String location, Date lastLogin,
			Set<Roles> roles) {
		super();
		this.emplId = emplId;
		this.type = type;
		this.name = name;
		this.businessEmail = businessEmail;
		this.dateOfHiring = dateOfHiring;
		this.department = department;
		this.jobTitle = jobTitle;
		this.reportsTo = reportsTo;
		this.location = location;
		this.lastLogin = lastLogin;
		this.roles = roles;
	}


	@Override
	public String toString() {
		return "EmployeeDetails [emplId=" + emplId + ", type=" + type + ", name=" + name + ", businessEmail="
				+ businessEmail + ", dateOfHiring=" + dateOfHiring + ", department=" + department + ", jobTitle="
				+ jobTitle + ", reportsTo=" + reportsTo + ", location=" + location + ", lastLogin=" + lastLogin
				+ ", roles=" + roles + "]";
	}
	
	
	
}
