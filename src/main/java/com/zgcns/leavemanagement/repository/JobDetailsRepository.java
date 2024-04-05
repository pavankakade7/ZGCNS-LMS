package com.zgcns.leavemanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.JobDetails;
import com.zgcns.leavemanagement.models.JobDetailsId;




@Repository
public interface JobDetailsRepository extends CrudRepository<JobDetails, JobDetailsId> {

	List<JobDetails> findByIdEmplid(String emplid);

}
