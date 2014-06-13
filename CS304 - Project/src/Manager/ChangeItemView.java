package Manager;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This view is a subview of ManagerView. It shows up asking to search/add an item
 * when the change item button is clicked. This view is controlled by the Manager
 * Controller.
 */

public class ChangeItemView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private JTextField title_field;
	private JButton searchBtn = new JButton("Search");
	private JButton insertBtn = new JButton("New Item");
	

	public ChangeItemView() {
		this.add(new JLabel(" Search title: "));
		title_field = new JTextField(20);
		this.add(title_field);
		this.add(searchBtn);
		this.add(insertBtn);
	}

	public void addActionToSearchItemButton(ActionListener listener) {
		searchBtn.addActionListener(listener);
	}
	
	public void addActionToInsertItemButton(ActionListener listener) {
		insertBtn.addActionListener(listener);
	}

	public String getItemTitle() {
		return title_field.getText();
	}
	
}
