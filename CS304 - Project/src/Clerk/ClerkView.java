package Clerk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The main view for the clerk. This class holds all the code for the general view and
 * when/where the purchase and return views will be shown
 */

public class ClerkView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private PurchaseView purchasePanel;
	private ReturnView returnPanel;
	
	private JPanel menuPanel, contentPanel;
	private JButton purchaseButton, returnButton;
	
	public ClerkView() {
    	this.setLayout(new BorderLayout(10, 10));
		
    	// the two main panels
    	menuPanel = new JPanel();
    	contentPanel = new JPanel();
    	
    	// purchase view related components
		purchasePanel = new PurchaseView();
		purchaseButton = new JButton("Process a Purchase");
		
		// return view related components
		returnPanel = new ReturnView();
		returnButton = new JButton("Process a Return");
		
		// menu panel layout/UI
    	menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER,70,10));
		menuPanel.add(purchaseButton);
		menuPanel.add(returnButton);
		
		this.add(menuPanel, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
		
		pur().setCardPurchaseNotVisible();
	}
	
	/* Returns the purchasePanel */
	public PurchaseView pur(){
		return purchasePanel;
	}
	
	/* Hides returnPanel and displays purchasePanel */
	public void displayPurView() {
		contentPanel.remove(returnPanel);
		contentPanel.add(purchasePanel);
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	
	/* Returns the returnPanel */
	public ReturnView ret() {
		return returnPanel;
	}
	
	/* Hides purchasePanel and displays returnPanel */
	public void displayRetView() {
		contentPanel.remove(purchasePanel);
		contentPanel.add(returnPanel);
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	
	/* It's a good practice to let the view controller handle the actions (MVC): */
	public void addActionToPurchaseButton(ActionListener listener) {
		purchaseButton.addActionListener(listener);
	}
	
	public void addActionToReturnButton(ActionListener listener) {
		returnButton.addActionListener(listener);
	}
	
	/* Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
}
