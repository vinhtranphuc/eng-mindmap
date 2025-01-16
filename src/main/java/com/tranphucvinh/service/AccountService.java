package com.tranphucvinh.service;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.tranphucvinh.config.pagination.PageRequest;
import com.tranphucvinh.mybatis.mapper.AccountMapper;
import com.tranphucvinh.mybatis.model.AccountVO;
import com.tranphucvinh.payload.ReqAdminApiPageList;

@Service
public class AccountService {
    
    @Resource
    private AccountMapper accountMapper;

    public List<AccountVO> selectAdminList(@Valid ReqAdminApiPageList searchReq,
            @Valid PageRequest pageRequest) {
        return accountMapper.selectAdminList(searchReq, pageRequest);
    }
}
