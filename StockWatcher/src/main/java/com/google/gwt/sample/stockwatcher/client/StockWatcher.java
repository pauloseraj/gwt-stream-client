package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class StockWatcher implements EntryPoint {

	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();

	protected static native void _log(String text)
	/*-{
		console.log(text);
	}-*/;

	protected class Console {
		public void log(String text) {
			_log(text);
		}
	}

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		final Label outputLabel = new Label();

		final Element output = DOM.getElementById("output");
		final Element status = DOM.getElementById("status");

		final Console console = new Console();

		console.log("adding websocket");

		// Establish a websocket communication channel to the atmosphere chat service.
		// Websocket socket = new
		// Websocket("ws://localhost:8080/chat?X-Atmosphere-tracking-id=5ebed4c5-0b90-4166-88b2-9f273719ab75&X-Atmosphere-Framework=2.2.1-jquery&X-Atmosphere-Transport=websocket&Content-Type=application/json&X-atmo-protocol=true");
		final String url = "ws://localhost:8080/stream";
		Websocket socket = new Websocket(url);

		socket.addListener(new WebsocketListener() {

			@Override
			public void onClose() {
				// do something on close
			}

			@Override
			public void onMessage(String msg) {
				// a message is received
				console.log("onMessage(): " + msg);
				outputLabel.setText("websocket " + url + ": " + msg);
				output.setInnerText(msg);
			}

			@Override
			public void onOpen() {
				// do something on open
				console.log("onOpen()");
				status.setInnerText("connected: " + url);
			}
		});

		socket.open();

		console.log("websocket is open");
	}
}

/*
 * // Create table for stock data. stocksFlexTable.setText(0, 0, "Symbol");
 * stocksFlexTable.setText(0, 1, "Price"); stocksFlexTable.setText(0, 2,
 * "Change"); stocksFlexTable.setText(0, 3, "Remove");
 * 
 * // Assemble Add Stock panel. addPanel.add(newSymbolTextBox);
 * addPanel.add(addStockButton);
 * 
 * // Assemble Main panel. mainPanel.add(stocksFlexTable);
 * mainPanel.add(addPanel); mainPanel.add(lastUpdatedLabel);
 * 
 * // Associate the Main panel with the HTML host page.
 * RootPanel.get("stockList").add(mainPanel);
 * 
 * // Move cursor focus to the input box. newSymbolTextBox.setFocus(true);
 * 
 * // Setup timer to refresh list automatically. Timer refreshTimer = new
 * Timer() {
 * 
 * @Override public void run() { refreshWatchList(); }
 * 
 *//**
	 * Generate random stock prices.
	 */
/*
 * private void refreshWatchList() { final double MAX_PRICE = 100.0; // $100.00
 * final double MAX_PRICE_CHANGE = 0.02; // +/- 2%
 * 
 * StockPrice[] prices = new StockPrice[stocks.size()]; for (int i = 0; i <
 * stocks.size(); i++) { double price = Random.nextDouble() * MAX_PRICE; double
 * change = price * MAX_PRICE_CHANGE * (Random.nextDouble() * 2.0 - 1.0);
 * 
 * prices[i] = new StockPrice(stocks.get(i), price, change); }
 * 
 * updateTable(prices); }
 * 
 *//**
	 * Update the Price and Change fields all the rows in the stock table.
	 *
	 * @param prices Stock data for all rows.
	 */
/*
 * private void updateTable(StockPrice[] prices) { for (int i = 0; i <
 * prices.length; i++) { updateTable(prices[i]); }
 * 
 * // Display timestamp showing last refresh. DateTimeFormat dateFormat =
 * DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
 * lastUpdatedLabel.setText("Last update : " + dateFormat.format(new Date())); }
 * 
 *//**
	 * Update a single row in the stock table.
	 *
	 * @param price Stock data for a single row.
	 */
/*
 * private void updateTable(StockPrice price) { // Make sure the stock is still
 * in the stock table. if (!stocks.contains(price.getSymbol())) { return; }
 * 
 * int row = stocks.indexOf(price.getSymbol()) + 1;
 * 
 * // Format the data in the Price and Change fields. String priceText =
 * NumberFormat.getFormat("#,##0.00").format(price.getPrice()); NumberFormat
 * changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00"); String
 * changeText = changeFormat.format(price.getChange()); String changePercentText
 * = changeFormat.format(price.getChangePercent());
 * 
 * // Populate the Price and Change fields with new data.
 * stocksFlexTable.setText(row, 1, priceText); stocksFlexTable.setText(row, 2,
 * changeText + " (" + changePercentText + "%)"); } };
 * refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
 * 
 * // Listen for mouse events on the Add button.
 * addStockButton.addClickHandler(new ClickHandler() { public void
 * onClick(ClickEvent event) { addStock(); } });
 * 
 * // Listen for keyboard events in the input box.
 * newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() { public void
 * onKeyDown(KeyDownEvent event) { if (event.getNativeKeyCode() ==
 * KeyCodes.KEY_ENTER) { addStock(); } } }); }
 * 
 *//**
	 * Add stock to FlexTable. Executed when the user clicks the addStockButton or
	 * presses enter in the newSymbolTextBox.
	 *//*
		 * private void addStock() { final String symbol =
		 * newSymbolTextBox.getText().toUpperCase().trim();
		 * newSymbolTextBox.setFocus(true);
		 * 
		 * // Stock code must be between 1 and 10 chars that are numbers, letters, or
		 * dots. if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) { Window.alert("'" + symbol
		 * + "' is not a valid symbol."); newSymbolTextBox.selectAll(); return; }
		 * 
		 * newSymbolTextBox.setText("");
		 * 
		 * // Don't add the stock if it's already in the table. if
		 * (stocks.contains(symbol)) return; // Add the stock to the table. int row =
		 * stocksFlexTable.getRowCount(); stocks.add(symbol);
		 * stocksFlexTable.setText(row, 0, symbol); // Add a button to remove this stock
		 * from the table. Button removeStockButton = new Button("x");
		 * removeStockButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { int removedIndex = stocks.indexOf(symbol);
		 * stocks.remove(removedIndex); stocksFlexTable.removeRow(removedIndex + 1); }
		 * }); stocksFlexTable.setWidget(row, 3, removeStockButton); // TODO Get the
		 * stock price. }
		 */
