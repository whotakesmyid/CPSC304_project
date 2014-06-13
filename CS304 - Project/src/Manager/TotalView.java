package Manager;

import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * This view is a subview of ManagerView. It allows the manager to choose a date
 * in order to display the most sold items of the day.
 * This view is controlled by the Manager Controller.
 */

public class TotalView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private JButton totalGenerateBtn;
	private JSpinner dateSpinner;
	
	public TotalView() {
		this.add(new JLabel(
				"Enter a date before clicking on the generate report button:"));

		dateSpinner = new JSpinner(new SpinnerDateModel(new Date(),
				null, null, Calendar.DAY_OF_MONTH));
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yy"));
		this.add(dateSpinner);

		totalGenerateBtn = new JButton("Generate Report");
		this.add(totalGenerateBtn);

	}

	public void addActionToGenerateButton(ActionListener listener) {
		totalGenerateBtn.addActionListener(listener);
	}

	public String getDate() {
		DateFormat df = new SimpleDateFormat("yy-MM-dd");
		return df.format((Date) dateSpinner.getValue());
	}
	
}
