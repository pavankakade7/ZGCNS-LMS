package com.zgcns.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.Roles;


@Repository
public interface RoleRepository extends JpaRepository<Roles, String>{

}