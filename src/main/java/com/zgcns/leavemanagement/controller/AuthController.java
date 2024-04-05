package com.zgcns.leavemanagement.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zgcns.leavemanagement.models.LoginDetails;
import com.zgcns.leavemanagement.models.LoginRequest;
import com.zgcns.leavemanagement.repository.LoginDetailsRepository;
import com.zgcns.leavemanagement.security.CurrentUser;
import com.zgcns.leavemanagement.security.JwtAuthenticationResponse;
import com.zgcns.leavemanagement.security.JwtTokenProvider;
import com.zgcns.leavemanagement.security.UserPrincipal;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	LoginDetailsRepository loginDetailsRepository;

	@Autowired
	JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		LoginDetails loginDetails = loginDetailsRepository.findByUserIdAndLockAccount(loginRequest.getEmail(), 0)
				.orElse(null);
		
		if(loginDetails == null) {
			Map<String, String> mapResponse = new HashMap<String, String>();
			mapResponse.put("message", "LOCKED");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapResponse);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@GetMapping("/user/me")
	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public LoginDetails getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		Optional<LoginDetails> loginDetails = loginDetailsRepository.findByEmplId(currentUser.getId());

		return loginDetails.get();
	}

}
