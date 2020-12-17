package com.revoluxx.qlc.client.data.parser;

import com.revoluxx.qlc.client.enums.FunctionType;

public class FunctionTypeResponseParser implements ResponseParser<FunctionType> {

	@Override
	public FunctionType parseResponse(String responseBody, String responseHeader) {
		FunctionType response = null;
		if (responseBody != null) {
			response = FunctionType.ofName(responseBody);
		}
		return response;
	}

}
