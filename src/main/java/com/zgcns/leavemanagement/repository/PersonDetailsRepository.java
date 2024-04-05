package com.zgcns.leavemanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.PersonDetails;

@Repository
public interface PersonDetailsRepository extends CrudRepository<PersonDetails, String> {

}
