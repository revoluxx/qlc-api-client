package com.revoluxx.qlc.client.data.parser;

import com.revoluxx.qlc.client.enums.WidgetType;

public class WidgetTypeResponseParser implements ResponseParser<WidgetType> {

	@Override
	public WidgetType parseResponse(String responseBody, String responseHeader) {
		WidgetType response = null;
		if (responseBody != null) {
			response = WidgetType.ofValue(responseBody);
		}
		return response;
	}

}
