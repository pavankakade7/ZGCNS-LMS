package com.zgcns.leavemanagement.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zgcns.leavemanagement.models.AppliedLeave;
import com.zgcns.leavemanagement.security.CurrentUser;
import com.zgcns.leavemanagement.security.UserPrincipal;
import com.zgcns.leavemanagement.services.AppliedLeaveService;
import com.zgcns.leavemanagement.storage.FileSystemStorageService;
import com.zgcns.leavemanagement.storage.StorageService;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class AppliedLeaveRestController {
	
	@Autowired	
	private AppliedLeaveService appliedLeaveService;
	@Autowired
	private StorageService storageService;

	
	public AppliedLeaveRestController(AppliedLeaveService appliedLeaveService, FileSystemStorageService storageService) {
		this.appliedLeaveService = appliedLeaveService;
		this.storageService = storageService;
	}
	
	@GetMapping("/appliedleave")
	@PreAuthorize("hasAuthority('HR')")
	public List<AppliedLeave> doListAppliedLeave() {
		List<AppliedLeave> appliedLeave = this.appliedLeaveService.findAll();
		return appliedLeave;
	}
	
	@GetMapping("/appliedleave/{year}")
	@PreAuthorize("hasAuthority('HR')")
	public List<AppliedLeave> doListAppliedLeaveByYear(@PathVariable("year") int year) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, Calendar.JANUARY, 1);
		Date startDate = cal.getTime();
		cal.set(year, Calendar.DECEMBER, 31);
		Date endDate = cal.getTime();
		
		List<AppliedLeave> appliedLeave = this.appliedLeaveService.findLeaveBetweenDates(startDate, endDate);
		return appliedLeave;
	}
	
	@PostMapping("/appliedleave")
	public ResponseEntity<?> doSaveAppliedLeave(@RequestBody AppliedLeave appliedLeave) {

		Map<String, String> responseEntityMessage = new HashMap<String, String> ();
		try {

			AppliedLeave newAppliedLeave = this.appliedLeaveService.create(appliedLeave);
			if(newAppliedLeave == null) {
				responseEntityMessage.put("message", "Leave application failed");
				return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.BAD_REQUEST);				
			}			
		} catch (Exception e) {

			if(e.getMessage().substring(0, 6).contentEquals("csilms")) {
				responseEntityMessage.put("message", e.getMessage().substring(8));
				return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.CONFLICT);
			} else {
				responseEntityMessage.put("message", "An unexpected error has occurred");
				return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// Leave application created successfully
		responseEntityMessage.put("message", "Leave application submitted");
		return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.CREATED );		
	}

	@DeleteMapping("/appliedleave/{emplid}/{effDate}/{startDate}/{leavecode}")
	public ResponseEntity<?> doDeleteAppliedLeaveById(@PathVariable("emplid") String emplid,  
										   @PathVariable("effDate") String effDateStr, 
										   @PathVariable("startDate") String startDateStr,
										   @PathVariable("leavecode") String leaveCode) {
		
		Map <String, String> responseEntityMessage = new HashMap<String, String> ();
				
		// If both dates are not null, parse and create Date objects
		Date startDate = null, effDate = null;
		boolean dateValid = false;
		
		if(startDateStr != null && effDateStr != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				startDate = sdf.parse(startDateStr);
				effDate = sdf.parse(effDateStr);
				dateValid = true;				
			} catch (Exception e) {
				dateValid = false;
			}
		}
		
		if(!dateValid) {
			responseEntityMessage.put("message","Invalid Date supplied");
			return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.BAD_REQUEST);
		}
		
		// Retrieve Applied Leave, and attachment file name
		AppliedLeave appliedLeave = this.appliedLeaveService.findById(emplid, effDate, startDate, leaveCode);
		if(appliedLeave == null) {
			responseEntityMessage.put("message","Leave application not found");
			return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.NOT_FOUND);
		} 
		
		String attachedFileName = appliedLeave.getAttachment();
		
		boolean result = this.appliedLeaveService.deleteById(emplid, effDate, startDate, leaveCode);
		if(!result) {
			responseEntityMessage.put("message","Unable to delete leave applied");
			return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// If Applied Leave deleted, also delete attachment
		if(! attachedFileName.contentEquals(""))
			this.storageService.delete(attachedFileName);
		
		responseEntityMessage.put("message","Success");
		return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.OK);
	}

	@PatchMapping("/appliedleave/{emplid}")
	public AppliedLeave doUpdateAppliedLeaveById(@PathVariable("emplid") String emplid,  
												 @RequestParam("effDate") String effDateStr, 
												 @RequestParam("startDate") String startDateStr,
												 @RequestParam("leavecode") String leaveCode,
												 @RequestBody AppliedLeave appliedLeave) {
		
		AppliedLeave newAppliedLeave = this.appliedLeaveService.save(appliedLeave);
		return newAppliedLeave;
	}

	@GetMapping("/appliedleave/count/{emplid}/{leavecode}/{year}")
	public float doCountAppliedLeaveTakenByLeaveCode(@PathVariable("emplid") String emplid,
													@PathVariable("leavecode") String leaveCode,  
													@PathVariable("year") int year) {
		return this.appliedLeaveService.countLeaveTakenByEmplidAndLeaveCodeAndYear(emplid, leaveCode, year);
	}
	
	@GetMapping("/appliedleave/{emplid}/{effDate}/{startDate}/{leavecode}")
	public AppliedLeave doGetAppliedLeaveById(@PathVariable("emplid") String emplid,  
											  @PathVariable("effDate") String effDateStr, 
											  @PathVariable("startDate") String startDateStr,
											  @PathVariable("leavecode") String leaveCode) {
		
		// Try create Effective Date and Start Date from Path Variable
		Date startDate = null, effDate = null;
		boolean dateValid = false;
				
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			startDate = sdf.parse(startDateStr);
			effDate = sdf.parse(effDateStr);
			dateValid = true;				
		} catch (Exception e) {
			dateValid = false;
		}
		
		if(dateValid) 
			return this.appliedLeaveService.findById(emplid, effDate, startDate, leaveCode);			
		else 
			return null;
	}
	
	@PatchMapping("/appliedleave/{emplid}/{effDate}/{startDate}/{leavecode}/{leaveStatus}")
	public ResponseEntity<?> doUpdateAppliedLeaveStatus(@PathVariable("emplid") String emplid,  
											  @PathVariable("effDate") String effDateStr, 
											  @PathVariable("startDate") String startDateStr,
											  @PathVariable("leavecode") String leaveCode,
											  @PathVariable("leaveStatus") String leaveStatus) {
		
		Map <String, String> responseEntityMessage = new HashMap<String, String> ();
		
		// Try create Effective Date and Start Date from Path Variable
		Date startDate = null, effDate = null;
		boolean dateValid = false;
				
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			startDate = sdf.parse(startDateStr);
			effDate = sdf.parse(effDateStr);
			dateValid = true;				
		} catch (Exception e) {
			dateValid = false;
		}
		
		if(! dateValid) {			
			responseEntityMessage.put("message","Input Date is invalid");
			return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.BAD_REQUEST);	
		}
		
		// Retrieve the applied leave 
		AppliedLeave thisAppliedLeave = this.appliedLeaveService.findById(emplid, effDate, startDate, leaveCode);
		
		if(thisAppliedLeave == null){			
			responseEntityMessage.put("message","Leave application not found");
			return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.NOT_FOUND);	
		}
		
		try {
			this.appliedLeaveService.updateLeaveStatus(thisAppliedLeave, leaveStatus);
		} catch (Exception e) {
			
			if(e.getMessage().substring(0, 6).contentEquals("csilms")) {
				responseEntityMessage.put("message", e.getMessage().substring(8));
				return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.BAD_REQUEST);
			} else {
				responseEntityMessage.put("message", "An unexpected error has occurred");
				return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		responseEntityMessage.put("message","Success");
		return new ResponseEntity<Map<String, String>>(responseEntityMessage, HttpStatus.OK);	
	}
	
	@GetMapping("/appliedleave/{emplid}")
	public List<AppliedLeave> doListAppliedLeaveByEmplid(@PathVariable("emplid") String emplid, 
													 	 @RequestParam(value="leaveCode", required=false) String leaveCode,
														 @RequestParam(value="leaveStatus", required=false) String leaveStatus,
													 	 @RequestParam(value="startDate", required=false) String startDateStr,
														 @RequestParam(value="endDate", required=false) String endDateStr) {
				
		// If both dates are not null, parse and create Date objects
		Date startDate = null, endDate = null;
		boolean datePresent = false;
		
		if(startDateStr != null && endDateStr != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				startDate = sdf.parse(startDateStr);
				endDate = sdf.parse(endDateStr);
				datePresent = true;				
			} catch (Exception e) {
				datePresent = false;
			}
		}
		
		List<AppliedLeave> appliedLeave;
		
		// Call corresponding Service method base on PARAM provided
		if(leaveCode != null && leaveStatus != null && datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeAndLeaveStatusAndBetweenDate
								(emplid, leaveCode, leaveStatus, startDate, endDate);
			
		} else if(leaveCode != null && leaveStatus != null && !datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeAndLeaveStatus
								(emplid, leaveCode, leaveStatus);
		
		} else if(leaveCode != null && leaveStatus == null && datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeBetweenDates
								(emplid, leaveCode, startDate, endDate);
			
		} else if(leaveCode != null && leaveStatus == null && !datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCode
								(emplid, leaveCode);
		
		} else if(leaveCode == null && leaveStatus != null && datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveStatusBetweenDates
								(emplid, leaveStatus, startDate, endDate);
		
		} else if(leaveCode == null && leaveStatus != null && !datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveStatus
								(emplid, leaveStatus);
		
		} else if(leaveCode == null && leaveStatus == null && datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidBetweenDates
								(emplid, startDate, endDate);
		
		} else //(leaveCode == null && leaveStatus == null && !datePresent) 
			appliedLeave = this.appliedLeaveService.findByEmplid(emplid);
		
		return appliedLeave;
	}
	
	@GetMapping("/appliedleave/count/{emplid}")
	public long doCountAppliedLeaveByEmplid(@PathVariable("emplid") String emplid, 
											@RequestParam("leaveCode") String leaveCode) {
		long count = this.appliedLeaveService.countByEmplidAndLeaveCode(emplid, leaveCode);
		return count;
	}
	
	@GetMapping("/appliedleave/count/{emplid}/{leaveCode}")
	public long doCountAppliedLeaveByEmplidB(@PathVariable("emplid") String emplid, 
											 @PathVariable("leaveCode") String leaveCode) {
		long count = this.appliedLeaveService.countByEmplidAndLeaveCode(emplid, leaveCode);
		return count;
	}	

