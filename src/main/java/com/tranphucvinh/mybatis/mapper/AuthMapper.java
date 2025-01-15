package com.tranphucvinh.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tranphucvinh.mybatis.model.AccountVO;

@Mapper
public interface AuthMapper {

    AccountVO selectAccount(@Param("id") String id, @Param("loginId") String loginId);

    void updaetOffRequestLogout(String loginId);
}
