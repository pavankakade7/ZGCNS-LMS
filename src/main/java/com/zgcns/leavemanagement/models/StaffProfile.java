package com.zgcns.leavemanagement.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="staffprofiles")
public class StaffProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="zgcns_staff_id")
	private String zgcnsStaffId;
	
	@Column(name="staff_name")
	private String staffName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="job_title")
	private String jobTitle; 
	
	@Column(name="mobile_no")
	private String mobileNo; 
	
	@Column(name="join_date")
	private Date joinDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZgcnsStaffId() {
		return zgcnsStaffId;
	}

	public void setZgcnsStaffId(String zgcnsStaffId) {
		this.zgcnsStaffId = zgcnsStaffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public StaffProfile(int id, String zgcnsStaffId, String staffName, String email, String jobTitle, String mobileNo,
			Date joinDate) {
		super();
		this.id = id;
		this.zgcnsStaffId = zgcnsStaffId;
		this.staffName = staffName;
		this.email = email;
		this.jobTitle = jobTitle;
		this.mobileNo = mobileNo;
		this.joinDate = joinDate;
	}

	public StaffProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "StaffProfile [id=" + id + ", zgcnsStaffId=" + zgcnsStaffId + ", staffName=" + staffName + ", email="
				+ email + ", jobTitle=" + jobTitle + ", mobileNo=" + mobileNo + ", joinDate=" + joinDate + "]";
	} 
	
	
	
	
}
