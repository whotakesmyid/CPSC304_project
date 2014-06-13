package Manager;

import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * This view is a subview of ManagerView. It allows the manager to choose a date
 * in order to display the tables related to the daily sales.
 * This view is controlled by the Manager Controller.
 */

public class DailyView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private JButton dailygenerate;
	private JSpinner dateSpinner;
	
	public DailyView() {	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel header = new JPanel();
		dailygenerate = new JButton("Generate Daily Report");
		header.add(new JLabel(
				"Enter a date before clicking on the generate button: "));
		dateSpinner = new JSpinner(new SpinnerDateModel(new Date(),
				null, null, Calendar.DAY_OF_MONTH));
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yy"));
		header.add(dateSpinner);
		header.add(dailygenerate);
		this.add(header);
	}
	
	public void addActionToGenerateButton(ActionListener listener) {
		dailygenerate.addActionListener(listener);
	}

	public String getDate() {
		DateFormat df = new SimpleDateFormat("yy-MM-dd");  
		return df.format((Date) dateSpinner.getValue()); 
	}
	
	public void disableBtn() {
		dailygenerate.setEnabled(false);
	}
	
	public void enableBtn() {
		dailygenerate.setEnabled(true);
	}

}
