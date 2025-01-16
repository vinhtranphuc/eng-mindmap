package com.tranphucvinh.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tranphucvinh.config.pagination.PageRequest;
import com.tranphucvinh.mybatis.model.AccountVO;
import com.tranphucvinh.payload.ReqAdminApiPageList;

@Mapper
public interface AccountMapper {

    List<AccountVO> selectAdminList(@Param("searchRequest") ReqAdminApiPageList searchRequest, @Param("pageRequest") PageRequest pageRequest);
}
