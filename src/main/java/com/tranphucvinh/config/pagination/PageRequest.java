package com.tranphucvinh.config.pagination;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest extends AbstractPage {

	public static final String PARAM_NAME = "pageRequest";

	@NotNull
    private int page = 1; // default

	@NotNull
    private int pageSize = 10; // default

    private String sortSql;

	@Override
	public void setTotal(int total) {
		this.total = total;
	}
}
