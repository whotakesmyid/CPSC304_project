
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Clerk.ClerkController;
import Clerk.ClerkModel;
import Clerk.ClerkView;
import Customer.CustomerController;
import Customer.CustomerModel;
import Customer.CustomerView;
import Manager.ManagerController;
import Manager.ManagerModel;
import Manager.ManagerView;
import Others.TableView;


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
			loadCustomer();
		}
		
	}

	public static Object getCurrentController() {
		return currentController;
	}
	
	
	
	/* 
	 *   PRIVATE HELPER METHODS BELOW: 
	 */
	

	private static void addActionsToFrame() {
		
		frame.addActionToMenuCustomerMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		loadCustomer();
            }

        });
		
		frame.addActionToMenuClerkMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		loadClerk();
            }

        });
		
		frame.addActionToMenuManagerMode(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent event) {
        		loadManager();
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
		

		frame.addActionToMenuItemTable(tableQuery("Item"));
		frame.addActionToMenuLeadSingerTable(tableQuery("LeadSinger"));
		frame.addActionToMenuHasSongTable(tableQuery("HasSong"));
		frame.addActionToMenuCustomerTable(tableQuery("Customer"));
		frame.addActionToMenuPurchaseTable(tableQuery("Purchase"));
		frame.addActionToMenuPurchaseItemTable(tableQuery("PurchaseItem"));
		frame.addActionToMenuReturnTable(tableQuery("Return"));
		frame.addActionToMenuReturnItemTable(tableQuery("ReturnItem"));
		frame.addActionToMenuCustomQuery(tableQuery(null));
		
	}

	private static ActionListener tableQuery(final String tableTitle) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableView table;

				if (tableTitle == null) {
					String getQuery = frame.inputPopup("Please, enter a SQL query:");
					if (getQuery == null) return; //input cancelled by user
					table = new TableView(db.getCon(), getQuery);
				}
				else {
					table = new TableView(db.getCon(), "SELECT * FROM " + tableTitle);

					table.addButton("Delete", new AbstractAction() {
						private static final long serialVersionUID = 1L;

						@Override
						public void actionPerformed(ActionEvent e) {

							JTable table = (JTable)e.getSource();
							int modelRow = Integer.valueOf( e.getActionCommand() );

							PreparedStatement ps;
							String value1, value2, part1, part2;
							
							if (table.getValueAt(modelRow, 0) == null) {
								part1 = " IS NULL";
								value1 = "";
							} else {
								value1 = table.getValueAt(modelRow, 0).toString();
								part1 = " = ?";
							}
							
							if (table.getValueAt(modelRow, 1) == null) {
								part2 = " IS NULL";
								value2 = "";
							} else {
								value2 = table.getValueAt(modelRow, 1).toString();
								part2 = " = ?";
							}
							
							try {
								ps = db.getCon().prepareStatement("DELETE FROM " + tableTitle + " WHERE "
										+ table.getColumnName(0) + part1 + " AND "
										+ table.getColumnName(1) + part2);

														
								if (value1.length() > 0)
									ps.setString(1, value1);	
								
								if (value2.length() > 0)
									ps.setString(2, value2);
								
								int rowCount = ps.executeUpdate();

								if (rowCount == 1) {
									db.getCon().commit();
									((DefaultTableModel) table.getModel()).removeRow(modelRow);
								} else {
									db.getCon().rollback();
									frame.errorPopup("Wrong number of rows affected: " + rowCount + " rows");
								}
								ps.close();
							} catch (SQLException e1) {
								frame.errorPopup(e1.getMessage());
							}

						}

					});
				}
				table.show();
			}

		};
	}
	
	private static void loadCustomer() {
		CustomerView view = new CustomerView();
		CustomerModel model = new CustomerModel(db.getCon());
		currentController = new CustomerController(model, view);
		frame.changeView(view);
	}
	
	private static void loadManager() {
		ManagerView view = new ManagerView();
		ManagerModel model = new ManagerModel(db.getCon());
		currentController = new ManagerController(model, view);
        frame.changeView(view);
	}
	
	private static void loadClerk() {
		ClerkView view = new ClerkView();
		ClerkModel model = new ClerkModel(db.getCon());
		currentController = new ClerkController(model, view);
        frame.changeView(view);
	}

}
