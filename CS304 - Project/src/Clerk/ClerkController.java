package Clerk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * This class controls the clerk view (eg: the action of the buttons in the view).
 * Since this is a small app, maybe we can keep the model and the controller in here.
 * That means, instead of the model, the controller can do the database queries and display
 * the results in the view class.
 */

/*
 *  ====================== INCOMPLETE CLASS (UNDER DEVELOPMENT) ====================== 
 *  Things the clerk should be able to do (maybe you want to implement in this order):
 *  1. Get the product code (by typing. In a real scenario, that would happen through the scanner).
 *		- Info about the product is shown in a table.
 *		- User can add more product codes, which add more items in the table.
 *		- Button to purchase. When pressed, it shows price and ask cash or credit card. 
 *			* If credit card, type the card number.
 *		- Display receipt in the end (receipt #, date, items, quantities, prices, total, card#).
 *        
 *   2. Process an item being returned. 
 *   	- Type the item upc and the receipt number.
 *   	- Display if the return is in cash or card.
 *        
 */

public class ClerkController {
	
	/* The connection to the database (necessary to execute queries): */
	private Connection con;
	
	/* The view (to display the results): */
	private ClerkView view;

	public ClerkController(Connection con, ClerkView view) {
		this.con = con;
		this.view = view;
		setUpView();
	}

	private void setUpView() {
		
		/* Add actions to the view: */
		view.addActionToButton(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
                view.warningPopup("You pressed that useless button");
            }

        });
		
	}
	
}
