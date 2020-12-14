package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum FunctionType {

	AUDIO("Audio"),
	CHASER("Chaser"),
	COLLECTION("Collection"),
	EFX("EFX"),
	RGBMATRIX("RGBMatrix"),
	SCENE("Scene"),
	SCRIPT("Script"),
	SEQUENCE("Sequence"),
	SHOW("Show"),
	UNDEFINED("Undefined"),
	VIDEO("Video");

	private static final Map<String, FunctionType> map = new HashMap<String, FunctionType>();

	private final String name;

	static {
		for (FunctionType functionType : FunctionType.values()) {
			map.put(functionType.getName(), functionType);
		}
	}

	private FunctionType(String name) {
		this.name = name;
	}

	public static FunctionType ofName(String name) {
		return map.get(name);
	}

	public String getName() {
		return name;
	}

}
