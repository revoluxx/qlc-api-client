package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum CommandCategory {

	API("QLC+API|"),
	AUTH("QLC+AUTH|"),
	CMD("QLC+CMD|"),
	IO("QLC+IO|"),
	SYS("QLC+SYS|"),
	CHANNEL("CH"),
	HIGH_RATE(""),
	POLL("POLL");

	public static final char COMMAND_SEPARATOR = '|';

	private static final Map<String, CommandCategory> map = new HashMap<String, CommandCategory>();

	private final String value;

	static {
		for (CommandCategory commandCategory : CommandCategory.values()) {
			map.put(commandCategory.getValue(), commandCategory);
		}
	}

	private CommandCategory(String value) {
		this.value = value;
	}

	public static CommandCategory ofValue(String value) {
		return map.get(value);
	}

	public String getValue() {
		return value;
	}

}
