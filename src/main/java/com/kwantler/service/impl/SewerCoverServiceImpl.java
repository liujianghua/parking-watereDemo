package com.kwantler.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwantler.dao.SewerCoverMapper;
import com.kwantler.model.SewerCover;
import com.kwantler.service.ISewerCoverService;

/**
 * 
 * @author
 * @desc service
 *
 */
@Service
public class SewerCoverServiceImpl implements ISewerCoverService {

	@Autowired
	SewerCoverMapper sewerCoverMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return sewerCoverMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SewerCover record) {
		return sewerCoverMapper.insert(record);
	}

	@Override
	public int insertSelective(SewerCover record) {
		return sewerCoverMapper.insertSelective(record);
	}

	@Override
	public SewerCover selectByPrimaryKey(Integer id) {
		return sewerCoverMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SewerCover record) {
		return sewerCoverMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SewerCover record) {
		return sewerCoverMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SewerCover> selectAll(Map<String, Object> params) {
		return sewerCoverMapper.selectAll(params);
	}

	@Override
	public int updateStatusByPk(SewerCover record) {
		return sewerCoverMapper.updateStatusByPk(record);
	}

	@Override
	public List<SewerCover> selectNewestMonitorTime() {
		return sewerCoverMapper.selectNewestMonitorTime();
	}


}
