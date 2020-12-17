package com.revoluxx.qlc.client;

import java.util.List;
import java.util.Random;

import com.revoluxx.qlc.client.data.GetFunctionsListRecord;

public class QlcApiClientTest {

	public static void main(String[] args) throws Exception {
		QlcApiClient qlcClient = QlcApiClient.builder().build();
		qlcClient.connect();
		for (int i = 0; i < 30; i++) {
			try {
				List<GetFunctionsListRecord> result = qlcClient.executeQuery(QlcApiQuery.getFunctionsList());
				if (result != null) {
					System.out.println(result.size());
					if (!result.isEmpty()) {
						int randF = new Random().nextInt(result.size());
						System.out.println(result.get(randF));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getFunctionType(result.get(randF).getId())));
						System.out.println(qlcClient.executeQuery(QlcApiQuery.getFunctionStatus(result.get(randF).getId())));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Thread.sleep(5000);
		}
		qlcClient.close();
	}
	
}
