package com.tranphucvinh.mybatis.mapper;

import java.util.List;

import com.tranphucvinh.mybatis.model.MasterVO;

public interface MasterMapper {

    List<MasterVO> selectMasterList();
}
