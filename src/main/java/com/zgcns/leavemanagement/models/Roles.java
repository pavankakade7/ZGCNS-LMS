package com.zgcns.leavemanagement.models;


import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "role_table")
public class Roles {

	@Id
	private String role;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(name = "role_name", length = 60)
	private RoleName roleName;

	public String getRole() {
		return role;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}


	@Override
	public String toString() {

		return "Roles [role=" + this.role + ", " + this.roleName.toString() + "]";
	}

}
