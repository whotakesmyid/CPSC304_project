package Clerk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.Date;

/**
 * This class controls the clerk view (eg: the action of the buttons in the view).
 * and interacts with the ClerkModel.
 */

public class ClerkController {
		
	/* The view (to display the results): */
	private ClerkView view;
	
	/* The model (handles the SQL queries): */
	private ClerkModel model;
	
	private static int DOES_NOT_EXIST = -1;
	private NumberFormat formatter;
	private float totalPrice = 0; // current total price of transaction

	public ClerkController(ClerkModel model, ClerkView view) {
		this.view = view;
		this.model = model;
		formatter = NumberFormat.getCurrencyInstance();	// allows numbers to display in a currency format
		setUpView();
	}

	/**
	 * Sets up both the purchase and return view listeners and their behaviour
	 */
	private void setUpView() {		
		view.addActionToPurchaseButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				 view.displayPurView();	// when selected the purchase view UI is shown
			}		
		});

		view.addActionToReturnButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				view.displayRetView(); // when selected the return view UI is shown
			}	
		});
	
		purchaseViewActions(); // retrieves all purchase view actions
		returnViewActions(); // retrieves all return view actions
	}
	
	/**
	 * Holds all the action listeners for the purchase view
	 */
	private void purchaseViewActions() {
		view.pur().addActionToCancelButton(new ActionListener() { // cancels a transaction when clicked

			@Override
			public void actionPerformed(ActionEvent event) {
				view.pur().clearTransaction();
				view.warningPopup("Transaction has been cancelled.");
				totalPrice = 0;
				view.pur().updateTotal(0, 0);
			}	
		});
		
		view.pur().addActionToCardRadio(new ActionListener() { // credit as payment selected
			
			@Override
			public void actionPerformed(ActionEvent event) {
				view.pur().setCardPurchaseVisible(); // shows credit card-related UI				
			}	
		});
		
		view.pur().addActionToCashRadio(new ActionListener() { // cash as payment selected
			
			@Override
			public void actionPerformed(ActionEvent event) {
				view.pur().setCardPurchaseNotVisible(); // hides credit card-related UI
			}	
		});

		view.pur().addActionToAddButton(new ActionListener() { // adds items to list
			
    		@Override
    		public void actionPerformed(ActionEvent event) {   
    			String title = "NO TITLE";
    			float price = 0;
    			
    			try {
    				int upc = Integer.parseInt(view.pur().getUPCText());
    				int qty = Integer.parseInt(view.pur().getQtyText());
    				
    				if (view.pur().getUPCText().length() == 0 || !model.validUPC(upc)) {
    					view.warningPopup("Please enter a valid UPC.");
    					return;
    				}
    				if (view.pur().getQtyText().length() == 0 || qty <= 0) {
    					view.warningPopup("Please enter a quantity greater than zero.");
    					return;
    				}
    				
    				// checks supply: in an actual in-store situation this would likely not 
    				// need to occur since items are physically available (ie. stock exists)
    				if (!model.checkStock(upc, qty)) { 
    					return;
    				}
    				else {
    					title = model.getItemTitle(upc);
        				price = model.getItemPrice(upc);
        			}
    				
    				// checks if the same item has already been added to the list and updates the
    				// quantity of the item accordingly along with the total of the transaction
        			int duplicateIndex = view.pur().upcDuplicate(Integer.toString(upc));
        			if (duplicateIndex == DOES_NOT_EXIST) {
        				view.pur().addToList(upc + ", " + title + ", " + qty + ", " + formatter.format(price*qty));
        				totalPrice = view.pur().updateTotal(price*qty, totalPrice);
        			}
        			else {  	
        				int newQty = view.pur().newQuantity(duplicateIndex, qty);
        				if (!model.checkStock(upc, newQty)) {
        					return;
        				}
        				view.pur().updateList(upc, newQty, title, price, duplicateIndex);
        				totalPrice = view.pur().updateTotal(price*qty, totalPrice);
        			}
    			}
    			catch(NumberFormatException e) {
    				JOptionPane.showMessageDialog(new JFrame(), "Please use numbers only.", 
					"Invalid Input", JOptionPane.ERROR_MESSAGE);
    			} 
            }
        });
		
		view.pur().addActionToRemoveButton(new ActionListener() { // removes an item from the list

			@Override
			public void actionPerformed(ActionEvent event) {
				
				float deductPrice = view.pur().removeItems();
				if (deductPrice == DOES_NOT_EXIST) {
					view.warningPopup("ERROR: Could not remove item.");
					return;
				}
				totalPrice = totalPrice - deductPrice; // updates total sales price
				view.pur().updateTotal(0, totalPrice);
			}	
		});
		
		view.pur().addActionToButton(new ActionListener() { // processes a purchase
			
    		@Override
    		public void actionPerformed(ActionEvent event) {  

    			try {
    				Object[] salesList = view.pur().retrieveSaleList(); // obtain the list of items
    				if (salesList.length == 0) {
    					view.warningPopup("Please add items first.");
    					return;
    				}
    				// if payment by credit is selected
    				if (view.pur().payByCard()) {
    					if (view.pur().getCreditCard().length() == 0) {
    						view.warningPopup("Please enter a credit card number.");
    						return;
    					}
    					if (view.pur().getCreditCard().length() < 10) {
    						view.warningPopup("Please enter a proper credit card number. (at least 10 digits)");
    						return;
    					}
    					if (isCardExpired())
    					{
    						view.warningPopup("The card has expired. Please process the " +
    								"transaction through cash payment or enter another card.");
    					}    
    					else {
    						/* Authorization would be checked here but this is not handled by our program
    						 * so it is set to true (always authorized) */
    						boolean creditCardAccepted = true;
    						
    						/* In an event where the credit card is not authorized the 
    						 * following would occur.
    						 */
    						if (!creditCardAccepted) {
    							view.warningPopup("Credit card denied.");
    							if (JOptionPane.showConfirmDialog(null, "Complete transaction in cash?") == 0) {
    								return;
    							}
    							else { // cancels transaction if customer refuses to pay in cash
    								view.pur().clearTransaction(); 
    								totalPrice = 0;
    								view.pur().updateTotal(0, 0);
    							}
    							return;
    						}
    						// confirm sale
    						if (JOptionPane.showConfirmDialog(null, 
    							"Complete transaction with a total of " + formatter.format(totalPrice) + " ?") == 0) {
    							processByCard();
    						}
    						else { // if user selects "no" or "cancel" to completing the purchase
    							return;
    						}
    					}
    				}
    				// if payment by cash is selected
    				else {
    					if (JOptionPane.showConfirmDialog(null, 
    						"Complete transaction with a total of " + formatter.format(totalPrice) + " ?") == 0) {
    						model.processByCash();
    					}
    					else { // if user selects "no" or "cancel" to completing the purchase
    						return;
    					}
    				}
    				int receiptId = model.getReceiptId();
    				if (receiptId == DOES_NOT_EXIST) {
        				JOptionPane.showMessageDialog(new JFrame(), "Please redo the transaction, " +
        						"a fatal error has occurred. If the error persists, please contact " +
        						"the System Administrator.", 
        						"Error Occurred", JOptionPane.ERROR_MESSAGE);
    				}
    				else {
    					model.addProductItems(salesList, receiptId); // add entries in ProductItems table for all items
    					model.updatePurchase(receiptId); // complete the purchase table entry
    					model.updateStock(receiptId); // update stock accordingly
    					printReceipt(receiptId);
    					view.pur().clearTransaction(); // clear the UI
    					totalPrice = 0;
    					view.pur().updateTotal(0, 0);
    				}
    			}
    			catch(NumberFormatException e) {
    				JOptionPane.showMessageDialog(new JFrame(), "Please use numbers only.", 
					"Invalid Input", JOptionPane.ERROR_MESSAGE);
    			}
            }
        });	
	}
	
	/**
	 * Holds all the action listeners for the return view
	 */
	private void returnViewActions() {
		view.ret().addActionToRefundButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (view.ret().getIDText().length() == 0 || view.ret().getUPCText().length() == 0 || view.ret().getQtyText().length() == 0) {
					view.warningPopup("Please complete all fields.");
					return;
				}
				try {
					int receiptId = Integer.parseInt(view.ret().getIDText());
					int upc = Integer.parseInt(view.ret().getUPCText());
					int qty = Integer.parseInt(view.ret().getQtyText());
					
					if (qty <= 0) {
						view.warningPopup("There must be at least one item in order to process a return.");
						return;
					}
					// verifies that the entered information has an existing purchase record, 
					// purchase item record and that the return is within 15 days
					if(!model.isValidReturn(receiptId, upc))  { 
						view.warningPopup("This is not a valid return. Please check the UPC " +
								"and receipt ID entered and verify that the purchase date " +
								"has not exceeded 15 days.");
						return;
					}
					
					int returnedQty = model.getReturnedQty(receiptId, upc); // 0 if no previous return on specific item
					if(!model.validQty(receiptId, upc, qty, returnedQty)) { // prevents returning items already returned
						view.warningPopup("Quantity error. Please re-enter. " +
								"The quantity requested for refund is greater than remaining " +
								"refundable quantity.");
						return;
					}
					
					if (JOptionPane.showConfirmDialog(null, "Commit Refund?") == 0) {
						
						model.createReturn(receiptId); // Creates a return record
						int retid = model.getRetId(receiptId);
						if (retid == DOES_NOT_EXIST) {
	        				JOptionPane.showMessageDialog(new JFrame(), "Please redo the transaction, " +
	        						"a fatal error has occurred. If the error persists, please contact " +
	        						"the System Administrator.", 
	        						"Error Occurred", JOptionPane.ERROR_MESSAGE);
	        				return;
						}
						model.addReturnItems(retid,upc,qty); // Create return items
						model.updateReturn(retid); // update return record with current date
						
						// we are assuming that all refunds taken back are tip-top quality 
						// and will be returned straight back to the shelf (stock)
						model.updateStockOnRefund(upc, qty);
						
						String card = model.getCard(receiptId); // retrieve credit card number if it exists
						float amount = model.getItemPrice(upc); // retrieves per item price
						
						// note: fields are not cleared in case another item is being returned from the same purchase
						if (card == null) {
							JOptionPane.showMessageDialog(new JFrame(), "$" + amount*qty + " has been " +
									"refunded in cash.");
						}
						else {
							JOptionPane.showMessageDialog(new JFrame(), "$" + amount*qty + " has been " +
									"refunded into your credit card.");
						}
					}
					else { // if user chooses "no" or "cancel" when prompted to commit the transaction
						return;
					}				
				}
    			catch(NumberFormatException e) {
    				JOptionPane.showMessageDialog(new JFrame(), "Please use numbers only.", 
					"Invalid Input", JOptionPane.ERROR_MESSAGE);
    			} 
			}	
		});
	}
		
	/**
	 * Process a transaction with a credit card
	 */
	private void processByCard() {		
	    String card = view.pur().getCreditCard();  
	    String expiryDate = view.pur().getExpiryDate();

	    model.processByCard(card, expiryDate);
	}
	
	/**
	 * Checks if the credit card provided has expired
	 * @return true: card has expired
	 * 		   false: card has not expired yet
	 */
	private boolean isCardExpired() {		
		Date currentDate = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("MM-yy"); 
		
		String currentDtString = dt.format(currentDate);
		String[] currentDateMonth = currentDtString.split("-");
		String expiryDate = view.pur().getExpiryDate();
		String[] expiryDateMonth = expiryDate.split("-");
		
		int currentMonth = Integer.parseInt(currentDateMonth[0]);
		int currentYear = Integer.parseInt(currentDateMonth[1]);
		int expiryMonth = Integer.parseInt(expiryDateMonth[0]);
		int expiryYear = Integer.parseInt(expiryDateMonth[1]);
		
		if (expiryYear < currentYear || (expiryYear == currentYear && expiryMonth < currentMonth)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Prints the receipt of the completed transaction
	 * @param receiptId: the id of the receipt
	 */
	private void printReceipt(int receiptId) {
		model.generateReceiptTable(receiptId, totalPrice);
	}

}
