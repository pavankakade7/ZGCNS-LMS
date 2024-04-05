package com.zgcns.leavemanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.Translateitem;
import com.zgcns.leavemanagement.models.TranslateitemId;



@Repository
public interface TranslateitemRepository extends CrudRepository<Translateitem, TranslateitemId> {

	List<Translateitem> findByIdFieldname(String fieldname);
	Translateitem findByIdFieldnameAndIdFieldvalue(String fieldname, String fieldvalue);
	

}
