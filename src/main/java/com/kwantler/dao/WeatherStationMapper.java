package com.kwantler.dao;

import java.util.List;
import java.util.Map;

import com.kwantler.model.WeatherStation;

public interface WeatherStationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WeatherStation record);

    int insertSelective(WeatherStation record);

    WeatherStation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeatherStation record);

    int updateByPrimaryKey(WeatherStation record);
    
	List<WeatherStation> selectAll(Map<String, Object> params);
	
	int updateStatusByPk(WeatherStation record);
	
	WeatherStation selectNewestByDeviceId(String deviceId);
}