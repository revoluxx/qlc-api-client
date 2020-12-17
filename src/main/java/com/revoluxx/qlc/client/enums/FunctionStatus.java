package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum FunctionStatus {

	RUNNING("Running", "1"),
	STOPPED("Stopped", "0"),
	UNDEFINED("Undefined", "");

	private static final Map<String, FunctionStatus> map = new HashMap<String, FunctionStatus>();

	private final String name;
	private final String value;

	static {
		for (FunctionStatus functionType : FunctionStatus.values()) {
			map.put(functionType.getName(), functionType);
		}
	}

	private FunctionStatus(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static FunctionStatus ofName(String name) {
		return map.get(name);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
