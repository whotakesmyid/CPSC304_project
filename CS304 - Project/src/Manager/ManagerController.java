package Manager;

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
 * This class controls the manager view and interacts with the Manager model.
 */


public class ManagerController {

	/* The view (to display the results): */
	private ManagerView view;

	/* The model (to interact with the database and save values): */
	private ManagerModel manager;

	public ManagerController(ManagerModel model, ManagerView view) {
		manager = model;
		this.view = view;
		setUpView();
	}

	private void setUpView() {

		/** Open daily report view: */
		view.addActionToDailyButton(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				view.displayDailyView();

			}
		});

		/** Open total report view: */
		view.addActionToTopBtn(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				view.displayTotalView();
			}
		});

		/** Open change item view: */
		view.addActionToChangeBtn(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				view.displayChangeView();
				searchItem();
			}
		});

		/** Open delivery view: */
		view.addActionToDeliveryBtn(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showDeliveryView();
			}
		});

		/** Generate daily view report: */
		view.daily_view().addActionToGenerateButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement ps = manager.getDailyReportAllItems(view
						.daily_view().getDate());

				/* Create a table to display the results of the query above: */
				TableView table = new TableView(ps);

				try {
					view.addTablePanel(table.getTable());

					ps = manager.getDailyReportCategorialTotal();
					table = new TableView(ps);
					view.addTablePanel(table.getTable());

					view.addTotalLabel(manager.getDailyReportTotalPrice(),
							manager.getDailyReportTotalItems());

					view.daily_view().disableBtn();

				} catch (SQLException e) {
					view.warningPopup("Can't show table: " + e.getMessage());
				}

			}

		});

		/** Generate total view report: */
		view.total_view().addActionToGenerateButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String row_num_display = JOptionPane.showInputDialog(null,
						"How many items should we display?", "5");

				int quant = 0;

				if (row_num_display == null) return; //Cancel button pressed

				try {
					quant = Integer.parseInt(row_num_display);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Quantity typed is not a number.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}

				if (quant < 1) {
					JOptionPane.showMessageDialog(null, "Quantity must be bigger than 0.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}

				PreparedStatement ps = manager.getTopSellingReportItems(view
						.total_view().getDate(), quant);

				TableView table = new TableView(ps);

				try {
					view.removeTablePanel();
					view.addTablePanel(table.getTable());
					table.setColumnSize(0, 50); //upc
					table.setColumnSize(1, 260); //name
					table.setColumnSize(2, 200); //company

				} catch (SQLException e1) {
					view.warningPopup("Can't show table: " + e1.getMessage());
				}

			}

		});


		/** Change Item: */
		view.changeitem_view().addActionToSearchItemButton(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchItem();
			}
		});

		/** Insert new Item if doesn't exist */
		view.changeitem_view().addActionToInsertItemButton(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] values = view.getAddItemFormDialog();
				if (values == null) return;
				if (manager.insertItem(values))
					view.infoPopup("Item added successfully!");
				else
					view.warningPopup("Could not add item. Please, try again later.");
			}
		});
	}

	private void showDeliveryView() {
		view.displayDeliveryView();

		TableView table = new TableView(manager.getShipmentStatement());

		table.addButton("Client", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number
				String receipt = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				String cid = table.getValueAt(modelRow, 1).toString(); //get the second value of the row
				view.infoPopup(manager.getCustomerPurchaseInfo(receipt, cid));
			}
		});

		table.addButton("Items", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number
				String receipt = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				TableView table2 = new TableView(manager.getItemsFromPurchase(receipt));
				table2.show();
				table2.setFrameTitle("Items of purchase " + receipt);

			}
		});

		table.addButton("Set Delivery Date", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {

				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number
				String receipt = table.getValueAt(modelRow, 0).toString(); //get the first value of the row

				String date = view.getDateDialog("Delivery Date");
				if (date == null) return;

				if (manager.setDeliveryDate(receipt, date)) {
					showDeliveryView();
					view.infoPopup("The delivery date has been set to " + date + ".");
				}
				else
					view.warningPopup("Could not set the delivery date. Please, try again later.");
			}
		});

		try {
			view.addTablePanel(table.getTable());
			table.setColumnSize(0, 10); //receiptId
			table.setColumnSize(1, 20); //cid
			table.setColumnSize(2, 80); //date
			table.setColumnSize(3, 80); //date
			table.setColumnSize(4, 40); //customer button
			table.setColumnSize(5, 40); //items button
		} catch (SQLException e1) {
			view.warningPopup("Can't show table: " + e1.getMessage());
		}
	}


	private void searchItem() {
		TableView table = new TableView(manager.getSearchItemStatement(view.changeitem_view().getItemTitle()));

		table.addButton("Price", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number					

				String upc = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				String originalPrice = table.getValueAt(modelRow, 3).toString();

				String newPrice= JOptionPane.showInputDialog(null, "Change price:", originalPrice);	
				if (newPrice == null) return; //Cancel button is pressed				

				double price = 0;

				/*
				 * Input not a number
				 */					
				try {
					price = Double.parseDouble(newPrice);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Price typed is not a number.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}		

				if (price < 0) {
					JOptionPane.showMessageDialog(null, "Price must be bigger or equal 0.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}


				if (manager.changeItemPrice(upc, newPrice)) {
					table.setValueAt(newPrice, modelRow, 3);
				} else {
					view.warningPopup("Could not change the price. Please, try again later.");
				}

			}
		});
		
		table.addButton("Quantity", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number					

				String upc = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				String originalStock = table.getValueAt(modelRow, 4).toString();

				String newStock = JOptionPane.showInputDialog(null, "Change stock:", originalStock);	
				if (newStock == null) return; //Cancel button is pressed				

				int quant = 0;

				/*
				 * Input not a number
				 */					
				try {
					quant = Integer.parseInt(newStock);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Quantity typed is not a number.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}		

				if (quant < 1) {
					JOptionPane.showMessageDialog(null, "Quantity must be bigger than 0.", 
							"Error", JOptionPane.ERROR_MESSAGE); 
					return;
				}


				if (manager.changeItemStock(upc, newStock)) {
					table.setValueAt(newStock, modelRow, 4);
				} else {
					view.warningPopup("Could not change the stock. Please, try again later.");
				}

			}
		});

		table.addButton("Delete", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource(); //Get the table
				int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number	
				String upc = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
				String name = table.getValueAt(modelRow, 1).toString(); //get the first value of the row
				if (view.confirmationPopup("Delete item " + name + "?") == false) return;

				if (manager.deleteItem(upc)) {
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
				} else {
					view.warningPopup("Could not delete item. Maybe this item is being used.");
				}

			}});

		try {
			view.removeTablePanel();
			view.addTablePanel(table.getTable());
			table.setColumnSize(0, 20); //upc
			table.setColumnSize(1, 230); //title
			table.setColumnSize(2, 50); //year
			table.setColumnSize(3, 50); //price
			table.setColumnSize(4, 50); //stock
			table.setColumnSize(5, 65); //set price
			table.setColumnSize(6, 82); //quantity
			table.setColumnSize(7, 70); //delete
		} catch (SQLException e1) {
			view.warningPopup("Can't show table: " + e1.getMessage());
		}
	}

}




