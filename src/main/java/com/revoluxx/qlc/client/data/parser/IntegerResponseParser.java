package com.revoluxx.qlc.client.data.parser;

public class IntegerResponseParser implements ResponseParser<Integer> {

	@Override
	public Integer parseResponse(String responseBody, String responseHeader) {
		Integer responseData = null;
		if (!responseBody.isEmpty()) {
			try {
				responseData = Integer.parseInt(responseBody);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		return responseData;
	}

}
