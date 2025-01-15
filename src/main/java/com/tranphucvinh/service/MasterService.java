package com.tranphucvinh.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tranphucvinh.mybatis.mapper.MasterMapper;
import com.tranphucvinh.mybatis.model.MasterVO;

@Service
public class MasterService {

    @Resource
    private MasterMapper masterMapper;

    public List<MasterVO> selectMasterList() {
        return masterMapper.selectMasterList();
    }
}
