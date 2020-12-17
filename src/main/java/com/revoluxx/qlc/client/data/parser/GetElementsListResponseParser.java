package com.revoluxx.qlc.client.data.parser;

import java.util.ArrayList;
import java.util.List;

import com.revoluxx.qlc.client.data.GetElementsListRecord;
import com.revoluxx.qlc.client.enums.CommandCategory;

public class GetElementsListResponseParser implements ResponseParser<List<GetElementsListRecord>> {

	@Override
	public List<GetElementsListRecord> parseResponse(String responseBody, String responseHeader) {
		List<GetElementsListRecord> result = null;
		if (!responseBody.isEmpty()) {
			if (responseBody.charAt(responseBody.length()-1) == CommandCategory.COMMAND_SEPARATOR) {
				responseBody = responseBody.substring(0, responseBody.length()-1);
			}
			final String[] splitedData = responseBody.split("\\" + CommandCategory.COMMAND_SEPARATOR);
			if (splitedData.length >= 2 && (splitedData.length % 2) == 0) {
				result = new ArrayList<GetElementsListRecord>();
				int i = 0;
				while (i < splitedData.length) {
					result.add(new GetElementsListRecord(splitedData[i++], splitedData[i++]));
				}
			}
		}
		return result;
	}

}
