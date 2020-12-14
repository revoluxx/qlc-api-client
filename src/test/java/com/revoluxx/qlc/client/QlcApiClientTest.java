package com.revoluxx.qlc.client;

import java.util.List;

import com.revoluxx.qlc.client.data.GetFunctionsListRecord;

public class QlcApiClientTest {

	public static void main(String[] args) throws Exception {
		QlcApiClient qlcClient = QlcApiClient.builder().build();
		qlcClient.connect();
		for (int i = 0; i < 30; i++) {
			try {
				List<GetFunctionsListRecord> result = qlcClient.getFunctionsList();
				if (result != null) {
					System.out.println(result.size());
					/*for (GetFunctionsListRecord gflr : result) {
						System.out.println(gflr);
					}*/
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Thread.sleep(5000);
		}
		qlcClient.close();
	}

}
