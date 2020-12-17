package com.revoluxx.qlc.client;

import java.io.Serializable;

import com.revoluxx.qlc.client.data.parser.FunctionStatusResponseParser;
import com.revoluxx.qlc.client.data.parser.FunctionTypeResponseParser;
import com.revoluxx.qlc.client.data.parser.GetChannelsValuesParser;
import com.revoluxx.qlc.client.data.parser.GetElementsListResponseParser;
import com.revoluxx.qlc.client.data.parser.IntegerResponseParser;
import com.revoluxx.qlc.client.data.parser.ResponseParser;
import com.revoluxx.qlc.client.data.parser.StringResponseParser;
import com.revoluxx.qlc.client.data.parser.WidgetTypeResponseParser;
import com.revoluxx.qlc.client.enums.CommandCategory;
import com.revoluxx.qlc.client.enums.FunctionStatus;

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

	public static QlcApiQuery<GetChannelsValuesParser> getChannelsValues(int universeIndex, int startAddress, int channelsCount) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getChannelsValues");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(Integer.toString(universeIndex), Integer.toString(startAddress), Integer.toString(channelsCount)));
		final GetChannelsValuesParser parser = new GetChannelsValuesParser();
		return new QlcApiQuery<GetChannelsValuesParser>(sbCommand.toString(), responseHeader, parser);
	}

	public static QlcApiQuery<GetElementsListResponseParser> getFunctionsList() {
		final String command = CommandCategory.API.getValue() + "getFunctionsList";
		final String responseHeader = command;
		final GetElementsListResponseParser parser = new GetElementsListResponseParser();
		return new QlcApiQuery<GetElementsListResponseParser>(command, responseHeader, parser);
	}

	public static QlcApiQuery<IntegerResponseParser> getFunctionsNumber() {
		final String command = CommandCategory.API.getValue() + "getFunctionsNumber";
		final String responseHeader = command;
		final IntegerResponseParser parser = new IntegerResponseParser();
		return new QlcApiQuery<IntegerResponseParser>(command, responseHeader, parser);
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

	public static QlcApiQuery<ResponseParser<?>> setFunctionStatus(String functionId, FunctionStatus status) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("setFunctionStatus");
		sbCommand.append(formatCommandArgs(functionId, status.getValue()));
		return new QlcApiQuery<ResponseParser<?>>(sbCommand.toString(), null, null);
	}

	public static QlcApiQuery<GetElementsListResponseParser> getWidgetsList() {
		final String command = CommandCategory.API.getValue() + "getWidgetsList";
		final String responseHeader = command;
		final GetElementsListResponseParser parser = new GetElementsListResponseParser();
		return new QlcApiQuery<GetElementsListResponseParser>(command, responseHeader, parser);
	}

	public static QlcApiQuery<IntegerResponseParser> getWidgetsNumber() {
		final String command = CommandCategory.API.getValue() + "getWidgetsNumber";
		final String responseHeader = command;
		final IntegerResponseParser parser = new IntegerResponseParser();
		return new QlcApiQuery<IntegerResponseParser>(command, responseHeader, parser);
	}

	public static QlcApiQuery<WidgetTypeResponseParser> getWidgetType(String widgetId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getWidgetType");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(widgetId));
		final WidgetTypeResponseParser parser = new WidgetTypeResponseParser();
		return new QlcApiQuery<WidgetTypeResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	public static QlcApiQuery<StringResponseParser> getWidgetStatus(String widgetId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getWidgetStatus");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(widgetId));
		final StringResponseParser parser = new StringResponseParser();
		return new QlcApiQuery<StringResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	public static QlcApiQuery<ResponseParser<?>> setChannelValue(int universeIndex, int channelAddress, int channelDmxValue) {
		final int absoluteChannelAddress = channelAddress + ((universeIndex - 1) * 512);
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.CHANNEL.getValue());
		sbCommand.append(formatCommandArgs(Integer.toString(absoluteChannelAddress), Integer.toString(channelDmxValue)));
		return new QlcApiQuery<ResponseParser<?>>(sbCommand.toString(), null, null);
	}

	protected static String formatCommandArgs(final String... commandArgs) {
		final StringBuilder sb = new StringBuilder();
		for (final String commandArg : commandArgs) {
			sb.append(CommandCategory.COMMAND_SEPARATOR).append(commandArg);
		}
		return sb.toString();
	}

}
