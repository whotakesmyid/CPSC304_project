package Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/** 
 * This class controls actions related to the manager.
 * It deals directly with the database.
 */

public class ManagerModel {

	/* Maximum number of items to display in the search when searching for an item: */
	static final int MAX_ITEMS_SEARCH = 50;

	private Connection con;
	Calendar now = Calendar.getInstance();

	double totalPrice = -1;
	int totalItems = -1;


	public ManagerModel(Connection con) {
		this.con = con;
	}

	/**
	 * Return a statement for the daily report of all items sold. 
	 * @return a null value if there's an error.
	 */
	public PreparedStatement getDailyReportAllItems(String strDate) {

		try {
			updateDailySalesView(strDate);
			return con.prepareStatement("SELECT upc, category, concat('$', price) AS price, units as \"UNITS SOLD\", concat('$', total_value) as \"TOTAL VALUE\" FROM daily_sales");
		}

		catch (SQLException ex) {
			System.out.println("Could not get result: " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Return a result containing the top selling [@param limit] items.
	 * @return a null value if there's an error.
	 */
	public PreparedStatement getTopSellingReportItems(String strDate,
			int limit) {

		PreparedStatement ps;

		try {
			updateDailySalesView(strDate);
			ps = con.prepareStatement("SELECT ROWNUM as rank, title, company, stock, units as sold FROM item NATURAL JOIN (SELECT upc, units FROM daily_sales ORDER BY units DESC) WHERE ROWNUM <= ?");
			ps.setInt(1, limit);
			return ps;
		}

		catch (SQLException ex) {
			System.out.println("Could not get result: " + ex.getMessage());
		}
		return null;
	}


	/**
	 * Search to see if a specific item to be added exist or not. 
	 * @return the prepared statement of the select query.
	 */
	public PreparedStatement getSearchItemStatement(String searchTitle) {

		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT upc, title, year, price, stock FROM item WHERE UPPER(title) LIKE UPPER(?) AND ROWNUM <= ?");
			ps.setString(1, "%" + searchTitle + "%");		
			ps.setInt(2, MAX_ITEMS_SEARCH);
			return ps;

		}
		catch (SQLException ex) {
			System.out.println("Could not prepare result: " + ex.getMessage());		

		}
		return null;
	}

	/**
	 * Insert an item into the store if it's not exist.
	 * @return true if everything went well.
	 */
	public boolean insertItem(String[] values) {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("INSERT INTO Item VALUES (?, ?, ?, ?, ?, ?, ?, 0)");
			for (int i = 0; i < 7; i++)
				ps.setString(i+1, values[i]);
			if (ps.executeUpdate() == 1) {
				con.commit();
				return true;
			}
			con.rollback();
		}
		catch (SQLException ex) {
			System.out.println("Could not get result: " + ex.getMessage());
		}
		return false;
	}


	public PreparedStatement getShipmentStatement() {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("SELECT receiptId AS id, cid, TO_CHAR(purchaseDate, 'DD-MON-YY') AS \"PURCHASE DATE\", TO_CHAR(expectedDate, 'DD-MON-YY') AS \"EXPECTED DATE\" FROM Purchase WHERE deliveredDate IS NULL AND expectedDate IS NOT NULL AND purchaseDate IS NOT NULL ORDER BY expectedDate");
			return ps;
		}

		catch (SQLException ex) {
			System.out.println("Could not prepare shipment statement: " + ex.getMessage());
		}
		return null;
	}


	public PreparedStatement getDailyReportCategorialTotal() {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("SELECT category, SUM(units) as \"UNITS SOLD\", concat('$', SUM(total_value)) AS \"TOTAL VALUE\" FROM daily_sales GROUP BY category");

			/* Prepares the total sum too: */
			PreparedStatement ps2 = con
					.prepareStatement("SELECT SUM(total_value) AS total_value, SUM(units) AS total_units FROM daily_sales");
			ResultSet rs = ps2.executeQuery();

			if (rs.next()) {
				totalPrice = rs.getDouble(1);
				totalItems = rs.getInt(2);
			}

			return ps;
		}

