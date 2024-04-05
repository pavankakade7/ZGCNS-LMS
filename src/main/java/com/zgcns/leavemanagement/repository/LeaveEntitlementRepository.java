package com.zgcns.leavemanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.LeaveEntitlement;
import com.zgcns.leavemanagement.models.LeaveEntitlementId;



@Repository
public interface LeaveEntitlementRepository extends CrudRepository<LeaveEntitlement, LeaveEntitlementId>  {

	List<LeaveEntitlement> findByIdEmplidAndIdYear(String emplid, int year);
	List<LeaveEntitlement> findByIdEmplidAndIdLeaveCode(String emplid, String leaveCode);
	List<LeaveEntitlement> findByIdEmplid(String emplid);
	List<LeaveEntitlement> findByIdYear(int year);
	List<LeaveEntitlement> findByIdEmplidIn(List<String> emplIdList);
}
