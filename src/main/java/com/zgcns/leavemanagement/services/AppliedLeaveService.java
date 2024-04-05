package com.zgcns.leavemanagement.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.zgcns.leavemanagement.models.AppliedLeave;
import com.zgcns.leavemanagement.models.AppliedLeaveId;
import com.zgcns.leavemanagement.models.EmployeeDetails;
import com.zgcns.leavemanagement.models.LeaveCategory;
import com.zgcns.leavemanagement.models.LeaveEntitlement;
import com.zgcns.leavemanagement.models.Translateitem;
import com.zgcns.leavemanagement.repository.AppliedLeaveRepository;


@Service
public class AppliedLeaveService {
	@Autowired
	private AppliedLeaveRepository appliedLeaveRepository;
	@Autowired
	private EmployeeDetailsService employeeDetailsService;
	@Autowired
	private LeaveEntitlementService leaveEntitlementService;
	@Autowired
	private TranslateitemService translateitemService;
	@Autowired
	private LeaveCategoryService leaveCategoryService;

	
	public AppliedLeaveService(AppliedLeaveRepository appliedLeaveRepository,
			                   EmployeeDetailsService employeeDetailsService,
			                   LeaveEntitlementService leaveEntitlementService,	
			                   TranslateitemService translateitemService,
			                   LeaveCategoryService leaveCategoryService) {
		this.appliedLeaveRepository = appliedLeaveRepository;
		this.employeeDetailsService = employeeDetailsService;
		this.leaveEntitlementService = leaveEntitlementService;	
		this.translateitemService = translateitemService;
		this.leaveCategoryService = leaveCategoryService;
	}
	
	public List<AppliedLeave> findAll() {
		List<AppliedLeave> appliedLeaveList = (List<AppliedLeave>)this.appliedLeaveRepository.findAll();
		return appliedLeaveList;
	}
	
	public AppliedLeave create(AppliedLeave newAppliedLeave) throws Exception {
		// validate leave
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(newAppliedLeave.getId().getStartDate());
		int year = cal.get(Calendar.YEAR);
		
		// Check 1: If duration exceed current available leave entitlement
		LeaveEntitlement userLeaveEntitlement = leaveEntitlementService.findById(newAppliedLeave.getId().getEmplid(),
																				 year, 
																				 newAppliedLeave.getLeaveCategory().getLeaveCode());
		if(userLeaveEntitlement == null) {
			LeaveCategory thisLeaveCategory = this.leaveCategoryService.findById(newAppliedLeave.getLeaveCategory().getLeaveCode());
			throw new Exception("zgcns: You are not entitle for " + thisLeaveCategory.getLeaveDescr() +
					" in " + year + ". Please contact HR for further clarification.");
		}
		
		if(userLeaveEntitlement.getBalanceLeave() < newAppliedLeave.getLeaveDuration())
			throw new Exception("zgcns: You do not have sufficient leave balance for " + userLeaveEntitlement.getLeaveCategory().getLeaveDescr());
		
		// Check 2: If there's leave applied during the same duration
		List<AppliedLeave> userAppliedLeaveList = this.appliedLeaveRepository.findByIdEmplid(newAppliedLeave.getId().getEmplid());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newLeaveStartDate = newAppliedLeave.getId().getStartDate();
		Date newLeaveEndDate = newAppliedLeave.getEndDate();
				
		for(AppliedLeave userAppliedLeave : userAppliedLeaveList) {
			
			// Skip existing leaves that are cancelled or rejected
			if(userAppliedLeave.getLeaveStatus().contentEquals("CANCL") || userAppliedLeave.getLeaveStatus().contentEquals("REJCT"))
				continue;
			
			Date existingLeaveStartDate = userAppliedLeave.getId().getStartDate();
			Date existingLeaveEndDate = userAppliedLeave.getEndDate();
			String existingLeaveStartDateStr = sdf.format(existingLeaveStartDate);
			String existingLeaveEndDateStr = sdf.format(existingLeaveEndDate);
			
			// Check if leave start date overlaps with any existing leave start and end date
			if( (existingLeaveStartDate.before(newLeaveStartDate) && existingLeaveEndDate.after(newLeaveStartDate)) || 
					newLeaveStartDate.equals(existingLeaveStartDate) || 
					newLeaveStartDate.equals(existingLeaveEndDate)) 
				throw new Exception("zgcns: You have already applied " + userAppliedLeave.getLeaveCategory().getLeaveDescr() + " from " 
						+ existingLeaveStartDateStr + " to " + existingLeaveEndDateStr );
						
			if(newAppliedLeave.getLeaveDuration() > 1) {				
				// Check if leave end date overlaps with any existing leave, if start date and end date are different
				if( (existingLeaveStartDate.before(newLeaveEndDate) && existingLeaveEndDate.after(newLeaveEndDate)) || 
						newLeaveEndDate.equals(existingLeaveStartDate) || 
						newLeaveEndDate.equals(existingLeaveEndDate))
					throw new Exception("zgcns: You have already applied " + userAppliedLeave.getLeaveCategory().getLeaveDescr() + " from " 
							+ existingLeaveStartDateStr + " to " + existingLeaveEndDateStr );
				
				// Check if any existing leave are in between new leave start and end date
				if(newLeaveStartDate.before(existingLeaveStartDate) && newLeaveEndDate.after(existingLeaveEndDate))
					throw new Exception("zgcns: You have already applied " + userAppliedLeave.getLeaveCategory().getLeaveDescr() + " from " 
							+ existingLeaveStartDateStr + " to " + existingLeaveEndDateStr );
			}
		}
				
		return this.appliedLeaveRepository.save(newAppliedLeave);
	}
	
