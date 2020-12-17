package com.revoluxx.qlc.client.enums;

import java.util.HashMap;
import java.util.Map;

public enum WidgetType {

	UNKNOWN("0"),
	BUTTON("1"),
	SLIDER("2"),
	XYPAD("3"),
	FRAME("4"),
	SOLOFRAME("5"),
	SPEEDDIAL("6"),
	CUELIST("7"),
	LABEL("8"),
	AUDIOTRIGGERS("9"),
	ANIMATION("10"),
	CLOCK("11");

	private static final Map<String, WidgetType> map = new HashMap<String, WidgetType>();

	private final String value;

	static {
		for (WidgetType widgetType : WidgetType.values()) {
			map.put(widgetType.getValue(), widgetType);
		}
	}

	private WidgetType(String value) {
		this.value = value;
	}

	public static WidgetType ofValue(String value) {
		return map.get(value);
	}

	public String getValue() {
		return value;
	}

}
