package com.tranphucvinh.controller;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranphucvinh.config.pagination.Page;
import com.tranphucvinh.config.pagination.PageRequest;
import com.tranphucvinh.mybatis.model.AccountVO;
import com.tranphucvinh.payload.ReqAdminApiPageList;
import com.tranphucvinh.service.AccountService;

@RestController
@RequestMapping("/admin/api/")
public class AdminRestController {
    
    @Autowired
    private AccountService accountService;

    @GetMapping("/pageList")
    public ResponseEntity<Page<AccountVO>> pageList(@ParameterObject @Valid PageRequest pageRequest,@ParameterObject @Valid ReqAdminApiPageList searchRequest) {
        Page<AccountVO> page = new Page<AccountVO>().with(pageRequest,accountService.selectAdminList(searchRequest, pageRequest));
        return ResponseEntity.ok(page);
    }
}
