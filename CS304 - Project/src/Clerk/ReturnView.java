package Clerk;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This class handles all the UI and UI-related functions related to a return
 */

public class ReturnView extends JPanel {
	
	private static final long serialVersionUID = 1L; // java asks for this
	private JLabel upcLb, receiptIdLb, qtyLb, blank;
	private JTextField upcBox, receiptBox, qtyBox;
	private JButton processRefund;
	
	public ReturnView() {
		super(new GridLayout(8,0));
		
		// create labels
		upcLb = new JLabel("UPC:");
		receiptIdLb = new JLabel("Receipt ID:");
		qtyLb = new JLabel("Quantity:");
		blank = new JLabel("");
		
		// create text fields
		upcBox = new JTextField(15);
		receiptBox = new JTextField(15);
		qtyBox = new JTextField(15);
		
		// create buttons
		processRefund = new JButton("Process Refund");
				
		// add all UI components to the panel
		this.add(upcLb);
		this.add(upcBox);
		this.add(receiptIdLb);
		this.add(receiptBox);
		this.add(qtyLb);
		this.add(qtyBox);
		this.add(blank);
		this.add(processRefund); 
	}
	
	/* Returns the text in the UPC field */
	public String getUPCText() {
		return upcBox.getText();
	}
	
	/* Returns the text in the receipt ID field */
	public String getIDText() {
		return receiptBox.getText();
	}

	/* Returns the text in the quantity field */
	public String getQtyText() {
		return qtyBox.getText();
	}

	public void addActionToRefundButton(ActionListener listener) {
		processRefund.addActionListener(listener);
	}

	/* Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
