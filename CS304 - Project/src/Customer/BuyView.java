package Customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

/**
 * This class is the view displayed when the user wants to buy (after clicking checkout).
 * The view asks for the credit card and expiry date. The controller should handle what
 * happens when the user clicks in the buy button here.
 */

public class BuyView extends JPanel {
	
	private static final long serialVersionUID = 1L; /* Java is asking for this */

	JButton returnBtn;
	JButton buyBtn;
	JLabel  cartInfoLb;
	JLabel cardNumberLb;
	JLabel expiryDateLb;
	JTextField cardNumber;
	JSpinner cardExpiryDate;
	
	public BuyView() {
		buyBtn = new JButton("Buy");
		returnBtn = new JButton("Return");
		cardNumber = new JTextField(16);
		cardNumberLb = new JLabel("Card Number:");
		expiryDateLb = new JLabel("Expiry Date:");
		cartInfoLb = new JLabel();

		cardExpiryDate = new JSpinner(new SpinnerDateModel(new Date(), 
				null, null, Calendar.MONTH));
		cardExpiryDate.setEditor(new JSpinner.DateEditor(cardExpiryDate, "MM/yy"));
		
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		this.setLayout(gb);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		c.gridwidth = GridBagConstraints.NONE;
		c.insets = new Insets(-200, 0, 0, 0);
		gb.setConstraints(cartInfoLb, c);
		this.add(cartInfoLb);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 0, 0, 0);
		gb.setConstraints(cardNumberLb, c);
		this.add(cardNumberLb);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 10, 0, 0);
		gb.setConstraints(cardNumber, c);
		this.add(cardNumber);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(5, 10, 0, 0);
		gb.setConstraints(expiryDateLb, c);
		this.add(expiryDateLb);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, -117, 0, 0);
		gb.setConstraints(cardExpiryDate, c);
		this.add(cardExpiryDate);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(20, 0, 0, 0);
		gb.setConstraints(returnBtn, c);
		this.add(returnBtn);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(20, 130, 0, 0);
		gb.setConstraints(buyBtn, c);
		this.add(buyBtn);
	}
	
	public void setCartInfoLbText(String text) {
		cartInfoLb.setText(text);
	}
	
	public String getCreditCard() {
		return cardNumber.getText();
	}
	
	public String getExpiryDate() {
		DateFormat df = new SimpleDateFormat("MM-yy");  
		return df.format((Date) cardExpiryDate.getValue()); 
	}
	
	public void addActionToReturnBtn(ActionListener listener) {
		returnBtn.addActionListener(listener);
	}
	
	public void addActionToBuyBtn(ActionListener listener) {
		buyBtn.addActionListener(listener);
	}
}