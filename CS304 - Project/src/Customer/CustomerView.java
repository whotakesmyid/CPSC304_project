package Customer;

import java.awt.event.ActionListener;
import javax.swing.*;

/*
 *  ====================== INCOMPLETE CLASS (UNDER DEVELOPMENT) ====================== 
 */

public class CustomerView extends JPanel {
	
	JButton button;
	
	public CustomerView() {
		JLabel lb = new JLabel("This is the customer view.");
		this.add(lb);
		
		this.add(new JLabel("If you see this, that means the database is running."));
		this.add(new JLabel("Add stuff in here."));
		
		button = new JButton("Just a button");
		this.add(button);
	}
	
	/* It's a good practice to let the view controller handle the actions (MVC): */
	public void addActionToButton(ActionListener listener) {
		button.addActionListener(listener);
	}
	
	/* Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
}
