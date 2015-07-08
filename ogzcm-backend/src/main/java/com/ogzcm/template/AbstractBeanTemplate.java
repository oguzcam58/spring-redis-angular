package com.ogzcm.template;

import org.springframework.data.annotation.Id;

public abstract class AbstractBeanTemplate {
	@Id
	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
