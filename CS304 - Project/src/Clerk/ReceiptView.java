package Clerk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

/**
 * This class handles the UI of a receipt
 */

public class ReceiptView extends JPanel{

	private static final long serialVersionUID = 1L; // java asks for this

	private String[] columnNames = {"Quantity", "Product", "Price Each"}; // columns of a receipt are always the same
	
	private Object[][] data;

	private JFrame frame;
	
	/* Sets the data for the rows of the receipt table */
    public void setRows(Object[][] data2) {
    	data = data2;   	
    }
    
    /**
     * Creates the receipt
     * @param receiptId: receipt ID of the purchase
     * @param date: date of the purchase
     * @param card: card number used for the purchase (null if none exists)
     * @param totalPrice: total price of the sale
     */
    public void setTable(int receiptId, String date, String card, float totalPrice) {
    	if (card == null) {
    		card = "";
    	}
    	else {
    		card = card.substring(card.length() - 5, card.length()); // returns last 5 digits of card
    	}
    	setLayout(new BorderLayout(10, 10));
    	JTable table = new JTable(data, columnNames);
    	table.setPreferredScrollableViewportSize(new Dimension(300, 350));
    	table.setFillsViewportHeight(true);
    	
    	table.setAutoCreateRowSorter(true); //allow rows to be sorted by the user
    	table.setEnabled(false); //disable editing
    	
    	SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yy");
    	SimpleDateFormat old = new SimpleDateFormat("yyyy-MM-dd");
    	
     	Date purchaseDate = null;		
    	try {
			purchaseDate = old.parse(date);
		} catch (ParseException e) {
			System.out.println("Receipt: WRONG DATE FORMAT");
		}
		
    	// displays the receipt ID, date of purchase and last 5 digits
    	//  of a credit card (if exists) related to the purchase
    	JTextArea information = new JTextArea("  RECEIPTID:" + receiptId + 
    			"\n  DATE OF PURCHASE: " + fm.format(purchaseDate) + "\n  CARDNO:" + card);
        
    	information.setBackground(null);
    	information.setLineWrap(true);
    	information.setBorder(null);
    	information.setWrapStyleWord(true);
    	information.setFocusable(false);
    	information.setEditable(false);
    	
		JLabel totalLb = new JLabel("  TOTAL: $" + totalPrice);
		this.add(information, BorderLayout.NORTH);
		this.add(totalLb, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane(table);
		
		this.add(scrollPane);
		
		// sets frame attributes
		frame = new JFrame();
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Receipt");
		}
}	
