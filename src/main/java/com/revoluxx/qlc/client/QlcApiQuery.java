package com.revoluxx.qlc.client;

import java.io.Serializable;

import com.revoluxx.qlc.client.data.parser.FunctionStatusResponseParser;
import com.revoluxx.qlc.client.data.parser.FunctionTypeResponseParser;
import com.revoluxx.qlc.client.data.parser.GetFunctionsListParser;
import com.revoluxx.qlc.client.data.parser.ResponseParser;
import com.revoluxx.qlc.client.enums.CommandCategory;

public class QlcApiQuery<T extends ResponseParser<?>> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static char COMMAND_SEPARATOR = '|';

	protected final String command;
	protected final String responseHeader;
	protected final T responseParser;

	protected QlcApiQuery(String command, String responseHeader, T responseParser) {
		this.command = command;
		this.responseHeader = responseHeader;
		this.responseParser = responseParser;
	}

	public String getCommand() {
		return command;
	}

	public String getResponseHeader() {
		return responseHeader;
	}

	public T getResponseParser() {
		return responseParser;
	}

	public static QlcApiQuery<GetFunctionsListParser> getFunctionsList() {
		final String command = CommandCategory.API.getValue() + "getFunctionsList";
		final String responseHeader = command;
		final GetFunctionsListParser parser = new GetFunctionsListParser();
		return new QlcApiQuery<GetFunctionsListParser>(command, responseHeader, parser);
	}

	public static QlcApiQuery<FunctionTypeResponseParser> getFunctionType(String functionId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getFunctionType");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(functionId));
		final FunctionTypeResponseParser parser = new FunctionTypeResponseParser();
		return new QlcApiQuery<FunctionTypeResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	public static QlcApiQuery<FunctionStatusResponseParser> getFunctionStatus(String functionId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getFunctionStatus");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(functionId));
		final FunctionStatusResponseParser parser = new FunctionStatusResponseParser();
		return new QlcApiQuery<FunctionStatusResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	protected static String formatCommandArgs(final String... commandArgs) {
		final StringBuilder sb = new StringBuilder();
		for (final String commandArg : commandArgs) {
			sb.append(CommandCategory.COMMAND_SEPARATOR).append(commandArg);
		}
		return sb.toString();
	}

}
