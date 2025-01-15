package com.tranphucvinh.config.util;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tranphucvinh.constant.GrpRefTableEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupRefer {

    public GroupRefer(@NotNull GrpRefTableEnum grpRefTable, @NotNull GrpRefTableEnum.RefTypeEnum grpRefType,
			Long grpRefPrimaryId) {
		super();
		this.grpRefTable = grpRefTable;
		this.grpRefType = grpRefType;
		this.grpRefPrimaryId = grpRefPrimaryId;
	}

	@NotNull
    protected GrpRefTableEnum grpRefTable;
    @NotNull
    protected GrpRefTableEnum.RefTypeEnum grpRefType;
    protected Long grpRefPrimaryId;

    @JsonIgnore
    private String groupFieldName;
    @JsonIgnore
    private String primaryIdFieldName;
}
