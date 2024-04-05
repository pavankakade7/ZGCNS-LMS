package com.zgcns.leavemanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.LeaveStatus;


@Repository
public interface LeaveStatusRepository extends CrudRepository<LeaveStatus, Integer> {

}