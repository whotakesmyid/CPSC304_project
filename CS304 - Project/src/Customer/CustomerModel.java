package Customer;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import Others.TableView;

/** 
 * This class controls actions related to the customer.
 * It deals directly with the database.
 */

public class CustomerModel {
	
	/* Days to send the package if there are no outstanding orders: */
	final static int DAYS_TO_ARRIVE = 5;
	
	/* Maximum number of orders that can be processed in a day: */
	final static int MAX_NUM_OF_ORDERS = 5;
	
	
	/* Variables from the database model: */
	private int cid;
	private int receiptId; //id of the current purchase (the cart)
	private String name;
	private String address;
	private String phone;
	
	/* Variables to save the total price to pay and how many items are in the cart: */
	private double totalPrice;
	private int totalItems;
	
	/* The connection to the database (necessary to execute queries): */
	private Connection con;
	
	public CustomerModel(Connection con) {
		this.con = con;
	}

	public int getCid() {
		return cid;
	}
	
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public int getTotalItems() {
		return totalItems;
	}
	
	/** @return true if the customer was successfully registered */
	public boolean registerCustomer(String name, String password,
			String address, String phone) {
		
		PreparedStatement  ps;

		try {
			ps = con.prepareStatement("INSERT INTO " +
					"Customer(cid, password, name, address, phone)" +
					" VALUES (seq_cid.nextVal,ORA_HASH(?), ?, ?, ?)");
			
			ps.setString(2, name);
			ps.setString(1, password);
			ps.setString(3, (address.length() == 0) ? null : address);
			ps.setString(4, (phone.length() == 0) ? null : phone);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			//Insert a purchase to represent the customer cart:
			ps = con.prepareStatement("INSERT INTO Purchase VALUES " +
					"(seq_receiptId.nextVal, seq_cid.currVal, NULL, NULL, NULL, NULL, NULL)");
						
			ps.executeUpdate();
			con.commit();
			ps.close();
			
			

		} catch (SQLException e) {
			System.out.println("Could not register user: " + e.getMessage());
			rollback();
			return false;
		}
		return true;
	}

	/** @return true if the customer logs in successfully */
	public boolean login(String username, String password) {
		
		ResultSet  rs;
		PreparedStatement  ps;

		try
		{
			ps = con.prepareStatement("SELECT Customer.cid, name, address, phone," +
					" receiptId FROM Customer, Purchase " +
					"WHERE name = ? AND password = ORA_HASH(?) " +
					"AND Customer.cid = Purchase.cid AND purchaseDate IS NULL");
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				cid = rs.getInt("cid");
				name = rs.getString("name");
				address = rs.getString("address");
				phone = rs.getString("phone");
				receiptId = rs.getInt("receiptId"); //the cart
				ps.close();
				return true;
			}
			ps.close();
			
		}
		catch (SQLException ex)
		{
			System.out.println("Could not login: " + ex.getMessage());
		}	
		
