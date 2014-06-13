package Customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Others.TableView;

/**
 * This class controls the customer views (eg: the action of the buttons in the view)
 * and interacts with the customer model.
 */

public class CustomerController {
	
	
	/* The view (to display the results): */
	private CustomerView view;
	
	/* The model (to interact with the database and save values): */
	private CustomerModel customer;
	
	public CustomerController(CustomerModel model, CustomerView view) {
		customer = model;
		this.view = view;
		setUpView();
	}

	private void setUpView() {
		
		/** Open register view: */
		view.addActionToRegBtn(new ActionListener(){
        	@Override
            public void actionPerformed(ActionEvent event) {
     			view.displayReg();
            }	
		});
		
		/** Open purchases: */
		view.addActionToPurchasesBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		if (!customer.getPurchases())
        			view.warningPopup("Error. Could not see past purchases. Please, try again.");
        	}
		});

		/** Register a customer (in register view): */
		view.reg().addActionToRegisterBtn(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				
				if (view.reg().getNameText().length() == 0) {
					view.warningPopup("You must type a name first.");
					return;
				}
				
				if (view.reg().getPassText().length() == 0) {
					view.warningPopup("You must type a password first.");
					return;
				}
				
				if (customer.registerCustomer(view.reg().getNameText(), view.reg().getPassText(), 
						view.reg().getAddrText(), view.reg().getPhoneText())) {
					view.infoPopup("Customer " + view.reg().getNameText() + 
							" was successfully registered!");
					view.hideReg();
				} else {
					view.warningPopup("Please, use a different name.");
				}
			}
		});
		
		/** Get out of the register view: */
		view.reg().addActionToReturnBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		view.hideReg();
            }
        });
		
		/** Get out of the buy view: */
		view.getBuyView().addActionToReturnBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		view.hideBuyView();
        		getCart();
        	}
		});
		
		/** Login button pressed: */
		view.addActionToLoginBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		if (customer.login(view.getUsernameText(), view.getPasswordText())) {
        			view.setUsernameLb("You're logged in as " + customer.getName());
        			view.displayMain(); //logged view
        			getCart();
        		} else {
        			view.warningPopup("Wrong login or password.");
        		}
        	}
		});
		
		/** Checkout button pressed: */
		view.addActionToBuyBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		view.displayBuyView();
        		if (customer.prepareCheckout()) {
    				view.updateCartInfo(customer.getTotalPrice(), customer.getTotalItems());
    			}
        	}
		});
		
		/** Buy button pressed to complete purchase: */
		view.getBuyView().addActionToBuyBtn(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				if (!customer.prepareCheckout()) {
					view.warningPopup("Could not update your final price. Please, try again.");
					return;
				}

				view.updateCartInfo(customer.getTotalPrice(), customer.getTotalItems());
				
				String cardNumber = view.getBuyView().getCreditCard();
				
				if (cardNumber.isEmpty()) {
					view.warningPopup("Please, type a credit card number first.");
					return;
				}
				
				if (customer.getTotalItems() < 1) {
					view.warningPopup("You should first add some items to your cart.");
					return;
				}
				
				String expiryDate = view.getBuyView().getExpiryDate();

				if (!view.confirmationPopup("Pay $" + customer.getTotalPrice() + 
						" in credit card number " + cardNumber + 
						" [Expiry date: "+ expiryDate +"]?"))
					return;

				/* Here would be a function to process the credit card and pay */
				boolean creditCardAccepted = true;
				//creditCardAccepted = buy(cardNumber, expiryDate, customer.getTotalPrice());

				if (!creditCardAccepted) {
					view.warningPopup("Credit Card denied.");
					return;
				}

				int daysToArrive = customer.buy(cardNumber, expiryDate);
				if (daysToArrive > 0) {
					view.hideBuyView();
					view.infoPopup("You just bought. Your product will arrive in " 
							+ daysToArrive + " days.");					
				} else {
					view.warningPopup("The purchase could not be completed." +
							"\nIt's likely that one of your items is not available anymore in stock." +
							"\nPlease, try again later or check the stock for items.");
				}

			}
		});

		/** Search button pressed: */
		view.addActionToSearchBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		//needs to output a textbox that the user can search with
        		view.displaySearch();
        		
        	}
		});
		
		/** Search Return button pressed: */
		view.addActionToSearchReturnBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		view.hideSearch();
        		getCart(); 		
        	}
		});
		
		/** RunSearch button pressed: */
		view.addActionToRunSearchBtn(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		String category = (String) view.categoryField.getSelectedItem();
        		if (category.equalsIgnoreCase("All")) category = "";
        		customer.searchItem(category, view.titleField.getText(),view.singerField.getText());
        	}
		});
		
	}
	
	private void getCart() {
		PreparedStatement ps = customer.getCart();
		if (ps == null) {
			view.warningPopup("Error. Could not open cart. Please, try again.");
			return;
		}

		/* Create a table to display the results of the query above: */
		TableView table = new TableView(ps);

		/* Add a "Remove" btn to that table. When this button is pressed, the item is removed from the db: */
		table.addButton("x", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number

				String itemUPC = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				String itemTitle = table.getValueAt(modelRow, 1).toString(); //get the second value of the row

				if (JOptionPane.showConfirmDialog(null, 
						"Are you sure you want to remove \"" + itemTitle + "\" from your cart?") == 0) {

					if (customer.deletePurchaseItem(itemUPC))

						/* Update the view to show that the row was removed: */
						((DefaultTableModel) table.getModel()).removeRow(modelRow);

				}
			}
		});

		/* Finally, display the table: */
		try {
			view.displayCart( table.getTable() );
		} catch (SQLException e1) {
			view.warningPopup("Could not get cart: " + e1.getMessage());
		}
		
		table.setColumnSize(0, 45); //Make the upc small
		table.setColumnSize(1, 350); //Make the name bigger
		table.setColumnSize(2, 55); //Make the type small
		table.setColumnSize(6, 50); //Make the button small
	}

}