		catch (SQLException ex) {
			System.out.println("Could not get result: " + ex.getMessage());
			return null;
		}
	}

	public double getDailyReportTotalPrice() {
		return totalPrice;
	}

	public int getDailyReportTotalItems() {
		return totalItems;
	}

	public String getCustomerPurchaseInfo(String receipt, String cid) {

		PreparedStatement ps;
		ResultSet rs;

		try {
			ps = con.prepareStatement("SELECT name, address, phone FROM Customer, Purchase WHERE Customer.cid = Purchase.cid AND Purchase.cid = ? AND Purchase.receiptId = ?");
			ps.setString(1, cid);
			ps.setString(2, receipt);

			rs = ps.executeQuery();

			if (rs.next()) {
				String info = "Customer: " + rs.getString("name") + "\n"
						+ "Address: " + rs.getString("address") + "\n"
						+ "Phone: " + rs.getString("phone");
				return info;
			}

			return "Customer was not found. Please, try again later";
		}

		catch (SQLException ex) {
			return "Could not get result: " + ex.getMessage();
		}
	}

	public boolean setDeliveryDate(String receipt, String date) {

		try {

			PreparedStatement ps = con
					.prepareStatement("UPDATE Purchase SET deliveredDate = TO_DATE(?, 'YY-MM-DD') WHERE receiptId = ?");
			ps.setString(1, date);
			ps.setString(2, receipt);

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected != 1) {
				System.out.println("Wrong number of rows affected: " + rowsAffected);
				con.rollback();
			} else {
				con.commit();
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Could not get result: "
					+ ex.getMessage());
		}
		return false;
	}

	public PreparedStatement getItemsFromPurchase(String receipt) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT Item.upc, title, type, category, company, year," +
					" price, quantity, (price * quantity) AS \"TOTAL PRICE\" " +
					"FROM Item, PurchaseItem " +
					"WHERE Item.upc = PurchaseItem.upc AND " +
					"PurchaseItem.receiptId = ?");
			ps.setString(1, receipt);
		} catch (SQLException e) {
			System.out.println("Could not get statement: "
					+ e.getMessage());
			return null;
		}
		return ps;
	}

	public boolean changeItemStock(String upc, String newStock) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("UPDATE Item SET stock = ? WHERE upc = ?");
			ps.setString(1, newStock);
			ps.setString(2, upc);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected != 1) {
				con.rollback();
				System.out.println("Wrong number of rows affected: " + rowsAffected);
			} else {
				con.commit();
				return true;
			}				
		} catch (SQLException ex) {
			System.out.println("Could not get result: "
					+ ex.getMessage());
		}
		return false;
	}
	
	public boolean changeItemPrice(String upc, String newPrice) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("UPDATE Item SET price = ? WHERE upc = ?");
			ps.setString(1, newPrice);
			ps.setString(2, upc);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected != 1) {
				con.rollback();
				System.out.println("Wrong number of rows affected: " + rowsAffected);
			} else {
				con.commit();
				return true;
			}				
		} catch (SQLException ex) {
			System.out.println("Could not get result: "
					+ ex.getMessage());
		}
		return false;
	}

	public boolean deleteItem(String upc) {
		try {
			PreparedStatement ps;
			ps = con.prepareStatement("DELETE FROM Item WHERE upc = ?");
			ps.setString(1, upc);

			int rowCount = ps.executeUpdate();

			if (rowCount == 1) {
				con.commit();
				return true;
			} else {
				con.rollback();
				System.out.println("Wrong number of rows affected: " + rowCount + " rows");
			}
			ps.close();
		} catch (SQLException e1) {
			System.out.println("SQL Error when deleting item: " + e1.getMessage());
		}

		return false;
	}
	
	private void updateDailySalesView(String strDate)
			throws SQLException {
		PreparedStatement ps;
		ps = con.prepareStatement("CREATE OR REPLACE VIEW daily_sales AS SELECT upc, category, price, units, (price*units) as total_value FROM item NATURAL JOIN (SELECT upc, SUM(quantity) AS units FROM purchaseItem, purchase WHERE purchaseItem.receiptId = purchase.receiptId AND TO_CHAR(purchaseDate, 'YY-MM-DD') = '"
				+ strDate
				+ "' GROUP BY upc) WHERE units > 0 ORDER BY category");
		ps.execute();
		ps.close();
	}

}
