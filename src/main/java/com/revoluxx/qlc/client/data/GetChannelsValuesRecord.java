package com.revoluxx.qlc.client.data;

import java.io.Serializable;

import com.revoluxx.qlc.client.enums.ChannelGroup;

public class GetChannelsValuesRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer index;
	private Integer value;
	private ChannelGroup type;
	private String hexColor;

	public GetChannelsValuesRecord() {
		
	}

	public GetChannelsValuesRecord(Integer index, Integer value, ChannelGroup type, String hexColor) {
		this.index = index;
		this.value = value;
		this.type = type;
		this.hexColor = hexColor;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public ChannelGroup getType() {
		return type;
	}

	public void setType(ChannelGroup type) {
		this.type = type;
	}

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}

	@Override
	public String toString() {
		return "GetChannelsValuesRecord [index=" + index + ", value=" + value + ", type=" + type + ", hexColor="
				+ hexColor + "]";
	}

}
