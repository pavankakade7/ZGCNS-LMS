package com.zgcns.leavemanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.StaffProfile;



@Repository
public interface StaffProfileRepository extends CrudRepository<StaffProfile, Integer> {

}
