package com.zgcns.leavemanagement.models;

import jakarta.annotation.Nonnull;

public class LoginRequest {
    
	@Nonnull
    private String email;
	
	@Nonnull
    private String password;

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

