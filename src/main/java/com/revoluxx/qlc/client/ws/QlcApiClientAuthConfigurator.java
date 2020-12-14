package com.revoluxx.qlc.client.ws;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig.Configurator;

public class QlcApiClientAuthConfigurator extends Configurator {

	private final String authUserName;
	private final String authPassword;

	public QlcApiClientAuthConfigurator(String authUserName, String authPassword) {
		this.authUserName = authUserName;
		this.authPassword = authPassword;
	}

	@Override
	public void beforeRequest(Map<String, List<String>> headers) {
		final String authCredentials = authUserName + ":" + authPassword;
		String authCredentialsB64 = "";
		try {
			authCredentialsB64 = new String(Base64.getEncoder().encode(authCredentials.getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		final List<String> authHeadervalue = new ArrayList<String>();
		authHeadervalue.add("Basic " + authCredentialsB64);
		headers.put("Authorization", authHeadervalue);
	}

}
