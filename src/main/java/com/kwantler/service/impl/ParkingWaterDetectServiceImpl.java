package com.kwantler.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwantler.dao.ParkingWaterDetectMapper;
import com.kwantler.model.ParkingWaterDetect;
import com.kwantler.service.IParkingWaterDetectService;

/**
 * 
 * @author
 * @desc service
 *
 */
@Service
public class ParkingWaterDetectServiceImpl implements IParkingWaterDetectService {

	@Autowired
	ParkingWaterDetectMapper parkingWaterDetectMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return parkingWaterDetectMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ParkingWaterDetect record) {
		return parkingWaterDetectMapper.insert(record);
	}

	@Override
	public int insertSelective(ParkingWaterDetect record) {
		return parkingWaterDetectMapper.insertSelective(record);
	}

	@Override
	public ParkingWaterDetect selectByPrimaryKey(Integer id) {
		return parkingWaterDetectMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ParkingWaterDetect record) {
		return parkingWaterDetectMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ParkingWaterDetect record) {
		return parkingWaterDetectMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ParkingWaterDetect> selectAll(Map<String, Object> params) {
		return parkingWaterDetectMapper.selectAll(params);
	}

	@Override
	public int updateStatusByPk(ParkingWaterDetect record) {
		return parkingWaterDetectMapper.updateStatusByPk(record);
	}

	@Override
	public ParkingWaterDetect selectBaseWaterLevel(Map<String, Object> params) {
		return parkingWaterDetectMapper.selectBaseWaterLevel(params);
	}

	@Override
	public List<ParkingWaterDetect> selectNewestMonitorTime() {
		return parkingWaterDetectMapper.selectNewestMonitorTime();
	}

}
