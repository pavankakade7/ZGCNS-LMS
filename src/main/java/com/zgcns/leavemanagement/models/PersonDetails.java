package com.zgcns.leavemanagement.models;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "person_details")  // other information
public class PersonDetails {
	
	@Id
	@Column(name = "emplid")
	private String emplId;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name = "personal_phone_number")
	private String personalPhoneNumber;
	
	@Column(name = "permanent_address")
	private String permanentAddress;
	
	@Column(name = "temporary_address")
	private String temporaryAddress;
	
	@Column(name = "highest_educational_qualification")
	private String highestEducationalQualification;
	
	@Column(name = "aadhaar_number")
	private String aadhaarNumber;
	
	@Column(name = "marital_Status")
	private String maritalStatus;
	
	@Column(name = "work_experience")
	private String workExperience;
	
	@Column(name = "previous_employer")
	private String previousEmployer;
	
	@Column(name = "previous_designation")
	private String previousDesignation;
	
	@Column(name = "emergency_contact_name")
	private String emergencyContactName;
	
	@Column(name = "emergency_contact_number")
	private String emergencyContactNumber;
	
	@Column(name = "emergency_contact_relation")
	private String emergencyContactRelation;
	
	@Column(name = "blood_group")
	private String bloodGroup;
	
	@Column(name = "nationality")
	private String nationality;

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPersonalPhoneNumber() {
		return personalPhoneNumber;
	}

	public void setPersonalPhoneNumber(String personalPhoneNumber) {
		this.personalPhoneNumber = personalPhoneNumber;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getTemporaryAddress() {
		return temporaryAddress;
	}

	public void setTemporaryAddress(String temporaryAddress) {
		this.temporaryAddress = temporaryAddress;
	}

	public String getHighestEducationalQualification() {
		return highestEducationalQualification;
	}

	public void setHighestEducationalQualification(String highestEducationalQualification) {
		this.highestEducationalQualification = highestEducationalQualification;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}

	public String getPreviousEmployer() {
		return previousEmployer;
	}

	public void setPreviousEmployer(String previousEmployer) {
		this.previousEmployer = previousEmployer;
	}

	public String getPreviousDesignation() {
		return previousDesignation;
	}

	public void setPreviousDesignation(String previousDesignation) {
		this.previousDesignation = previousDesignation;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public String getEmergencyContactRelation() {
		return emergencyContactRelation;
	}

	public void setEmergencyContactRelation(String emergencyContactRelation) {
		this.emergencyContactRelation = emergencyContactRelation;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public PersonDetails(String emplId, String phoneNumber, String gender, Date dateOfBirth, String personalPhoneNumber,
			String permanentAddress, String temporaryAddress, String highestEducationalQualification,
			String aadhaarNumber, String maritalStatus, String workExperience, String previousEmployer,
			String previousDesignation, String emergencyContactName, String emergencyContactNumber,
			String emergencyContactRelation, String bloodGroup, String nationality) {
		super();
		this.emplId = emplId;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.personalPhoneNumber = personalPhoneNumber;
		this.permanentAddress = permanentAddress;
		this.temporaryAddress = temporaryAddress;
		this.highestEducationalQualification = highestEducationalQualification;
		this.aadhaarNumber = aadhaarNumber;
		this.maritalStatus = maritalStatus;
		this.workExperience = workExperience;
		this.previousEmployer = previousEmployer;
		this.previousDesignation = previousDesignation;
		this.emergencyContactName = emergencyContactName;
		this.emergencyContactNumber = emergencyContactNumber;
		this.emergencyContactRelation = emergencyContactRelation;
		this.bloodGroup = bloodGroup;
		this.nationality = nationality;
	}

	public PersonDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PersonDetails [emplId=" + emplId + ", phoneNumber=" + phoneNumber + ", gender=" + gender
				+ ", dateOfBirth=" + dateOfBirth + ", personalPhoneNumber=" + personalPhoneNumber
				+ ", permanentAddress=" + permanentAddress + ", temporaryAddress=" + temporaryAddress
				+ ", highestEducationalQualification=" + highestEducationalQualification + ", aadhaarNumber="
				+ aadhaarNumber + ", maritalStatus=" + maritalStatus + ", workExperience=" + workExperience
				+ ", previousEmployer=" + previousEmployer + ", previousDesignation=" + previousDesignation
				+ ", emergencyContactName=" + emergencyContactName + ", emergencyContactNumber="
				+ emergencyContactNumber + ", emergencyContactRelation=" + emergencyContactRelation + ", bloodGroup="
				+ bloodGroup + ", nationality=" + nationality + "]";
	}
	
	
	
}