	public AppliedLeave updateLeaveStatus(AppliedLeave appliedLeave, String newLeaveStatus) throws Exception {
		
		// Validate Leave Status
		List<Translateitem> leaveStatusList = this.translateitemService.findByFieldname("leave_status");
		boolean validStatus = false;
		for(Translateitem leaveStatusItem : leaveStatusList) {
			if(leaveStatusItem.getId().getFieldvalue().equals(newLeaveStatus)) {
				validStatus = true;
				break;
			}
		}
		
		if(!validStatus) 
			throw new Exception("zgcns: Invalid Leave Status : " + newLeaveStatus);
		
		
		// Handle different type of leave status change
		String currentLeaveStatus = appliedLeave.getLeaveStatus();
		if(currentLeaveStatus.contentEquals("PNAPV") && newLeaveStatus.contentEquals("APPRV")) {
			
			// Update Leave Entitlement
			Calendar cal = Calendar.getInstance();
			cal.setTime(appliedLeave.getId().getStartDate());
			int year = cal.get(Calendar.YEAR);
			
			LeaveEntitlement userLeaveEntitlement = leaveEntitlementService.findById(appliedLeave.getId().getEmplid(),
																					 year, 
																					 appliedLeave.getLeaveCategory().getLeaveCode());
			
			if(userLeaveEntitlement.getBalanceLeave() < appliedLeave.getLeaveDuration())
				throw new Exception("zgcns: Employee does not have sufficient " + appliedLeave.getLeaveCategory().getLeaveCode()
						                + ". Leave balance: " + userLeaveEntitlement.getBalanceLeave() + " days, "
						                + "applying duration: " + appliedLeave.getLeaveDuration() + " days" );
			
			userLeaveEntitlement.setBalanceLeave(
					userLeaveEntitlement.getBalanceLeave() - appliedLeave.getLeaveDuration());
			userLeaveEntitlement.setTakenLeave(
					userLeaveEntitlement.getTakenLeave() + appliedLeave.getLeaveDuration());
			
			leaveEntitlementService.save(userLeaveEntitlement);
			
			// Also update Approved Date
			appliedLeave.setApprovedDate(new Date());
		
		} else if (currentLeaveStatus.contentEquals("PNCLD") && newLeaveStatus.contentEquals("CANCL")) {
			
			// Update Leave Entitlement
			Calendar cal = Calendar.getInstance();
			cal.setTime(appliedLeave.getId().getStartDate());
			int year = cal.get(Calendar.YEAR);
			
			LeaveEntitlement userLeaveEntitlement = leaveEntitlementService.findById(appliedLeave.getId().getEmplid(),
																					 year, 
																					 appliedLeave.getLeaveCategory().getLeaveCode());
			userLeaveEntitlement.setBalanceLeave(
					userLeaveEntitlement.getBalanceLeave() + appliedLeave.getLeaveDuration());
			userLeaveEntitlement.setTakenLeave(
					userLeaveEntitlement.getTakenLeave() - appliedLeave.getLeaveDuration());
			
			leaveEntitlementService.save(userLeaveEntitlement);
			
		} else if(currentLeaveStatus.contentEquals("PNAPV") && newLeaveStatus.contentEquals("REJCT")) {

			appliedLeave.setApprovedDate(new Date());
		}
		
		appliedLeave.setLeaveStatus(newLeaveStatus);
		
		return this.appliedLeaveRepository.save(appliedLeave);
	}
	
