package Manager;

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
 *   Things the manager should be able to do (maybe you want to implement in this order):
 *  1. Change an item.
 *  		- Type the upc of an item. The item shows up.
 *  		- User can then edit the quantity of the item.
 *  		- Other informations are optional (eg: price, title). Recommended.
 *  2. Display daily sales. Get a date and display items sold on that date.
 *  3. Display top selling items. Get a date and a number n. 
 *  		- Display the n best selling items in that date.
 */

public class ManagerController {
	
	/* The connection to the database (necessary to execute queries): */
	private Connection con;
	
	/* The view (to display the results): */
	private ManagerView view;

	public ManagerController(Connection con, ManagerView view) {
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
