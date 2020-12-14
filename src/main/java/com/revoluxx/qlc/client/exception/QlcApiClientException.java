package com.revoluxx.qlc.client.exception;

public class QlcApiClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public QlcApiClientException() {
        super();
    }

    public QlcApiClientException(String message) {
        super(message);
    }

    public QlcApiClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public QlcApiClientException(Throwable cause) {
        super(cause);
    }
	
}
