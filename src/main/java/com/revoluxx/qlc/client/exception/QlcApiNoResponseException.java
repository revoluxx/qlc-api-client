package com.revoluxx.qlc.client.exception;

public class QlcApiNoResponseException extends Exception {

	private static final long serialVersionUID = 1L;

	public QlcApiNoResponseException() {
        super();
    }

    public QlcApiNoResponseException(String message) {
        super(message);
    }

    public QlcApiNoResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public QlcApiNoResponseException(Throwable cause) {
        super(cause);
    }
	
}
