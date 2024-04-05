package com.zgcns.leavemanagement.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.LoginDetails;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, String>{
	
	Optional<LoginDetails> findByUserIdAndLockAccount(String userId, int lockAccount);
	Optional<LoginDetails> findByUserId(String userId);
	Optional<LoginDetails> findByEmplId(String emplId);
	Optional<LoginDetails> deleteByEmplId(String emplId);
}
