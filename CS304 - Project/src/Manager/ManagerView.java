package Manager;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;


public class ManagerView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	/**
	 * Manager Panel
	 */
	private DailyView dailypanel;
	private TotalView totalpanel;
	private ChangeItemView changepanel;

	private JPanel managerpanel;
	private JButton dailysalesBtn, topsalesBtn, changeItemBtn, deliveryBtn;

	private JScrollPane lastTable;
	
	public ManagerView() {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		managerpanel = new JPanel();

		/**
		 * Daily Sales Report View
		 */
		dailypanel = new DailyView();
		dailysalesBtn = new JButton("Daily Sales Report");
		managerpanel.add(dailysalesBtn);

		/**
		 * Top Sales Report View
		 */
		totalpanel = new TotalView();
		topsalesBtn = new JButton("Top Sales Report");
		managerpanel.add(topsalesBtn);

		/**
		 * Update Delivery View
		 */
		deliveryBtn = new JButton("Update Delivery");
		managerpanel.add(deliveryBtn);

		/**
		 * Change Item View
		 */
		changepanel = new ChangeItemView();
		changeItemBtn = new JButton("Manage Items");
		managerpanel.add(changeItemBtn);

		this.add(managerpanel, BorderLayout.NORTH);

	}

	public void displayDailyView() {
		this.removeAll();
		this.add(managerpanel);
		this.add(dailypanel);
		dailypanel.enableBtn();
		this.repaint();
		this.revalidate();

	}

	/** Top sales report: */
	public void displayTotalView() {
		this.removeAll();
		this.add(managerpanel);
		this.add(totalpanel);
		this.repaint();
		this.revalidate();

	}

	public void displayChangeView() {
		this.removeAll();
		this.add(managerpanel);
		this.add(changepanel);
		this.repaint();
		this.revalidate();
	}

	public void displayDeliveryView() {
		this.removeAll();
		this.add(managerpanel);
		this.add(new JLabel("Online orders that are still to be delivered:"));
		this.repaint();
		this.revalidate();
	}

	public void addTablePanel(JScrollPane table) {
		lastTable = table;
		this.add(lastTable);
		this.repaint();
		this.revalidate();
	}
	
	public void removeTablePanel() {
		if (lastTable == null) return;
		this.remove(lastTable);
		this.repaint();
		this.revalidate();
	}

	public void addTotalLabel(double total, int items) {
		this.add(new JLabel("Total Daily Sales: $" + total + ". " + items
				+ " item(s) sold."));
		this.repaint();
		this.revalidate();
	}

	public DailyView daily_view() {
		return dailypanel;
	}

	public TotalView total_view() {
		return totalpanel;
	}

	public ChangeItemView changeitem_view() {
		return changepanel;
	}

	public void addActionToDailyButton(ActionListener listener) {
		dailysalesBtn.addActionListener(listener);
	}

	public void addActionToTopBtn(ActionListener listener) {
		topsalesBtn.addActionListener(listener);
	}

	public void addActionToChangeBtn(ActionListener listener) {
		changeItemBtn.addActionListener(listener);
	}

	public void addActionToDeliveryBtn(ActionListener listener) {
		deliveryBtn.addActionListener(listener);
	}

	/* Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}
	
	/* Display an info popup to the user: */
	public void infoPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public boolean confirmationPopup(String message) {
		return (JOptionPane.showConfirmDialog(null, message) == 0);
	}
	
	public String[] getAddItemFormDialog() {
		
		JSpinner upcSpinner = getUPCSpinner();
		JSpinner yearSpinner = getYearSpinner();
        JSpinner priceSpinner = getPriceSpinner();
        
		JTextField title = new JTextField();

		JRadioButton cdBtn = new JRadioButton("CD");
		cdBtn.setActionCommand("CD");
		JRadioButton dvdBtn = new JRadioButton("DVD");
		dvdBtn.setActionCommand("DVD");
		dvdBtn.setSelected(true);
		ButtonGroup type = new ButtonGroup();
		Box typeBox = Box.createHorizontalBox();
		typeBox.setBorder(BorderFactory.createTitledBorder("Type"));
		type.add(cdBtn);
		type.add(dvdBtn);
		typeBox.add(cdBtn);
		typeBox.add(dvdBtn);
		
		String[] categories = {"rock", "pop", "rap", "country", "classical", "new age", "instrumental"};
		JComboBox<String> category = new JComboBox<String>(categories);
		
		final JComponent[] inputs = new JComponent[] {
				new JLabel("UPC"), upcSpinner,
				new JLabel("Title"), title,
				typeBox,
				new JLabel("Category"), category,
				new JLabel("Year"), yearSpinner,
				new JLabel("Price"), priceSpinner,
				new JLabel("Company"),
		};
		
		String company = JOptionPane.showInputDialog(null, inputs, "New Item", JOptionPane.QUESTION_MESSAGE);
		if (company == null) return null;
		
		String[] values = new String[7];
		values[0] = upcSpinner.getValue().toString();
		values[1] = title.getText();
		values[2] = type.getSelection().getActionCommand();
		values[3] = (String) category.getSelectedItem();
		values[4] = company;
		values[5] = yearSpinner.getValue().toString();
		values[6] = priceSpinner.getValue().toString();
		
		return values;
	}
	
	public String getDateDialog(String title) {
		JSpinner dateSpinner = getDateSpinner();
		
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Would you like to set a date?"),
				dateSpinner,
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, title, JOptionPane.NO_OPTION);
		if (result != 0) return null; //cancelled dialog

		DateFormat df = new SimpleDateFormat("yy-MM-dd");
		return df.format((Date) dateSpinner.getValue());
	}

	private JSpinner getDateSpinner() {
		JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(),
				null, null, Calendar.DAY_OF_MONTH));
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yy"));
		return dateSpinner;
	}

	private JSpinner getUPCSpinner() {
		Random rand = new Random();
		int randUpc = rand.nextInt();
		if (randUpc < 0) randUpc *= -1; //make sure it's positive
		JSpinner upcSpinner = new JSpinner(new SpinnerNumberModel(randUpc, 1, Integer.MAX_VALUE, 1));
		JSpinner.NumberEditor noCommas = new JSpinner.NumberEditor(upcSpinner, "#"); 
		upcSpinner.setEditor(noCommas); //remove commas from the number
		return upcSpinner;
	}

	private JSpinner getYearSpinner() {
		JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2013, 1501, 2499, 1));
		JSpinner.NumberEditor noCommas2 = new JSpinner.NumberEditor(yearSpinner, "#"); 
		yearSpinner.setEditor(noCommas2); //remove commas from the number
		return yearSpinner;
	}

	private JSpinner getPriceSpinner() {
		JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(1.5, 0, 10000000, 0.01));  
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) priceSpinner.getEditor();  
        DecimalFormat format = editor.getFormat();  
        format.setMinimumFractionDigits(2);
		return priceSpinner;
	}

}
