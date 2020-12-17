package com.revoluxx.qlc.client.data;

import java.io.Serializable;

public class GetElementsListRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;

	public GetElementsListRecord() {
		
	}

	public GetElementsListRecord(final String id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GetFunctionsListRecord [id=" + id + ", name=" + name + "]";
	}

}
