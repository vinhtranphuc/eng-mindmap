package com.tranphucvinh.mybatis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tranphucvinh.config.util.GroupRefer;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class GroupReferVO extends GroupRefer {

    @JsonIgnore
    private Integer grpSeq;
}
