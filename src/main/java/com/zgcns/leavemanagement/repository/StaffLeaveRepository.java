package com.zgcns.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.StaffLeave;



@Repository
public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Integer> {

}
