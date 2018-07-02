package com.kwantler.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwantler.dao.WeatherStationMapper;
import com.kwantler.model.WeatherStation;
import com.kwantler.service.IWeatherStationService;

/**
 * 
 * @author
 * @desc service
 *
 */
@Service
public class WeatherStationServiceImpl implements IWeatherStationService {

	@Autowired
	WeatherStationMapper weatherStationMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return weatherStationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(WeatherStation record) {
		return weatherStationMapper.insert(record);
	}

	@Override
	public int insertSelective(WeatherStation record) {
		return weatherStationMapper.insertSelective(record);
	}

	@Override
	public WeatherStation selectByPrimaryKey(Integer id) {
		return weatherStationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(WeatherStation record) {
		return weatherStationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(WeatherStation record) {
		return weatherStationMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<WeatherStation> selectAll(Map<String, Object> params) {
		return weatherStationMapper.selectAll(params);
	}

	@Override
	public int updateStatusByPk(WeatherStation record) {
		return weatherStationMapper.updateStatusByPk(record);
	}

	@Override
	public WeatherStation selectNewestByDeviceId(String deviceId) {
		return weatherStationMapper.selectNewestByDeviceId(deviceId);
	}


}
