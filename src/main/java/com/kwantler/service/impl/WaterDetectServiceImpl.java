package com.kwantler.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwantler.dao.WaterDetectMapper;
import com.kwantler.model.WaterDetect;
import com.kwantler.service.IWaterDetectService;

/**
 * 
 * @author
 * @desc service
 *
 */
@Service
public class WaterDetectServiceImpl implements IWaterDetectService {

	@Autowired
	WaterDetectMapper waterDetectMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return waterDetectMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(WaterDetect record) {
		return waterDetectMapper.insert(record);
	}

	@Override
	public int insertSelective(WaterDetect record) {
		return waterDetectMapper.insertSelective(record);
	}

	@Override
	public WaterDetect selectByPrimaryKey(Integer id) {
		return waterDetectMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(WaterDetect record) {
		return waterDetectMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(WaterDetect record) {
		return waterDetectMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<WaterDetect> selectAll(Map<String, Object> params) {
		return waterDetectMapper.selectAll(params);
	}

	@Override
	public int updateStatusByPk(WaterDetect record) {
		return waterDetectMapper.updateStatusByPk(record);
	}


}
