package com.kwantler.dao;

import java.util.List;
import java.util.Map;

import com.kwantler.model.SewerCover;

public interface SewerCoverMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SewerCover record);

    int insertSelective(SewerCover record);

    SewerCover selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SewerCover record);

    int updateByPrimaryKey(SewerCover record);
    
	List<SewerCover> selectAll(Map<String, Object> params);

	int updateStatusByPk(SewerCover record);
	
	List<SewerCover> selectNewestMonitorTime();
}