package Manager;

import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

/*
 *  ====================== INCOMPLETE CLASS (UNDER DEVELOPMENT) ====================== 
 */

public class ManagerView extends JPanel {
	
	JButton button;
	
	public ManagerView() {
		JLabel lb = new JLabel("This is the manager view.");
		this.add(lb);
		
		this.add(new JLabel("If you see this, that means the database is running."));
		this.add(new JLabel("Add stuff in here."));
		
		JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), 
				null, null, Calendar.DAY_OF_MONTH));
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yy"));
		this.add(dateSpinner);
		
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
