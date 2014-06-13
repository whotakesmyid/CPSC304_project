import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import Clerk.ClerkController;
import Clerk.ClerkView;
import Customer.CustomerController;
import Customer.CustomerView;
import Manager.ManagerController;
import Manager.ManagerView;


/**
 * This class is the main class of the application. It calls the primary views
 * and connects to the database. The main() function is in here.
 */

public class MainController {
	
	private static Database db = new Database();
	
	/* The main frame of the app. All views might be simply JPanels of this view. */
	private static MainView frame; 
	
	/* Keep track of the current view controller running (which view the user is seeing): */
	private static Object currentController;
	
	public static void main(String[] args) {
		
		/* If you are able to connect to the database, then show the main frame: */
		if (db.connect()) {
			frame = new MainView();
			addActionsToFrame();
			frame.setVisible(true);
			
			/* Display the customer view as the default view: */
			CustomerView view = new CustomerView();
    		currentController = new CustomerController(db.getCon(), view);
            frame.changeView(view);
		}
		
	}
	
	public static Object getCurrentController() {
		return currentController;
	}

	private static void addActionsToFrame() {
		
		frame.addActionToMenuCustomerMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		CustomerView view = new CustomerView();
        		currentController = new CustomerController(db.getCon(), view);
                frame.changeView(view);
            }

        });
		
		frame.addActionToMenuClerkMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		ClerkView view = new ClerkView();
        		currentController = new ClerkController(db.getCon(), view);
                frame.changeView(view);
            }

        });
		
		frame.addActionToMenuManagerMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		ManagerView view = new ManagerView();
        		currentController = new ManagerController(db.getCon(), view);
                frame.changeView(view);
            }

        });
		
		frame.addActionToMenuExitMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		try {
    				db.getCon().close();
    			}
    			catch (SQLException ex) {
    				System.out.println("Error when closing database: " + ex.getMessage());
    			}
        		
        		System.exit(0);
            }

        });
		
		
	}

}
