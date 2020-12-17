package com.revoluxx.qlc.client.data.parser;

public class StringResponseParser implements ResponseParser<String> {

	@Override
	public String parseResponse(String responseBody, String responseHeader) {
		return responseBody;
	}

}
