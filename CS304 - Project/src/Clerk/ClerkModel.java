package Clerk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class handles all the SQL queries related to the clerk
 */

public class ClerkModel {

	/* The connection to the database (necessary to execute queries): */
	private Connection con;
	private int receiptId; // current receiptId for the current transaction
	private int retid; // current retId for current transaction
	private static final int RECEIPT_COLUMNS = 3;
	
	/* The connection to the database (necessary to execute queries): */
	public ClerkModel(Connection con) {
		this.con = con;
	}

	/**
	 * Returns the title of an item
	 * @param upc: upc of an item (unique for every item)
	 * @return the title of the item or "NO TITLE" if no title/no item exists
	 */
	public String getItemTitle(int upc) {
		String title = "NO TITLE";
		PreparedStatement ps;
		ResultSet rs;

		try {		
			ps = con.prepareStatement("SELECT title FROM Item WHERE upc = ?");
			ps.setInt(1, upc);
			rs = ps.executeQuery();

			if (rs.next()) 
			{
				title = rs.getString(1);
			}
			return title;
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return title;
	}
	
	/**
	 * Returns the price of an item
	 * @param upc: the upc of an item (unique for every item)
	 * @return the price of a single item or 0 if there is no price or item
	 */
	public float getItemPrice(int upc) {
		float price = 0;
		PreparedStatement ps;
		ResultSet rs;

		try {		
			ps = con.prepareStatement("SELECT price FROM Item WHERE upc = ?");
			ps.setInt(1, upc);
			rs = ps.executeQuery();

			if (rs.next()) 
			{
				price = rs.getFloat(1);
			}
			return price;
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return price;
	}
	
	/**
	 * Insert a new purchase record into the purchase table based on a cash payment transaction
	 */
	public void processByCash() {
		PreparedStatement ps;

		try {
				ps = con.prepareStatement("INSERT INTO Purchase " +
						"(receiptId, cid, purchaseDate, card#, expiryDate, expectedDate, deliveredDate) " +
						"VALUES (seq_receiptId.nextval, ?, ?, ?, ?, ?, ?)");
				
				ps.setNull(1,java.sql.Types.INTEGER);
				ps.setNull(2, java.sql.Types.DATE); // set to null at first for getReceiptId()
				ps.setNull(3,java.sql.Types.VARCHAR);
				ps.setNull(4,java.sql.Types.DATE);
				ps.setNull(5,java.sql.Types.DATE);
				ps.setNull(6,java.sql.Types.DATE);
				
				ps.executeUpdate(); 
				con.commit(); 
				ps.close();
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
	}

	/**
	 * Insert a new purchase record into the purchase table based on a credit payment transaction
	 * @param card: card number entered by user
	 * @param expiryDate: expiry date of credit card
	 */
	public void processByCard(String card, String expiryDate) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO Purchase " +
					"(receiptId, cid, purchaseDate, card#, expiryDate, expectedDate, deliveredDate) " +
					"VALUES (seq_receiptId.nextval, ?, ?, ?, TO_DATE(?, 'MM-YY'), ?, ?)");
			
				ps.setNull(1,java.sql.Types.INTEGER);
				ps.setNull(2, java.sql.Types.DATE); // set to null at first for getReceiptId()
				ps.setString(3,card);
				ps.setString(4, expiryDate);
				ps.setNull(5,java.sql.Types.DATE);
				ps.setNull(6,java.sql.Types.DATE);
				
				ps.executeUpdate(); 
				con.commit(); 
				ps.close();
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
	}
	
	/**
	 * Inserts all productItems of a sale into the productItems table
	 * @param salesList: the list of all items purchased and their information
	 * @param receiptId: receiptId of the corresponding purchase record
	 */
	public void addProductItems(Object[] salesList, int receiptId) {
		PreparedStatement ps;
			 
		try {
			 for (Object item : salesList) {
				 String[] split = item.toString().split(", "); // parses the list to seperate info
					ps = con.prepareStatement("INSERT INTO PurchaseItem " +
							"(receiptId, upc, quantity) " +
							"VALUES (?, ?, ?)");
						
					ps.setInt(1, receiptId);
					ps.setInt(2, Integer.parseInt(split[0])); // index 0 contains the UPC
					ps.setInt(3, Integer.parseInt(split[2])); // index 2 contains the QTY
					ps.executeUpdate(); 
					con.commit(); 
					ps.close();
			 }
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
	}
	
	/**
	 * Checks if a upc is valid or not (i.e. exists inside the Item table)
	 * @param upc: the upc of an item that is being checked for validity
	 * @return true: an item with the upc exists in the Item table
	 * 		   false: there does not exist an item in the Item table with that upc
	 */
	public boolean validUPC(int upc) {
		PreparedStatement ps;
		ResultSet data;
		
		try {
			ps = con.prepareStatement("SELECT * FROM Item WHERE upc = ?");
			ps.setInt(1, upc);
			
			data = ps.executeQuery();

			if (data.next()) 
			{
				return true;
			}
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Retrieves the receiptId of the current purchase
	 * @return
	 */
	public int getReceiptId() {
		PreparedStatement ps;
		
		try {
			/* Updates to current receiptId */
			ps = con.prepareStatement("SELECT receiptId FROM Purchase " +
					"WHERE cid IS NULL AND purchaseDate IS NULL");

			ResultSet rs = ps.executeQuery();
			if (!rs.next())  {
				con.rollback();
				return -1; // purchase does not exist
			}
			receiptId = rs.getInt("receiptId"); //the cart

			ps.close();
			con.commit();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return receiptId;
	}

	/**
	 * Finalizes the sale by updating the purchase table with a purchase date
	 * @param receiptId2: receipt ID of the current transaction
	 */
	public void updatePurchase(int receiptId2) {
		PreparedStatement ps;
		 
		try {
			ps = con.prepareStatement("UPDATE Purchase SET  " +
							"purchaseDate = current_date " +
							"WHERE receiptId = ?");
						
					ps.setInt(1, receiptId2);
					ps.executeUpdate(); 
					con.commit(); 
					ps.close();
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
	}
	
	/**
	 * Updates the stock quantity in the Items table for all items that have been purchased
	 * @param receiptId2: the receipt ID of the purchase
	 */
	public void updateStock(int receiptId2) {
		PreparedStatement ps;
		 
		try {
			ps = con.prepareStatement("UPDATE item SET stock = stock - " +
					"(SELECT quantity FROM PurchaseItem " +
					"WHERE item.upc = purchaseItem.upc AND receiptId = ?) " +
					"WHERE EXISTS (SELECT quantity FROM PurchaseItem " +
					"WHERE item.upc = purchaseItem.upc AND receiptId = ?)");
			ps.setInt(1, receiptId2);
			ps.setInt(2, receiptId2);
			ps.executeUpdate();
			con.commit(); 
			ps.close();
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
	}
	
	/**
	 * Verifies there are enough stock for the quantity requested
	 * @param upc: upc of the item in question
	 * @param qty: amount desired from the stock
	 * @return true: if there are enough quantity in the stock
	 * 		   false: if there are not enough in the stock
	 */
	public boolean checkStock(int upc, int qty) {
		PreparedStatement ps;
		int stock = 0;

		try {		
			/* Check if there's enough in store: */
			ps = con.prepareStatement("SELECT stock FROM Item WHERE upc = ?");
			ps.setInt(1, upc);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				stock = rs.getInt("stock"); // amount that exists in the stock
			
			ps.close();
			
			if (stock < qty) {
				JOptionPane.showMessageDialog(null, "There's not enough items in" +
						" the stock. You wanted " + qty + ". " +
								"The stock has " + stock + ".",
						"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		catch (SQLException e) {
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
		}
		return true;
	}

	/**
	 * Generates the receipt for the purchase
	 * @param receiptId2: the receipt ID of the purchase to print a receipt for
	 * @param totalPrice: the total price of the sale
	 */
	public void generateReceiptTable(int receiptId2, float totalPrice) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("SELECT P.quantity, I.title AS \"PRODUCT\", I.price " +
				"FROM PurchaseItem P, Item I WHERE P.receiptId = ? AND P.upc = I.upc");
				
			ps.setInt(1, receiptId2);
			rs = ps.executeQuery();
			
			int rows = getRows(receiptId2); // retrieves the amount of rows needed for the receipt table

			ReceiptView receiptPanel = new ReceiptView();
			
			String card = getCard(receiptId2);
			String date = getDate(receiptId2);
	
			Object[][] data = new Object[rows][RECEIPT_COLUMNS];
			
			int i = 0;
			while (rs.next()) {
				String qty = Integer.toString(rs.getInt("QUANTITY"));
				String title = rs.getString("PRODUCT");
				String price = Float.toString(rs.getFloat("PRICE"));

				data[i][0] = qty;
				data[i][1] = title;
				data[i][2] = price;
				i++;
			}

			receiptPanel.setRows(data);
			receiptPanel.setTable(receiptId2, date, card, totalPrice);
			ps.close();	

		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}

	/**
	 * Returns the number of different products purchased. Helper function for generating a receipt.
	 * @param receiptId2: the receipt ID of the purchase
	 * @return the number of different products purchased
	 */
	private int getRows(int receiptId2) {
		PreparedStatement ps;
		ResultSet rs;
		int rows = 0;
		
		try {
			ps = con.prepareStatement("SELECT * FROM PurchaseItem WHERE receiptId = ? ");
					
			ps.setInt(1, receiptId2);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				rows++;
			}
			ps.close();	
			return rows;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return rows;
	}
	
	/**
	 * Retrieve the purchase date of a purchase
	 * @param receiptId2: the receipt ID of the purchase
	 * @return the purchase date or an empty string if no purchase date if fails
	 */
	public String getDate(int receiptId2){
		PreparedStatement ps;
		ResultSet rs;
		String date = "";
		
		try {
			ps = con.prepareStatement("SELECT purchaseDate FROM Purchase WHERE receiptId = ? ");
					
			ps.setInt(1, receiptId2);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				date = rs.getString("PURCHASEDATE");
			}
			ps.close();	
			return date;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * Returns the card number used as payment on a purchase
	 * @param receiptId2: the receipt ID of the purchase
	 * @return the card number or a blank string if no credit card exists
	 */
	public String getCard(int receiptId2) {
		PreparedStatement ps;
		ResultSet rs;
		String card = "";
		
		try {
			ps = con.prepareStatement("SELECT card# FROM Purchase WHERE receiptId = ? ");
					
			ps.setInt(1, receiptId2);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				card = rs.getString("CARD#");
			}
			ps.close();	
			return card;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return card;
	}
	
	/**
	 * Checks if the quantity requested to be returned is a valid quantity
	 * @param receiptId2: the receipt ID of the purchase
	 * @param upc: the UPC of the item
	 * @param qty: the quantity desired to be refunded
	 * @param returnedQty: the quantity already refunded
	 * @return true: if the quantity is valid (ie. there are enough items to refund)
	 *         false: if the quantity is invalid (ie. the amount requested for 
	 *         refund exceeds the purchase+refunded amount)
	 */
	public boolean validQty(int receiptId2, int upc, int qty, int returnedQty) {
		PreparedStatement ps;
		ResultSet rs;
		int purchasedQty = 0;
		try {
			ps = con.prepareStatement("SELECT quantity FROM PurchaseItem WHERE receiptId = ? AND upc = ?");
					
			ps.setInt(1, receiptId2);
			ps.setInt(2, upc);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				purchasedQty = rs.getInt("QUANTITY");
			}
			ps.close();	
			if (purchasedQty >= (qty + returnedQty)) {
				return true;
			}
			return false;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Adds items back to the stock
	 * @param upc: upc of the item to update
	 * @param qty: quantity of stock to add to the item
	 */
	public void updateStockOnRefund(int upc, int qty) {
		PreparedStatement ps;
		 
		try {
			ps = con.prepareStatement("UPDATE Item SET stock = stock + ? " +
					"WHERE upc = ?");
			ps.setInt(1, qty);
			ps.setInt(2, upc);
			ps.executeUpdate();
			con.commit(); 
			ps.close();
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
	}
	
	/**
	 * Creates a new return record into the Return table
	 * @param receiptId2: receiptId of the purchase
	 */
	public void createReturn(int receiptId2) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO Return (retid, receiptId, returnDate) " +
					"VALUES (seq_retid.nextval, ?, ?)");
			
				ps.setInt(1, receiptId2);
				ps.setNull(2, java.sql.Types.DATE); // date is set to NULL for getRetid()
				
				ps.executeUpdate(); 
				con.commit(); 
				ps.close();
		} 
		catch (SQLException e1) {
			System.out.println("SQL Error HERE: " + e1.getMessage());
			try {
				con.rollback();
			} catch (SQLException e2) {
				System.out.println("ERROR. Could not rollback. " + e2.getMessage());
				System.exit(-1);
			}
		}			
	}
	
	/**
	 * Returns the return ID of the current return
	 * @param receiptId2: receiptID related to the return
	 * @return the return ID
	 */
	public int getRetId(int receiptId2) {
		PreparedStatement ps;

		try {
			/* Updates to current retId */
			ps = con.prepareStatement("SELECT retid FROM Return WHERE returnDate IS NULL");

			ResultSet rs = ps.executeQuery();
			if (!rs.next())  {
				con.rollback();
				return -1; // return does not exist
			}
			retid = rs.getInt("retid"); //the cart

			ps.close();
			con.commit();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return retid;
	}
	
	/**
	 * Updates the return with the current date
	 * @param retid: return ID of the return
	 */
	public void updateReturn(int retid) {
		PreparedStatement ps;
		 
		try {
			ps = con.prepareStatement("UPDATE Return SET returnDate = current_date WHERE retid = ?");
						
					ps.setInt(1, retid);
					ps.executeUpdate(); 
					con.commit(); 
					ps.close();
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
	}
	
	/**
	 * Inserts the items returned into the returnItems table
	 * @param retid: return ID of the return
	 * @param upc: UPC of the item returned
	 * @param qty: quantity returned
	 */
	public void addReturnItems(int retid, int upc, int qty) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO ReturnItem (retid, upc, quantity) VALUES (?, ?, ?)");
			
				ps.setInt(1, retid);
				ps.setInt(2, upc);
				ps.setInt(3, qty);
				
				ps.executeUpdate(); 
				con.commit(); 
				ps.close();
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
	}

	/**
	 * Checks if a return is a valid return (ie. <= 15 days since purchase date, valid upc and receiptID)
	 * @param receiptId2: receipt ID of purchase
	 * @param upc: UPC of item being returned
	 * @return true: if return is valid
	 *         false: if return is invalid (does not pass one of the criterias)
	 */
	public boolean isValidReturn(int receiptId2, int upc) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("SELECT * FROM Purchase P , PurchaseItem I WHERE P.receiptId = ? " +
					"AND P.receiptId = I.receiptId AND I.upc = ? AND (current_date - P.purchaseDate) < 16");
					
			ps.setInt(1, receiptId2);
			ps.setInt(2, upc);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			ps.close();	
			return false;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Return the quantity already returned for a specific item and purchase
	 * @param receiptId2: receipt ID of the purchase
	 * @param upc: UPC of the item
	 * @return the sum of the quantities of all previous returns on the item of that purchase
	 */
	public int getReturnedQty(int receiptId2, int upc) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("SELECT sum(I.quantity) AS \"QUANTITY\" from Return R, ReturnItem I " +
					"WHERE R.receiptId = ? AND R.retid = I.retid AND I.upc = ? ");
					
			ps.setInt(1, receiptId2);
			ps.setInt(2, upc);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("QUANTITY");
			}
			ps.close();	
			return 0;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return 0;
	}
}
