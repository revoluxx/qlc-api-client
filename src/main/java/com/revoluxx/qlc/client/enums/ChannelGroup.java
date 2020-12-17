package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelGroup {

	INTENSITY("0"),
	COLOUR("1"),
	GOBO("2"),
	SPEED("3"),
	PAN("4"),
	TILT("5"),
	SHUTTER("6"),
	PRISM("7"),
	BEAM("8"),
	EFFECT("9"),
	MAINTENANCE("10"),
	NOTHING("11"),
	NO_GROUP("2147483647");

	private static final Map<String, ChannelGroup> map = new HashMap<String, ChannelGroup>();

	private final String value;

	static {
		for (ChannelGroup channelGroup : ChannelGroup.values()) {
			map.put(channelGroup.getValue(), channelGroup);
		}
	}

	private ChannelGroup(String value) {
		this.value = value;
	}

	public static ChannelGroup ofValue(String value) {
		return map.get(value);
	}

	public String getValue() {
		return value;
	}

}
