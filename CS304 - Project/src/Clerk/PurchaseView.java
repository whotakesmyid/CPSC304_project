package Clerk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

/**
 * This class handles all the UI and UI-related functions related to a purchase
 */

public class PurchaseView extends JPanel {
	
	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private JButton processSaleButton, addItemButton, removeButton, cancelButton;
	private JTextField upcField, qtyField, cardNumber;
	private JList<String> itemsList;
	private JScrollPane scrollList;
	private DefaultListModel<String> itemsDList;
	private JLabel upcLb, qtyLb, cardLb, cashLb, expiryDateLb, totalPriceLb, itemDescipLb;
	private JSpinner cardExpiryDate;
	private JRadioButton cashPurchase, cardPurchase;
	private ButtonGroup purchaseTypes;
	private NumberFormat formatter;
	private JPanel leftPanel, rightPanel, addItemPanel, paymentPanel, transactionPanel;
	private JPanel itemListPanel, itemControlPanel;
	
	public PurchaseView() {
		formatter = NumberFormat.getCurrencyInstance();	// allows numbers to be in currency format
		
		// create all the panels
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		addItemPanel = new JPanel();
		paymentPanel = new JPanel();
		transactionPanel = new JPanel();
		itemListPanel = new JPanel();
		itemControlPanel = new JPanel();
		
		// set the layouts for all the panels
		leftPanel.setLayout(new GridLayout(3,0));
		rightPanel.setLayout(new BorderLayout(10,10));
		addItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		paymentPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		transactionPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
		itemListPanel.setLayout(new BorderLayout(10,10));
		itemControlPanel.setLayout(new BorderLayout(10,10));
		
		// add panels part of left panel
		leftPanel.add(addItemPanel);
		leftPanel.add(paymentPanel);
		leftPanel.add(transactionPanel);
		
		// add panels part of right panel
		rightPanel.add(itemListPanel, BorderLayout.CENTER);
		rightPanel.add(itemControlPanel, BorderLayout.SOUTH);
		
		// set style and panels to main panel
		this.setLayout(new BorderLayout(10,10));
		this.add(leftPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.LINE_END);
		
		// create all labels
		qtyLb = new JLabel("QTY:");
		upcLb = new JLabel("UPC:");
		cashLb = new JLabel("Cash:");
		cardLb = new JLabel("Card:");
		expiryDateLb = new JLabel("EXP:"); // expiry date
		itemDescipLb = new JLabel("Item List: (UPC, Title, QTY, Price)"); // item list description
		totalPriceLb = new JLabel("Total: $0.00");

		// create all text fields
		qtyField = new JTextField(3);
		upcField = new JTextField(12);
		cardNumber = new JTextField(10);

		// create all buttons
		addItemButton = new JButton("Add Item");
		processSaleButton = new JButton("Process Sale");
		cancelButton = new JButton("Cancel Transaction");
		removeButton = new JButton("Remove Item");

		// payment radio buttons
		cashPurchase = new JRadioButton();
		cardPurchase = new JRadioButton();
		purchaseTypes = new ButtonGroup();
		purchaseTypes.add(cashPurchase);
		purchaseTypes.add(cardPurchase);
		cashPurchase.setSelected(true);

		// create spinner for expiry date
		cardExpiryDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MONTH));
		cardExpiryDate.setEditor(new JSpinner.DateEditor(cardExpiryDate, "MM/yy"));

		// add all components to addItemPanel
		addItemPanel.add(qtyLb);
		addItemPanel.add(qtyField);	
		addItemPanel.add(upcLb);
		addItemPanel.add(upcField);
		addItemPanel.add(addItemButton);
		
		// add all components to paymentPanel
		paymentPanel.add(cashLb);
		paymentPanel.add(cashPurchase);
		paymentPanel.add(cardLb);
		paymentPanel.add(cardPurchase);	
		paymentPanel.add(cardNumber);
		paymentPanel.add(expiryDateLb);
		paymentPanel.add(cardExpiryDate);
		
		// add all components to transactionPanel
		transactionPanel.add(processSaleButton);	
		transactionPanel.add(cancelButton);
		 		
		// create items list related UI
		itemListPanel.add(itemDescipLb, BorderLayout.NORTH);
		itemsDList = new DefaultListModel<String>();
		itemsList = new JList<String>(itemsDList);
		itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollList = new JScrollPane(itemsList, 
				 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		scrollList.setSize(300, 400);
		 
		itemListPanel.add(scrollList, BorderLayout.CENTER);
		 
		itemControlPanel.add(totalPriceLb, BorderLayout.NORTH);	 		 
		itemControlPanel.add(removeButton, BorderLayout.CENTER);

	}
	
	/* It's a good practice to let the view controller handle the actions (MVC): */
	public void addActionToButton(ActionListener listener) {
		processSaleButton.addActionListener(listener);
	}
	
	public void addActionToAddButton(ActionListener listener) {
		addItemButton.addActionListener(listener);
	}
	
	public void addActionToRemoveButton(ActionListener listener) {
		removeButton.addActionListener(listener);
	}
	
	public void addActionToCancelButton(ActionListener listener) {
		cancelButton.addActionListener(listener);
	}
	
	public void addActionToCardRadio(ActionListener listener) {
		cardPurchase.addActionListener(listener);
	}
	
	public void addActionToCashRadio(ActionListener listener) {
		cashPurchase.addActionListener(listener);
	}
	
	/* Returns the text in the UPC field */
	public String getUPCText() {
		return upcField.getText();
	}
	
	/* Returns the text in the quantity field */
	public String getQtyText() {
		return qtyField.getText();
	}
	
	/* Adds an item to the sale list */
	public void addToList(String itemInfo) {
		itemsDList.addElement(itemInfo);
	}
	
	/* Returns an array of the sale List items */
	public Object[] retrieveSaleList() {
		Object[] salesList = itemsDList.toArray();
		return salesList;
	}
	
	/* Returns the text on the credit card field */
	public String getCreditCard() {
		return cardNumber.getText();
	}
	
	/* Returns the date from the expiry date spinner */
	public String getExpiryDate() {
		DateFormat df = new SimpleDateFormat("MM-yy");  
		return df.format((Date) cardExpiryDate.getValue()); 
	}
	
	/* Sets the credit card payment related UI to visible */
	public void setCardPurchaseVisible() {
		cardNumber.setVisible(true);
		cardExpiryDate.setVisible(true);
		expiryDateLb.setVisible(true);
	}
	
	/* Sets the credit card payment related UI to not visible */
	public void setCardPurchaseNotVisible() {
		cardNumber.setVisible(false);
		cardExpiryDate.setVisible(false);
		expiryDateLb.setVisible(false);
	}
	
	/* Clears all fields */
	public void clearTransaction() {
		itemsDList.removeAllElements();
		qtyField.setText("");
		upcField.setText("");
		cardNumber.setText("");
	}
	
	/* Updates the sale total */
	public float updateTotal(float price, float oldTotalPrice) {
		price = price + oldTotalPrice;
		totalPriceLb.setText("Total: " + formatter.format(price));
		return price;
	}

	/**
	 * Checks if an item is already added into the items list
	 * @param upc: the item added
	 * @return the index of the existing item or -1 if there are no duplicates
	 */
	public int upcDuplicate(String upc) {
		Object[] salesList = itemsDList.toArray();
		
		for (int i = 0; i < itemsDList.getSize(); i++) {
			if (salesList[i].toString().contains(upc)) {
				return i; // index of the duplicate
			}
		}
		return -1; // there are no duplicates
	}
	
	/**
	 * pdates the sales list information at the specified index
	 * @param upc: upc of the item
	 * @param newQty: new quantity to be added to the list
	 * @param title: title of the item
	 * @param price: price of the item (each)
	 * @param index: index in the sales list where an update needs to occur
	 */
	public void updateList(int upc, int newQty, String title, float price, int index) {
		String updatedString = upc + ", " + title + ", " + newQty + ", " + formatter.format(price*newQty);
		itemsDList.setElementAt(updatedString, index);
	}
	
	/**
	 * Returns the new quantity for the item in the sales list
	 * @param index: the index of the sales list where the item is located
	 * @param qty: the new quantity being added to the information
	 * @return the new quantity + the old quantity
	 */
	public int newQuantity(int index, int qty) {
		String oldValue = itemsDList.getElementAt(index); // retrieve the item at specified index
		String[] oldValues = oldValue.split(", "); // separate the information into an array
		int oldQty = Integer.parseInt(oldValues[2]); // index 2 contains the old quantity value
		int newQty = oldQty+qty;
		return newQty;
	}
		
	/**
	 * Returns if a payment is made by a credit card or not
	 * @return true: credit payment is selected
	 *         false: cash payment is selected
	 */
	public boolean payByCard() {
		if (cardPurchase.isSelected()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the selected item and returns the total price of the items removed
	 * @return total price of items removed, -1 if it fails
	 */
	public float removeItems() {
		try {
			int index = itemsList.getSelectedIndex();
			
			String value = itemsDList.getElementAt(index);
			String[] valueAtIndex = value.split(", ");
			String priceFormatted = valueAtIndex[3]; // index 3 is the formatted price
			priceFormatted = priceFormatted.replace("$", ""); // undo-ing the currency format
			priceFormatted = priceFormatted.replace(",", "");
			priceFormatted = priceFormatted.trim();
			float price = Float.parseFloat(priceFormatted);
			
			itemsDList.remove(index);
			return price;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Please select which item to remove.", 
			"Missing Item Selection", JOptionPane.ERROR_MESSAGE);
		}
		return -1;
	}
	
	/* Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
