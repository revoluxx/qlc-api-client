package com.revoluxx.qlc.client.data.parser;

import java.util.ArrayList;
import java.util.List;

import com.revoluxx.qlc.client.data.GetChannelsValuesRecord;
import com.revoluxx.qlc.client.enums.ChannelGroup;
import com.revoluxx.qlc.client.enums.CommandCategory;

public class GetChannelsValuesParser implements ResponseParser<List<GetChannelsValuesRecord>> {

	@Override
	public List<GetChannelsValuesRecord> parseResponse(String responseBody, String responseHeader) {
		List<GetChannelsValuesRecord> result = new ArrayList<GetChannelsValuesRecord>();
		if (!responseBody.isEmpty()) {
			final String[] splitedData = responseBody.split("\\" + CommandCategory.COMMAND_SEPARATOR);
			if (splitedData.length >= 3 && (splitedData.length % 3) == 0) {
				int i = 0;
				while (i < splitedData.length) {
					final GetChannelsValuesRecord record = new GetChannelsValuesRecord();
					record.setIndex(Integer.parseInt(splitedData[i++]));
					record.setValue(Integer.parseInt(splitedData[i++]));
					final String[] splitedTypeData = splitedData[i++].split("\\.");
					record.setType(ChannelGroup.ofValue(splitedTypeData[0]));
					if (splitedTypeData.length > 1) {
						record.setHexColor(splitedTypeData[1]);
					}
					result.add(record);
				}
			}
		}
		return result;
	}

}
