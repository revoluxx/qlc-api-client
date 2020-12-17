package com.revoluxx.qlc.client.data.parser;

public interface ResponseParser<T> {

	public T parseResponse(String responseBody, String responseHeader);

}
