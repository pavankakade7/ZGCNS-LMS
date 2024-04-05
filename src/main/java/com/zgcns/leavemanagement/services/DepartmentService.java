package com.zgcns.leavemanagement.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zgcns.leavemanagement.models.Department;
import com.zgcns.leavemanagement.repository.DepartmentRepository;


@Service
public class DepartmentService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> findAll(){
		List<Department> departmentList = (List<Department>) this.departmentRepository.findAll();
		return departmentList;
	}
		
	public Department findById(String deptId) {
		return this.departmentRepository.findById(deptId).orElse(null);
	}	
}
