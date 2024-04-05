package com.zgcns.leavemanagement.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgcns.leavemanagement.models.PublicHoliday;



@Repository
public interface PublicHolidayRepository extends CrudRepository<PublicHoliday, Date> {

}
