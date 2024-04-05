package com.zgcns.leavemanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.LeaveDetail;


@Repository
public interface LeaveDetailRepository extends CrudRepository<LeaveDetail, Integer> {

}
