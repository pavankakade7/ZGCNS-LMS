package com.zgcns.leavemanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.EmployeeDetails;
import com.zgcns.leavemanagement.models.PersonDetails;


@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, String>{
	EmployeeDetails findByEmplId(String emplId);

	List<EmployeeDetails> findByReportsTo(PersonDetails mgrId);
	
	@Query("SELECT DISTINCT e FROM EmployeeDetails e INNER JOIN e.roles r WHERE r.role = 'M'")
	List<EmployeeDetails> findAllManagers();
	
	List<EmployeeDetails> findByEmplIdNotIn(List<String> emplId);
	
	Optional<EmployeeDetails> findByBusinessEmail(String businessEmail);
	
	List<EmployeeDetails> findByType(String type);
	List<EmployeeDetails> findByDepartment(String department);
//	List<EmployeeDetails> findByreportsto(String reportsto);
	List<EmployeeDetails> findByName(String name);
	List<EmployeeDetails> findByJobTitle(String jobTitle);
//	List<EmployeeDetails> findByDateOfHiring(String dateofhiring);
}

