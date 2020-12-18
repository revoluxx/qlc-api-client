package com.revoluxx.qlc.client;

import java.util.List;
import java.util.Random;

import com.revoluxx.qlc.client.data.GetChannelsValuesRecord;
import com.revoluxx.qlc.client.data.GetElementsListRecord;
import com.revoluxx.qlc.client.enums.FunctionStatus;

public class QlcApiClientTest {

	public static void main(String[] args) throws Exception {
		QlcApiClient qlcClient = QlcApiClient.builder().build();
		qlcClient.connect();
		for (int i = 0; i < 30; i++) {
			try {
				System.out.println("=== FUNCTIONS ===");
				List<GetElementsListRecord> result = qlcClient.executeQuery(QlcApiQuery.getFunctionsList());
				if (result != null) {
					System.out.println(result.size());
					System.out.println(qlcClient.executeQuery(QlcApiQuery.getFunctionsNumber()));
					if (!result.isEmpty()) {
						int randF = new Random().nextInt(result.size());
						System.out.println(result.get(randF));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getFunctionType(result.get(randF).getId())));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getFunctionStatus(result.get(randF).getId())));
						Thread.sleep(350);
						qlcClient.executeQueryWithoutResponse(QlcApiQuery.setFunctionStatus(result.get(randF).getId(), FunctionStatus.RUNNING));
					}
				}
				
				System.out.println("=== CHANNELS ===");
				List<GetChannelsValuesRecord> channelsList = qlcClient.executeQuery(QlcApiQuery.getChannelsValues(1, 3, 12));
				if (!channelsList.isEmpty()) {
					int randC = new Random().nextInt(channelsList.size());
					System.out.println(channelsList.size());
					System.out.println(channelsList.get(randC));
					Thread.sleep(350);
					qlcClient.executeQueryWithoutResponse(QlcApiQuery.setChannelValue(1, channelsList.get(randC).getIndex(), 88));
				}
				
				System.out.println("=== WIDGETS ===");
				List<GetElementsListRecord> resultW = qlcClient.executeQuery(QlcApiQuery.getWidgetsList());
				if (resultW != null) {
					System.out.println(resultW.size());
					System.out.println(qlcClient.executeQuery(QlcApiQuery.getWidgetsNumber()));
					if (!resultW.isEmpty()) {
						int randF = new Random().nextInt(resultW.size());
						System.out.println(resultW.get(randF));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getWidgetType(resultW.get(randF).getId())));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getWidgetStatus(resultW.get(randF).getId())));
					}
				}
				
				System.out.println("=== RESET ===");
				Thread.sleep(5000);
				List<GetChannelsValuesRecord> resetresult = qlcClient.executeQuery(QlcApiQuery.resetUniverse());
				System.out.println(resetresult.size());
				Thread.sleep(10000);
				List<GetChannelsValuesRecord> resetChresult = qlcClient.executeQuery(QlcApiQuery.resetChannel(12));
				System.out.println(resetChresult.size());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Thread.sleep(10000);
		}
		qlcClient.close();
	}
	
}
