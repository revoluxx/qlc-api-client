package com.revoluxx.qlc.client.data.parser;

import com.revoluxx.qlc.client.enums.FunctionStatus;

public class FunctionStatusResponseParser implements ResponseParser<FunctionStatus> {

	@Override
	public FunctionStatus parseResponse(String responseBody, String responseHeader) {
		FunctionStatus response = null;
		if (responseBody != null) {
			response = FunctionStatus.ofName(responseBody);
		}
		return response;
	}

}
