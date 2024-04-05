package com.zgcns.leavemanagement.models;



import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_details")
public class LoginDetails {
	
	@Id
	@Column(name = "emplid")
	private String emplId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "userid", unique = true)
	private String userId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "buss_email")
	private String businessEmail;
	
	@JsonIgnore
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "emplid"), inverseJoinColumns = @JoinColumn(name = "role"))
	private Set<Roles> roles = new HashSet<Roles>();

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}



	public LoginDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginDetails(String emplId, String userId, String name, String businessEmail, String password,
			Set<Roles> roles) {
		super();
		this.emplId = emplId;
		this.userId = userId;
		this.name = name;
		this.businessEmail = businessEmail;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "LoginDetails [emplId=" + emplId + ", userId=" + userId + ", name=" + name + ", businessEmail="
				+ businessEmail + ", password=" + password + ", roles=" + roles + "]";
	}


	
}
