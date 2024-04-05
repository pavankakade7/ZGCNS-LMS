package com.zgcns.leavemanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.LeaveCategory;

@Repository
public interface LeaveCategoryRepository extends CrudRepository<LeaveCategory, String> {

}
