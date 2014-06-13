package Others;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** 
 * Gets a query and displays all the table cells in a new window: 
 */

public class TableView {

	// Used to hold the column data for each row
	private Object[][] databaseInfo;

	// The column titles for the JTable
	private Object[] columns;

	private ResultSet rows;
	private ResultSetMetaData metaData;

	private DefaultTableModel dTableModel;

	private String[] buttonTitle = new String[10];
	private Action[] buttonAction = new Action[10];

	private int totalButtons = 0;

	private int totalColumns = 0;

	private JFrame frame;
	private JTable table;

	public TableView(PreparedStatement ps) {

		try {
			rows = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Table SQL PS Exception: " + e.getMessage());
		}

	}

	public TableView(Connection con, String query) {
		try {
			rows = con.createStatement().executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Table SQL Con Exception: " + e.getMessage());
		}

	}

	/** Add a button to the each row.
	 * @param event: the action of the button
	 * @param text: the title of the button
	 */
	public void addButton(String text, Action action) {
		buttonTitle[totalButtons] = text;
		buttonAction[totalButtons] = action;
		totalButtons++;
	}

	/** Call that method to return the table in a JScrollPane */
	public JScrollPane getTable() throws SQLException {
		JScrollPane pane = prepareTable();
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		return pane;
	}
	
	/** Call that method to display the table in a new popup: */
	public void show() {
		try {
			displayTable();
		} catch (SQLException e) {
			System.out.println("Show Table SQL Exception: " + e.getMessage());
		}
	}
	
	/** Set the title of the frame (after calling show): */
	public void setFrameTitle(String title) {
		if (frame == null) {
			System.out.println("You must set the title after calling show");
			return;
		}
		frame.setTitle(title);
	}
	
	/** Set the size of a column in the table: */
	public void setColumnSize(int column, int width) {
		if (table == null) {
			System.out.println("You must set the column size after displaying the table");
			return;
		}
		table.getColumnModel().getColumn(column).setPreferredWidth(width);
	}
	
	
	/* 
	 *   PRIVATE HELPER METHODS BELOW: 
	 */
	
	private void displayTable() throws SQLException {
		frame = new JFrame();
		frame.add(prepareTable(), BorderLayout.CENTER);
		frame.setMinimumSize(new Dimension(880, 0));
		frame.pack();
		frame.setVisible(true);
	}

	private JScrollPane prepareTable() throws SQLException {
		/* Get the columns: */
		metaData = rows.getMetaData();
		if (metaData == null) {
			System.out.println("TableView: Error. No row metadata.");
			return new JScrollPane(new JLabel("ERROR"));
		}

		totalColumns = metaData.getColumnCount();

		columns = new String[totalColumns + totalButtons];
		for(int i=1; i<=totalColumns; i++)
			columns[i-1] = metaData.getColumnName(i);

		for(int i=0; i<totalButtons; i++)
			columns[totalColumns + i] = "";

		dTableModel = new DefaultTableModel(databaseInfo, columns);

		// Temporarily holds the row results
		Object[] tempRow;

		while(rows.next()){            	
			tempRow = new Object[totalColumns];
			for(int i=0; i<totalColumns; i++)
				tempRow[i] = rows.getString(i+1);
			dTableModel.addRow(tempRow);
		}

		table = new JTable(dTableModel){  
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row,int column){  
				if(column < totalColumns) return false;//the 4th column is not editable  
				return true;  
			}  
		}; 


		table.setFont(new Font("Serif", Font.PLAIN, 20));

		table.setRowHeight(table.getRowHeight()+10);

		/* Sorting rows are screwing buttons up */
		table.setAutoCreateRowSorter( (totalButtons == 0) );

		for(int i=0; i<totalButtons; i++) {
			table.getColumnModel().getColumn(totalColumns + i).setPreferredWidth(100);
			new ButtonColumn(table, buttonAction[i], buttonTitle[i], totalColumns + i);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		
		return scrollPane;
	}
}