	public AppliedLeave save(AppliedLeave newAppliedLeave) {
		return this.appliedLeaveRepository.save(newAppliedLeave);
	}
	
	public AppliedLeave findById(AppliedLeaveId id) {		
		return this.appliedLeaveRepository.findById(id).orElse(null);
	}
	
	public AppliedLeave findById(String emplid, Date effDate, Date startDate, String leaveCode) {
		AppliedLeaveId id = new AppliedLeaveId(emplid, effDate, startDate, leaveCode);
		return this.appliedLeaveRepository.findById(id).orElse(null);
	}

	public boolean deleteById(String emplid, Date effDate, Date startDate, String leaveCode) {		
		AppliedLeaveId id = new AppliedLeaveId(emplid, effDate, startDate, leaveCode);
		this.appliedLeaveRepository.deleteById(id);
		
		// Check if the ID is deleted, return true if ID NOT found.
		return !(this.appliedLeaveRepository.existsById(id));
	}
	
	public List<AppliedLeave> findByEmplid(String emplid) {		
		return appliedLeaveRepository.findByIdEmplid(emplid);
	}
	
	public List<AppliedLeave> findLeaveBetweenDates(Date startDate, Date endDate) {
		return appliedLeaveRepository.findByIdStartDateBetween(startDate, endDate);		
	}
	
	public List<AppliedLeave> findByEmplidBetweenDates(String emplid, Date startDate, Date endDate) {
		return appliedLeaveRepository.findByIdEmplidAndIdStartDateBetween(emplid, startDate, endDate);		
	}
	
	public long countByEmplidAndLeaveCode(String emplid, String leaveCode) {
		return appliedLeaveRepository.countByIdEmplidAndIdLeaveCode(emplid, leaveCode);
	}
	