/*	@RequestMapping(value="/appliedleave/count/{approver}/pendingapproval", method=RequestMethod.GET)
	public long doCountPendingApproverApproval(@PathVariable("approver") String approver) {
		List<String> leaveStatusList = new ArrayList<String> ();
		leaveStatusList.add("PNAPV");
		leaveStatusList.add("PNCLD");
		long count = this.appliedLeaveService.countByApproverAndLeaveStatusIn(approver, leaveStatusList);
		return count;
	}	

	@RequestMapping(value="/appliedleave/{approver}/pendingapproval", method=RequestMethod.GET)
	public List<AppliedLeave> doFindPendingApproverApproval(@PathVariable("approver") String approver) {
		List<String> leaveStatusList = new ArrayList<String> ();
		leaveStatusList.add("PNAPV");
		leaveStatusList.add("PNCLD");
		List<AppliedLeave> appliedLeaveList = this.appliedLeaveService.findByApproverAndLeaveStatusIn(approver, leaveStatusList);
		return appliedLeaveList;
	} */
	

	@GetMapping("/appliedleave/me")
	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public List<AppliedLeave> doGetMyAppliedLeaveByEmplid(@CurrentUser UserPrincipal currentUser, 
													 	 @RequestParam(value="leaveCode", required=false) String leaveCode,
														 @RequestParam(value="leaveStatus", required=false) String leaveStatus,
													 	 @RequestParam(value="startDate", required=false) String startDateStr,
														 @RequestParam(value="endDate", required=false) String endDateStr) {
				
		// If both dates are not null, parse and create Date objects
		Date startDate = null, endDate = null;
		boolean datePresent = false;
		
		if(startDateStr != null && endDateStr != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				startDate = sdf.parse(startDateStr);
				endDate = sdf.parse(endDateStr);
				datePresent = true;				
			} catch (Exception e) {
				datePresent = false;
			}
		}
		
		String emplid = currentUser.getId();
		
		List<AppliedLeave> appliedLeave;
		
		// Call corresponding Service method base on PARAM provided
		if(leaveCode != null && leaveStatus != null && datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeAndLeaveStatusAndBetweenDate
								(emplid, leaveCode, leaveStatus, startDate, endDate);
			
		} else if(leaveCode != null && leaveStatus != null && !datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeAndLeaveStatus
								(emplid, leaveCode, leaveStatus);
		
		} else if(leaveCode != null && leaveStatus == null && datePresent) {
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCodeBetweenDates
								(emplid, leaveCode, startDate, endDate);
			
		} else if(leaveCode != null && leaveStatus == null && !datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveCode
								(emplid, leaveCode);
		
		} else if(leaveCode == null && leaveStatus != null && datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveStatusBetweenDates
								(emplid, leaveStatus, startDate, endDate);
		
		} else if(leaveCode == null && leaveStatus != null && !datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidAndLeaveStatus
								(emplid, leaveStatus);
		
		} else if(leaveCode == null && leaveStatus == null && datePresent) { 				
			appliedLeave = this.appliedLeaveService.
								findByEmplidBetweenDates
								(emplid, startDate, endDate);
		
		} else //(leaveCode == null && leaveStatus == null && !datePresent) 
			appliedLeave = this.appliedLeaveService.findByEmplid(emplid);
		
		return appliedLeave;
	}

	@GetMapping("/appliedleave/count/me/pendingapproval")
	public long doCountPendingMyApproval(@CurrentUser UserPrincipal currentUser) {
		String approver = currentUser.getId();
		List<String> leaveStatusList = new ArrayList<String> ();
		leaveStatusList.add("PNAPV");
		leaveStatusList.add("PNCLD");
		long count = this.appliedLeaveService.countByApproverAndLeaveStatusIn(approver, leaveStatusList);
		return count;
	}	

	@GetMapping("/appliedleave/me/pendingapproval")
	public List<AppliedLeave> doFindPendingMyApproval(@CurrentUser UserPrincipal currentUser) {
		String approver = currentUser.getId();
		List<String> leaveStatusList = new ArrayList<String> ();
		leaveStatusList.add("PNAPV");
		leaveStatusList.add("PNCLD");
		List<AppliedLeave> appliedLeaveList = this.appliedLeaveService.findByApproverAndLeaveStatusIn(approver, leaveStatusList);
		return appliedLeaveList;
	}	
	
	@GetMapping("/appliedleave/me/{year}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
	public List<AppliedLeave> doFindMyReporteeLeaves(@CurrentUser UserPrincipal currentUser, 
			 										 @PathVariable("year") int year) {
		String approver = currentUser.getId();
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1);
		Date startDate = cal.getTime();
		cal.set(year, 11, 31);
		Date endDate = cal.getTime();
		
		List<AppliedLeave> appliedLeaveList = this.appliedLeaveService.findByManagerReporteeBetweenDates(approver, startDate, endDate);
		return appliedLeaveList;
	}	

}
