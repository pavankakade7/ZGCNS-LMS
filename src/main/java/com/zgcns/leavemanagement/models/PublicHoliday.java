package com.zgcns.leavemanagement.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name="public_holiday")
public class PublicHoliday {
	
	@Id
	@Column(name="holiday_date")
	private Date holidayDate;
	
	@Column(name="holiday_day")
	private String holidayDay;
	
	@Column(name="public_holiday_descr")
	private String holidayDescr;
	

	public PublicHoliday() {
		
	}
	
	public PublicHoliday(Date holidayDate, String holidayDay, String holidayDescr) {
		this.holidayDate = holidayDate;
		this.holidayDay = holidayDay;
		this.holidayDescr = holidayDescr;	
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getHolidayDay() {
		return holidayDay;
	}

	public void setHolidayDay(String holidayDay) {
		this.holidayDay = holidayDay;
	}

	public String getHolidayDescr() {
		return holidayDescr;
	}

	public void setHolidayDescr(String holidayDescr) {
		this.holidayDescr = holidayDescr;
	}



	@Override
	public String toString() {
		return "PublicHoliday [holidayDate=" + holidayDate + ", holidayDay=" + holidayDay + ", holidayDescr="
				+ holidayDescr ;
	}
	
}
