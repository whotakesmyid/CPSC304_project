package Customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * This class controls the customer view (eg: the action of the buttons in the view).
 * Since this is a small app, maybe we can keep the model and the controller in here.
 * That means, instead of the model, the controller can do the database queries and display
 * the results in the view class.
 */

/*
 *  ====================== INCOMPLETE CLASS (UNDER DEVELOPMENT) ====================== 
 *  Things the customer should be able to do (maybe you want to do in this order):
 *  1. Register
 *  2. Login
 *  3. Search item (by title, category, and/or singer).
 *  4. Add item (from the search) to the basket. Set the quantity (popup if not enough available).
 *  5. Interact with your virtual shopping basket (see/add/remove items).
 *  6. Check-out button. Display bill with items and total amount. Add field for credit card.
 *  7. Table of all past purchases (that also show when you will receive future products).
 *  
 */

public class CustomerController {
	
	/* The connection to the database (necessary to execute queries): */
	private Connection con;
	
	/* The view (to display the results): */
	private CustomerView view;

	public CustomerController(Connection con, CustomerView view) {
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
