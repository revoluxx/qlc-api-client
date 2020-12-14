package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum FunctionStatus {

	RUNNING("Running"),
	STOPPED("Stopped"),
	UNDEFINED("Undefined");

	private static final Map<String, FunctionStatus> map = new HashMap<String, FunctionStatus>();

	private final String name;

	static {
		for (FunctionStatus functionType : FunctionStatus.values()) {
			map.put(functionType.getName(), functionType);
		}
	}

	private FunctionStatus(String name) {
		this.name = name;
	}

	public static FunctionStatus ofName(String name) {
		return map.get(name);
	}

	public String getName() {
		return name;
	}

}