		return false;
	}
	
	/** 
	 * Calculates how much the customer will have to pay and the number of items in cart.
	 * The information is saved into class variables totalAmount and totalItems.
	 * @return true if checkout was prepared with success.
	 */
	public boolean prepareCheckout() {
		PreparedStatement  ps;
		try {
			ps = con.prepareStatement("SELECT SUM(total_price) AS price, SUM(quantity) AS quantity" +
					" FROM (SELECT quantity, (price * quantity) AS total_price " +
					"FROM Item, PurchaseItem WHERE Item.upc = PurchaseItem.upc " +
					"AND PurchaseItem.receiptId = ?)");
			ps.setInt(1, receiptId);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				totalPrice = rs.getDouble("price");
				totalItems = rs.getInt("quantity");
			} else {
				totalPrice = 0;
				totalItems = 0;
			}
			
			ps.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public PreparedStatement getCart() {
		PreparedStatement  ps;
		
		try {
			
			/* The query that will be displayed in the table: */
			ps = con.prepareStatement("SELECT Item.upc, title, type," +
					" price, quantity, (price * quantity) AS \"TOTAL PRICE\" " +
					"FROM Item, PurchaseItem " +
					"WHERE Item.upc = PurchaseItem.upc AND " +
					"PurchaseItem.receiptId = ?");
			ps.setInt(1, receiptId);
			
			return ps;
			
		} catch (SQLException e) {
			System.out.println("Could not get cart: " + e.getMessage());
		}
		return null;
	}

	public boolean getPurchases() {
		PreparedStatement  ps;
		
		try {
			ps = con.prepareStatement("SELECT Purchase.receiptId AS \"RECEIPT\", " +
					"total AS \"TOTAL PRICE\", totalQuantity AS \"NUMBER OF ITEMS\"," +
					" TO_CHAR(purchaseDate, 'dd-MON-yy') AS \"PURCHASE DATE\", " +
					"substr(card#, length(card#)-4, length(card#)) " +
					"AS \"CARD - LAST 5 DIGITS\", TO_CHAR(expectedDate, 'dd-MON-yy') " +
					"AS \"EXPECTED DATE\", TO_CHAR(deliveredDate, 'dd-MON-yy') " +
					"AS \"DELIVERED DATE\" FROM Purchase, " +
					"(SELECT Purchase.receiptId, SUM(total_price) " +
					"AS total, SUM(quantity) AS totalQuantity FROM Purchase, " +
					"(SELECT receiptID, quantity, (price * quantity) AS total_price " +
					"FROM Item, PurchaseItem WHERE Item.upc = PurchaseItem.upc) I " +
					"WHERE purchaseDate IS NOT NULL AND " +
					"I.receiptId = Purchase.receiptId AND cid = ? " +
					"GROUP BY (Purchase.receiptId)) P " +
					"WHERE P.receiptId = Purchase.receiptId ORDER BY purchaseDate");

			ps.setInt(1, cid);
			TableView table = new TableView(ps);
			
			table.addButton("Show items", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
			    public void actionPerformed(ActionEvent e) {
				
					JTable table = (JTable)e.getSource(); //Get the table
					int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number

					PreparedStatement ps;
					String receipt = table.getValueAt(modelRow, 0).toString(); //get the first value of the row

					try {
						ps = con.prepareStatement("SELECT Item.upc, title, type, category, company, year," +
								" price, quantity, (price * quantity) AS \"TOTAL PRICE\" " +
								"FROM Item, PurchaseItem " +
								"WHERE Item.upc = PurchaseItem.upc AND " +
								"PurchaseItem.receiptId = ?");
						ps.setString(1, receipt);
						TableView table2 = new TableView(ps);
						table2.show();
						table2.setFrameTitle("Items of purchase " + receipt);
						
					} catch (SQLException e1) {
						System.out.println("Could not get purchase items: " + e1.getMessage());
					}
				}
			});
			
			table.show();
			table.setFrameTitle("Past Purchases");

			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Could not get cart: " + e.getMessage());
		}
		return false;
	}
	
	public boolean searchItem(String searchCategory, String searchTitle, String searchSinger ){
		PreparedStatement  ps;
		
		try {
			ps = con.prepareStatement("SELECT I.upc, title, type, category, company" +
					", year, price, stock, name AS \"ARTIST NAME\" " +
					"FROM Item I, LeadSinger S WHERE " +
					"I.upc=S.upc AND UPPER(I.category) LIKE UPPER(?) " +
					"AND UPPER(I.title) LIKE UPPER(?) AND UPPER(S.name) LIKE UPPER(?)");


			ps.setString(1, "%" + searchCategory + "%");
			ps.setString(2, "%" + searchTitle + "%");
			ps.setString(3, "%" + searchSinger + "%");

			TableView table = new TableView(ps);
			
			table.addButton("Songs", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
			    public void actionPerformed(ActionEvent e) {
				
					JTable table = (JTable)e.getSource(); //Get the table
					int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number

					PreparedStatement ps;
					String itemUPC = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
					
					try{
						ps = con.prepareStatement("SELECT I.title AS \"ALBUMN TITLE\", S.title AS \"SONG TITLE\"" +
								"FROM Item I, HasSong S WHERE " +
								"I.upc=S.upc AND I.upc LIKE ?");
						
						ps.setString(1, itemUPC);
						
						TableView songTable = new TableView(ps);
						songTable.show();

						ps.close();

					} catch(SQLException e3) {
						System.out.println("Show Songs Error: " + e3.getMessage());
					}
				}
			});					
			table.addButton("Add to Cart", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
			    public void actionPerformed(ActionEvent e) {
				
					JTable table = (JTable)e.getSource(); //Get the table
					int modelRow = Integer.valueOf( e.getActionCommand() ); //get the row number

					PreparedStatement ps;
					String itemUPC = table.getValueAt(modelRow, 0).toString(); //get the first value of the row
					
					String getQuantity = JOptionPane.showInputDialog("How many?", "1");
					
					int quant = 0;
					
					if (getQuantity == null) return; //Cancel button pressed
					
					try {
						quant = Integer.parseInt(getQuantity);
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
					
					int stock = 0;

					try {
						
						ps = con.prepareStatement("SELECT stock FROM Item WHERE upc = ?");
						ps.setString(1, itemUPC);
						
						ResultSet rs = ps.executeQuery();
						
						if (rs.next())
							stock = rs.getInt("stock");
						
						ps.close();
						

						/* Check if the item is already in the cart: */
						ps = con.prepareStatement("SELECT quantity FROM PurchaseItem WHERE upc = ? AND receiptId = ?");
						ps.setString(1, itemUPC);
						ps.setInt(2, receiptId);
						
						rs = ps.executeQuery();
						
						int alreadyInCart = 0;
						if (rs.next()) {
							alreadyInCart = rs.getInt("quantity");
						}
						
						int newQty = alreadyInCart + quant;
						
						if (stock < (newQty)) {
							JOptionPane.showMessageDialog(null, "There's not enough items in" +
									" the stock. You wanted " + quant + ". " 
									+ ((alreadyInCart > 0) ? ("You already have " + alreadyInCart + ". ") : "")
									+ "The stock has " + stock + ".",
									"Error", JOptionPane.ERROR_MESSAGE);
							if (stock > 0 && stock != alreadyInCart) {
								if (JOptionPane.showConfirmDialog(null, "Would you like to add " +
										"the remaining stock to your cart?") == 0) {
										newQty = stock;
								}
								else {
									return;
								}
							}
							else {
								return;
							}
						}
						
						if (alreadyInCart > 0) {

							/* The item is already in the cart. So, just increment the quantity: */
							ps = con.prepareStatement("UPDATE PurchaseItem SET quantity = ?" +
									" WHERE upc = ? AND receiptId = ?");
							ps.setInt(1, newQty);
							ps.setString(2, itemUPC);
							ps.setInt(3, receiptId);
							if (executeAndCheck(ps, "Added to cart!"))
								con.commit();
							
						} else {
						
							/* The item is not in the cart. So, add to the cart: */
							ps = con.prepareStatement("INSERT INTO PurchaseItem VALUES (?,?,?)");
							ps.setInt(1, receiptId);
							ps.setString(2, itemUPC);
							ps.setInt(3, newQty);
							if (executeAndCheck(ps, "Added to cart!"))
								con.commit();
						
						}
						ps.close();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "SQL Error: " + e1.getMessage(), 
								"Error", JOptionPane.ERROR_MESSAGE); //Display error popup.
						
						try {
							con.rollback();
						} catch (SQLException e2) {
							System.out.println("ERROR. Could not rollback. " + e2.getMessage());
							System.exit(-1);
						}
					}
				}

			});
			

			
			table.show();

			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Search error: " + e.getMessage());
		}
		return false;
	}
	
	private boolean executeAndCheck(PreparedStatement ps, String msg) throws SQLException {
		int rowCount = ps.executeUpdate();

		if (rowCount == 1) {
		if (msg != null)
			JOptionPane.showMessageDialog(null, msg, 
					"Info", JOptionPane.INFORMATION_MESSAGE);
		return true;
		} else {
			con.rollback(); //Ignore the query. Something went wrong.
			JOptionPane.showMessageDialog(null, "Wrong number of rows affected: " 
					+ rowCount + " rows", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	/** @return how many days a new purchase will take to arrive */
	public int getDaysToArrive() {
		int outstandingOrders = getNumberOfOutstandingOrders();
		if (outstandingOrders < 0) return -1;
		
		return outstandingOrders / MAX_NUM_OF_ORDERS + DAYS_TO_ARRIVE;
	}

	/** @return how many days it will take for the product to arrive. -1 if never. */
	public int buy(String cardNumber, String expiryDate) {

		PreparedStatement  ps;

		try {
			
			int daysToArrive = getDaysToArrive();
			if (daysToArrive < 1) return -1;
			
			ps = con.prepareStatement("UPDATE Purchase SET purchaseDate = current_date," +
					" expectedDate = current_date + " + daysToArrive +
					", card# = ?, expiryDate = TO_DATE(?, 'MM-YY') WHERE receiptId = ?");
			ps.setString(1, cardNumber);
			ps.setString(2, expiryDate);
			ps.setInt(3, receiptId);
			if (!executeAndCheck(ps, null)) return -1;
			ps.close();
			
			ps = con.prepareStatement("INSERT INTO Purchase VALUES " +
					"(seq_receiptId.nextVal, ?, NULL, NULL, NULL, NULL, NULL)");
			ps.setInt(1, cid);
			if (!executeAndCheck(ps, null)) return -1;
			ps.close();
			
			ps = con.prepareStatement("UPDATE item SET stock = stock - " +
					"(SELECT quantity FROM PurchaseItem " +
					"WHERE item.upc = purchaseItem.upc AND receiptId = ?) " +
					"WHERE EXISTS (SELECT quantity FROM PurchaseItem " +
					"WHERE item.upc = purchaseItem.upc AND receiptId = ?)");
			ps.setInt(1, receiptId);
			ps.setInt(2, receiptId);
			ps.executeUpdate();
			ps.close();
			
			/* Get the new receiptId: */
			ps = con.prepareStatement("SELECT receiptId FROM Purchase " +
					"WHERE cid = ? AND purchaseDate IS NULL");
			ps.setInt(1, cid);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())  {
				con.rollback();
				return -1;
			}
			receiptId = rs.getInt("receiptId"); //the cart

			ps.close();
			con.commit();
			return daysToArrive;
		}
		catch (SQLException e1) {
			System.out.println("SQL Error: " + e1.getMessage());
			try {
				con.rollback();
			} catch (SQLException e2) {
				System.out.println("ERROR. Could not rollback. " + e2.getMessage());
				System.exit(-1);
			}
		}	
		return -1;
	}

	public boolean deletePurchaseItem(String itemUPC) {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("DELETE FROM PurchaseItem WHERE receiptId = ? AND upc = ?");

			ps.setInt(1, receiptId);
			ps.setString(2, itemUPC);	

			int rowCount = ps.executeUpdate();

			if (rowCount == 1) {
				con.commit();
				ps.close();
				return true;
			} else {
				con.rollback(); //Ignore the query. Something went wrong.
				JOptionPane.showMessageDialog(null, "Wrong number of rows affected: " 
						+ rowCount + " rows", "Error", JOptionPane.ERROR_MESSAGE);
				ps.close();
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "SQL Error: " + e1.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE); //Display error popup.
		}
		return false;
	}
	
	/** Undo every query executed after the last commit: */
	private void rollback() {
		try {
			con.rollback();	
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Could not rollback: " + e.getMessage()
					, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

	}
	
	
	/** @return the number of orders that are still going to be delivered: */
	private int getNumberOfOutstandingOrders() {
		ResultSet rs;
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("SELECT COUNT(*) FROM Purchase WHERE deliveredDate IS NULL");
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Could not get outstanding orders: " + e.getMessage());
		}
		return -1;
		
	}


}


