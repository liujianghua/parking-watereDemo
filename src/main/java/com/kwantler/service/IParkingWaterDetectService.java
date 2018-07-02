package com.kwantler.service;

import java.util.List;
import java.util.Map;

import com.kwantler.model.ParkingWaterDetect;

public interface IParkingWaterDetectService {
	int deleteByPrimaryKey(Integer id);

	int insert(ParkingWaterDetect record);

	int insertSelective(ParkingWaterDetect record);

	ParkingWaterDetect selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ParkingWaterDetect record);

	int updateByPrimaryKey(ParkingWaterDetect record);

	List<ParkingWaterDetect> selectAll(Map<String, Object> params);

	int updateStatusByPk(ParkingWaterDetect record);

	ParkingWaterDetect selectBaseWaterLevel(Map<String, Object> params);
	
	List<ParkingWaterDetect> selectNewestMonitorTime();

}