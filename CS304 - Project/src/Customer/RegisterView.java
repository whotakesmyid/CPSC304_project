package Customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 *  Customer Registration. Customers who access the store online for the first time, 
 *  will be asked to register by providing their personal information, 
 *  including their name, their address, phone number,  an id and a password.   
 *  If the id is already in the system, they will be asked to provide another one. 
 */

public class RegisterView extends JPanel {
	
	private static final long serialVersionUID = 1L; /* Java is asking for this */

	JButton regBtn, returnBtn;
    JTextField cnameField, caddrField, cphoneField;
    JPasswordField cpassField;
 
    
	GridBagConstraints gbc = new GridBagConstraints();
	
	public RegisterView() {

	setLayout(new GridBagLayout());
	
	// Defines padding top, left, bottom, right
	gbc.insets = new Insets(10,2,2,2);
	
	JPanel space = new JPanel();
	gbc.gridx = 0;
	gbc.gridy = 0;
	add(space, gbc);
    
    JLabel cnameLabel = new JLabel("Name: ");
	gbc.gridx = 0;
	gbc.gridy = 1;
	add(cnameLabel, gbc);
	
    cnameField = new JTextField(20);
	gbc.gridx = 1;
	gbc.gridy = 1;
	add(cnameField, gbc);
	
    JLabel caddrLabel = new JLabel("Address: ");	
	gbc.gridx = 0;
	gbc.gridy = 2;
	add(caddrLabel, gbc);
	
	caddrField = new JTextField(20);
	gbc.gridx = 1;
	gbc.gridy = 2;
	add(caddrField, gbc);
	
    JLabel cphoneLabel = new JLabel("Phone: ");
	gbc.gridx = 0;
	gbc.gridy = 3;
	add(cphoneLabel, gbc);
	
    cphoneField = new JTextField(20);
	gbc.gridx = 1;
	gbc.gridy = 3;
	add(cphoneField, gbc);
	
    JLabel cpassLabel = new JLabel("Password: ");
	gbc.gridx = 0;
	gbc.gridy = 6;
	add(cpassLabel, gbc);
    
	cpassField = new JPasswordField(20);
	cpassField.setEchoChar('*');
	gbc.gridx = 1;
	gbc.gridy = 6;
	add(cpassField, gbc);

	returnBtn = new JButton("Return");
	gbc.gridx = 0;
	gbc.gridy = 7;
	add(returnBtn, gbc);	
	
	regBtn = new JButton("Register");
	gbc.gridx = 1;
	gbc.gridy = 7;
	add(regBtn, gbc);
	
	}
	
	public void focusOnNameField() {
		cnameField.requestFocus();
	}
	
	public void addActionToRegisterBtn(ActionListener listener){
		regBtn.addActionListener(listener);
	}
	
	
	public void addActionToReturnBtn(ActionListener listener) {
		returnBtn.addActionListener(listener);
	}
	
	public String getNameText() {
		return cnameField.getText();
	}
	
	public String getAddrText() {
		return caddrField.getText();
	}
	
	public String getPhoneText() {
		return cphoneField.getText();
	}
	
	public String getPassText() {
		return new String(cpassField.getPassword());
	}
}