	public float countLeaveTakenByEmplidAndLeaveCodeAndYear(String emplid, String leaveCode, int year) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, Calendar.JANUARY, 1);
		Date startDate = cal.getTime();

		cal.set(year, Calendar.DECEMBER, 31);
		Date endDate = cal.getTime();
		
		List<String> leaveStatus = new ArrayList<String>();
		leaveStatus.add("APPRV");
		leaveStatus.add("PNCLD");
		
		List<AppliedLeave> appliedLeaveList = 
				appliedLeaveRepository.findByIdEmplidAndIdLeaveCodeAndIdStartDateBetweenAndLeaveStatusIn(emplid, leaveCode, startDate, endDate, leaveStatus);
		
		float totalLeaveTaken = 0;
		
		for(AppliedLeave appliedLeave : appliedLeaveList) {
			totalLeaveTaken += appliedLeave.getLeaveDuration();
		}
		return totalLeaveTaken;
	}
	
	public List<AppliedLeave> findByEmplidAndLeaveCode(String emplid, String leaveCode) {
		return appliedLeaveRepository.findByIdEmplidAndIdLeaveCode(emplid, leaveCode);		
	}

	public List<AppliedLeave> findByEmplidAndLeaveCodeBetweenDates(String emplid, String leaveCode, Date startDate, Date endDate) {
		return appliedLeaveRepository.findByIdEmplidAndIdLeaveCodeAndIdStartDateBetween(emplid, leaveCode, startDate, endDate);		
	}
	
	public List<AppliedLeave> findByEmplidAndLeaveStatus(String emplid, String leaveStatus) {
		return appliedLeaveRepository.findByIdEmplidAndLeaveStatus(emplid, leaveStatus);		
	}

	public List<AppliedLeave> findByEmplidAndLeaveStatusBetweenDates(String emplid, String leaveStatus, Date startDate, Date endDate) {
		return appliedLeaveRepository.findByIdEmplidAndLeaveStatusAndIdStartDateBetween(emplid, leaveStatus, startDate, endDate);		
	}
	
	public List<AppliedLeave> findByEmplidAndLeaveCodeAndLeaveStatus(String emplid, String leaveCode, String leaveStatus) {
		return appliedLeaveRepository.findByIdEmplidAndIdLeaveCodeAndLeaveStatus(emplid, leaveCode, leaveStatus);		
	}

	public List<AppliedLeave> findByEmplidAndLeaveCodeAndLeaveStatusAndBetweenDate(String emplid, 
																	 String leaveCode, 
																	 String leaveStatus, 
																	 Date startDate, 
																	 Date endDate) {
		return appliedLeaveRepository.
				findByIdEmplidAndIdLeaveCodeAndLeaveStatusAndIdStartDateBetween
				(emplid, leaveCode, leaveStatus, startDate, endDate);		
	}
		
	public long countByApproverAndLeaveStatusIn(String approver, List<String> leaveStatus) {
		return appliedLeaveRepository.countByApproverAndLeaveStatusIn(approver, leaveStatus);
	}
	
	public List<AppliedLeave> findByApproverAndLeaveStatusIn(String approver, List<String> leaveStatus) {
		return appliedLeaveRepository.findByApproverAndLeaveStatusIn(approver, leaveStatus);
	}
	
	/*
	 * METHODS for retrieving manager's reportee
	 */
	
	// Count number of applied leave for all employee under a manager, with a given leave code
	public long countByManagerReporteeAndLeaveCode(String mgrlid, String leaveCode) {

		ArrayList<String> emplList = (ArrayList<String>) findAllReportee(mgrlid);
		
		if(emplList.size() == 0)
			return 0;
		
		return appliedLeaveRepository.countByIdEmplidInAndIdLeaveCode(emplList, leaveCode);
	}
	
	// Retrieve number of applied leave for all employee under a manager, with a given leave code
	public List<AppliedLeave> findByManagerReporteeAndLeaveCode(String mgrlid, String leaveCode) {

		ArrayList<String> emplList = (ArrayList<String>) findAllReportee(mgrlid);
		
		if(emplList.size() == 0)
			return null;
		
		return appliedLeaveRepository.findByIdEmplidInAndIdLeaveCode(emplList, leaveCode);
	}
	
	// Count number of applied leave for all employee under a manager, with a given status
	public long countByManagerReporteeAndStatus(String mgrlid, String leaveStatus) {
		
		ArrayList<String> emplList = (ArrayList<String>) findAllReportee(mgrlid);
		
		if(emplList.size() == 0)
			return 0;
		
		return appliedLeaveRepository.countByIdEmplidInAndLeaveStatus(emplList, leaveStatus);
	}

	// Retrieve applied leave for all employee under a manager, with a given status
	public List<AppliedLeave> findByManagerReporteeAndStatus(String mgrlid, String leaveStatus) {
		
		ArrayList<String> emplList = (ArrayList<String>) findAllReportee(mgrlid);
		
		if(emplList.size() == 0)
			return null;
		
		return appliedLeaveRepository.findByIdEmplidInAndLeaveStatus(emplList, leaveStatus);
	}
		
	// Retrieve applied leave for all employee under a manager, for a given period of time
	public List<AppliedLeave> findByManagerReporteeBetweenDates(String mgrlid, Date startDate, Date endDate) {
		
		ArrayList<String> emplList = (ArrayList<String>) findAllReportee(mgrlid);
		
		if(emplList.size() == 0)
			return null;
		
		return appliedLeaveRepository.findByIdEmplidInAndIdStartDateBetween(emplList, startDate, endDate);
	}

	private List<String> findAllReportee(String mgrlid) {
		ArrayList<String> emplIdList = new ArrayList<String>();
		
		// Retrieve the list of employee reporting under Manager ID
		List<EmployeeDetails> empList= this.employeeDetailsService.findByMgrId(mgrlid);
		for(EmployeeDetails emp : empList)
			emplIdList.add(emp.getEmplId());
		
		return emplIdList;
	}
}

