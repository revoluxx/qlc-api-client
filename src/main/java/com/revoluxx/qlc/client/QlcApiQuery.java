package com.revoluxx.qlc.client;

import java.io.Serializable;

import com.revoluxx.qlc.client.data.parser.FunctionStatusResponseParser;
import com.revoluxx.qlc.client.data.parser.FunctionTypeResponseParser;
import com.revoluxx.qlc.client.data.parser.GetChannelsValuesParser;
import com.revoluxx.qlc.client.data.parser.GetElementsListResponseParser;
import com.revoluxx.qlc.client.data.parser.IntegerResponseParser;
import com.revoluxx.qlc.client.data.parser.ResponseParser;
import com.revoluxx.qlc.client.data.parser.StringResponseParser;
import com.revoluxx.qlc.client.enums.CommandCategory;
import com.revoluxx.qlc.client.enums.FunctionStatus;
import com.revoluxx.qlc.client.exception.QlcApiClientException;

/**
 * Query builder for the QlcApiClient.<br>
 * Use in conjunction with executeQuery/executeQueryWithoutResponse.<br>
 * Most of the QLC+ API commands definitions are available from this class by calling her static methods.<br>
 * For original API reference, see: https://qlcplus.org/Test_Web_API.html
 *
 * @param <T> - command response parser type for the query
 * @see QlcApiClient
 */
