package com.tranphucvinh.config.pagination;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T> extends PageRequest {

    private List<? extends T> content;

	public Page<T> with(PageRequest pageRequest, List<? extends T> content) {
		this.setPage(pageRequest.getPage());
		this.setPageSize(pageRequest.getPageSize());
		this.setTotal(pageRequest.total);
		this.content = content;
		return this;
	}
}
