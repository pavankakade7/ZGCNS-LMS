package com.zgcns.leavemanagement.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, String> {


}