public class QlcApiQuery<T extends ResponseParser<?>> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final char COMMAND_SEPARATOR = '|';

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

	/**
	 * Retrieve the specified number of DMX values for the given universe, starting at the given address.
	 * 
	 * @param universeIndex - index start from 1
	 * @param startAddress - start DMX address offset
	 * @param channelsCount - max limit of retrieved DMX channels
	 * @return the query to be executed by QlcApiClient.executeQuery
	 * @see com.revoluxx.qlc.client.data.GetChannelsValuesRecord
	 */
	public static QlcApiQuery<GetChannelsValuesParser> getChannelsValues(int universeIndex, int startAddress, int channelsCount) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getChannelsValues");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(Integer.toString(universeIndex), Integer.toString(startAddress), Integer.toString(channelsCount)));
		final GetChannelsValuesParser parser = new GetChannelsValuesParser();
		return new QlcApiQuery<GetChannelsValuesParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * Retrieve the list of functions with their ID and name.
	 * 
	 * @return the query to be executed by QlcApiClient.executeQuery
	 * @see com.revoluxx.qlc.client.data.GetElementsListRecord
	 */
	public static QlcApiQuery<GetElementsListResponseParser> getFunctionsList() {
		final String command = CommandCategory.API.getValue() + "getFunctionsList";
		final String responseHeader = command;
		final GetElementsListResponseParser parser = new GetElementsListResponseParser();
		return new QlcApiQuery<GetElementsListResponseParser>(command, responseHeader, parser);
	}

	/**
	 * Retrieve the number of functions loaded.
	 * 
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<IntegerResponseParser> getFunctionsNumber() {
		final String command = CommandCategory.API.getValue() + "getFunctionsNumber";
		final String responseHeader = command;
		final IntegerResponseParser parser = new IntegerResponseParser();
		return new QlcApiQuery<IntegerResponseParser>(command, responseHeader, parser);
	}

	/**
	 * Retrieve the type of a function with the given ID.
	 * 
	 * @param functionId
	 * @return the query to be executed by QlcApiClient.executeQuery
	 * @see com.revoluxx.qlc.client.enums.FunctionType
	 */
	public static QlcApiQuery<FunctionTypeResponseParser> getFunctionType(String functionId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getFunctionType");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(functionId));
		final FunctionTypeResponseParser parser = new FunctionTypeResponseParser();
		return new QlcApiQuery<FunctionTypeResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * Retrieve the running status of a function with the given ID.
	 * 
	 * @param functionId
	 * @return the query to be executed by QlcApiClient.executeQuery
	 * @see com.revoluxx.qlc.client.enums.FunctionStatus
	 */
	public static QlcApiQuery<FunctionStatusResponseParser> getFunctionStatus(String functionId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getFunctionStatus");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(functionId));
		final FunctionStatusResponseParser parser = new FunctionStatusResponseParser();
		return new QlcApiQuery<FunctionStatusResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * Set the running status of a function with the given ID.
	 * 
	 * @param functionId
	 * @param status
	 * @return the query to be executed by QlcApiClient.executeQueryWithoutResponse
	 * @see com.revoluxx.qlc.client.enums.FunctionStatus
	 */
	public static QlcApiQuery<StringResponseParser> setFunctionStatus(String functionId, FunctionStatus status) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("setFunctionStatus");
		sbCommand.append(formatCommandArgs(functionId, status.getValue()));
		return new QlcApiQuery<StringResponseParser>(sbCommand.toString(), null, new StringResponseParser());
	}

	/**
	 * Retrieve the list of Virtual Console Widgets with their ID and name.
	 * 
	 * @return the query to be executed by QlcApiClient.executeQuery
	 * @see com.revoluxx.qlc.client.data.GetElementsListRecord
	 */
	public static QlcApiQuery<GetElementsListResponseParser> getWidgetsList() {
		final String command = CommandCategory.API.getValue() + "getWidgetsList";
		final String responseHeader = command;
		final GetElementsListResponseParser parser = new GetElementsListResponseParser();
		return new QlcApiQuery<GetElementsListResponseParser>(command, responseHeader, parser);
	}

	/**
	 * Retrieve the number of widgets loaded.
	 * 
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<IntegerResponseParser> getWidgetsNumber() {
		final String command = CommandCategory.API.getValue() + "getWidgetsNumber";
		final String responseHeader = command;
		final IntegerResponseParser parser = new IntegerResponseParser();
		return new QlcApiQuery<IntegerResponseParser>(command, responseHeader, parser);
	}

/* 
 * Inconsistent QLC+ 4.12.3 API implementation of this command... returns localized literal Widget type, impossible mapping with enum...

	public static QlcApiQuery<WidgetTypeResponseParser> getWidgetType(String widgetId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getWidgetType");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(widgetId));
		final WidgetTypeResponseParser parser = new WidgetTypeResponseParser();
		return new QlcApiQuery<WidgetTypeResponseParser>(sbCommand.toString(), responseHeader, parser);
	}
*/

	/**
	 * Retrieve the type of a Virtual Console Widget with the given ID. (raw localized string)
	 * 
	 * @param widgetId
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<StringResponseParser> getWidgetType(String widgetId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getWidgetType");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(widgetId));
		final StringResponseParser parser = new StringResponseParser();
		return new QlcApiQuery<StringResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * Retrieve the status of a Virtual Console Widget with the given ID. (raw QLC internal status representation)
	 * 
	 * @param widgetId
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<StringResponseParser> getWidgetStatus(String widgetId) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("getWidgetStatus");
		final String responseHeader = sbCommand.toString();
		sbCommand.append(formatCommandArgs(widgetId));
		final StringResponseParser parser = new StringResponseParser();
		return new QlcApiQuery<StringResponseParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * This API sets the value of a single channel of the QLC+ Simple Desk.
	 * 
	 * @param universeIndex - index start from 1
	 * @param channelAddress - DMX address of the channel inside the universe (relative)
	 * @param channelDmxValue - the DMX value to set to the channel (value range: 0-255)
	 * @return the query to be executed by QlcApiClient.executeQueryWithoutResponse
	 * @throws QlcApiClientException if DMX value is incorrect
	 */
	public static QlcApiQuery<StringResponseParser> setChannelValue(int universeIndex, int channelAddress, int channelDmxValue) throws QlcApiClientException {
		checkDmxValue(channelDmxValue);
		final int absoluteChannelAddress = channelAddress + ((universeIndex - 1) * 512);
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.CHANNEL.getValue());
		sbCommand.append(formatCommandArgs(Integer.toString(absoluteChannelAddress), Integer.toString(channelDmxValue)));
		return new QlcApiQuery<StringResponseParser>(sbCommand.toString(), null, new StringResponseParser());
	}

	/**
	 * This API is the direct way to set a Virtual Console basic (binary) widget value.<br>
	 * This command is applicable for widgets of type:<br>
	 * Button, AudioTriggers
	 * 
	 * @param widgetId
	 * @param value - binary value (usually true=on/false=off)
	 * @return the query to be executed by QlcApiClient.executeQueryWithoutResponse
	 */
	public static QlcApiQuery<StringResponseParser> setBasicWidgetValue(String widgetId, boolean value) {
		final String commandValue = value ? "255" : "0";
		String command = formatCommandArgs(widgetId, commandValue);
		command = command.substring(1);
		return new QlcApiQuery<StringResponseParser>(command, null, new StringResponseParser());
	}

	/**
	 * Undocumented QLC+ API: Reset the selected channel address from the current universe of the QLC+ Simple Desk to its inital value.
	 * Then returns some information about current page Simple Desk channels..
	 * 
	 * @param channelAddress - relative to current Simple Desk universe
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<GetChannelsValuesParser> resetChannel(int channelAddress) {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("sdResetChannel");
		sbCommand.append(formatCommandArgs(Integer.toString(channelAddress)));
		final String responseHeader = CommandCategory.API.getValue() + "getChannelsValues";
		final GetChannelsValuesParser parser = new GetChannelsValuesParser();
		return new QlcApiQuery<GetChannelsValuesParser>(sbCommand.toString(), responseHeader, parser);
	}

	/**
	 * Undocumented QLC+ API: Reset the whole channels of the current Simple Desk universe to their inital values.
	 * Then returns some information about the current Simple Desk universe channels..
	 * 
	 * @param channelAddress - relative to current Simple Desk universe
	 * @return the query to be executed by QlcApiClient.executeQuery
	 */
	public static QlcApiQuery<GetChannelsValuesParser> resetUniverse() {
		final StringBuilder sbCommand = new StringBuilder(CommandCategory.API.getValue());
		sbCommand.append("sdResetUniverse");
		final String responseHeader = CommandCategory.API.getValue() + "getChannelsValues";
		final GetChannelsValuesParser parser = new GetChannelsValuesParser();
		return new QlcApiQuery<GetChannelsValuesParser>(sbCommand.toString(), responseHeader, parser);
	}

	protected static String formatCommandArgs(final String... commandArgs) {
		final StringBuilder sb = new StringBuilder();
		for (final String commandArg : commandArgs) {
			sb.append(CommandCategory.COMMAND_SEPARATOR).append(commandArg);
		}
		return sb.toString();
	}

	protected static void checkDmxValue(int dmxValue) throws QlcApiClientException {
		if (dmxValue < 0 || dmxValue > 255) {
			throw new QlcApiClientException("DMX value must be in range 0-255");
		}
	}

}
