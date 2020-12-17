package com.revoluxx.qlc.client.data.parser;

import java.util.ArrayList;
import java.util.List;

import com.revoluxx.qlc.client.data.GetFunctionsListRecord;
import com.revoluxx.qlc.client.enums.CommandCategory;

public class GetFunctionsListParser implements ResponseParser<List<GetFunctionsListRecord>> {

	@Override
	public List<GetFunctionsListRecord> parseResponse(String responseBody, String responseHeader) {
		List<GetFunctionsListRecord> result = null;
		if (!responseBody.isEmpty()) {
			final String[] splitedData = responseBody.split("\\" + CommandCategory.COMMAND_SEPARATOR);
			if (splitedData.length >= 2 && (splitedData.length % 2) == 0) {
				result = new ArrayList<GetFunctionsListRecord>();
				int i = 0;
				while (i < splitedData.length) {
					result.add(new GetFunctionsListRecord(splitedData[i++], splitedData[i++]));
				}
			}
		}
		return result;
	}

}
