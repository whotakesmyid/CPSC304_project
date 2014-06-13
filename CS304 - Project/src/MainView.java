import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

/**
 * This is the main frame of the app (that supports the panels inside).
 */

public class MainView extends JFrame {
	
	private static final long serialVersionUID = 1L; /* Java is asking for this */
	
	/* Items in the file menu bar: */
	JMenuItem menuCustomerMode;
	JMenuItem menuClerkMode;
	JMenuItem menuManagerMode;
	JMenuItem menuExit;
	
	/* Items in the table menu bar: */
	JMenuItem menuItemTable;
	JMenuItem menuLeadSingerTable;
	JMenuItem menuHasSongTable;
	JMenuItem menuCustomerTable;
	JMenuItem menuPurchaseTable;
	JMenuItem menuPurchaseItemTable;
	JMenuItem menuReturnTable;
	JMenuItem menuReturnItemTable;
	JMenuItem menuCustomQuery;
	
	/* View below the menu bar: */
	JPanel view;

	public MainView() {
		initUI();
	}
	
	public void changeView(JPanel panel) {
		if (view != null) 
			this.remove(view);
		view = panel;
		this.getContentPane().add(view);
		//this.add(view); //add panel to frame
		this.setVisible(true); //refresh the frame
	}
	
	public void addActionToMenuCustomerMode(ActionListener listener) {
		menuCustomerMode.addActionListener(listener);
	}
	
	public void addActionToMenuClerkMode(ActionListener listener) {
		menuClerkMode.addActionListener(listener);
	}
	
	public void addActionToMenuManagerMode(ActionListener listener) {
		menuManagerMode.addActionListener(listener);
	}
	
	public void addActionToMenuExitMode(ActionListener listener) {
		menuExit.addActionListener(listener);
	}
	
	public void addActionToMenuItemTable(ActionListener listener) {
		menuItemTable.addActionListener(listener);
	}
	
	public void addActionToMenuLeadSingerTable(ActionListener listener) {
		menuLeadSingerTable.addActionListener(listener);
	}
	
	public void addActionToMenuHasSongTable(ActionListener listener) {
		menuHasSongTable.addActionListener(listener);
	}
	
	public void addActionToMenuCustomerTable(ActionListener listener) {
		menuCustomerTable.addActionListener(listener);
	}
	
	public void addActionToMenuPurchaseTable(ActionListener listener) {
		menuPurchaseTable.addActionListener(listener);
	}
	
	public void addActionToMenuPurchaseItemTable(ActionListener listener) {
		menuPurchaseItemTable.addActionListener(listener);
	}
	
	public void addActionToMenuReturnTable(ActionListener listener) {
		menuReturnTable.addActionListener(listener);
	}
	
	public void addActionToMenuReturnItemTable(ActionListener listener) {
		menuReturnItemTable.addActionListener(listener);
	}
	
	public void addActionToMenuCustomQuery(ActionListener listener) {
		menuCustomQuery.addActionListener(listener);
	}
	
	public String inputPopup(String message) {
		return JOptionPane.showInputDialog(null, message, "Input", JOptionPane.QUESTION_MESSAGE);
	}
	
	public void errorPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	
	/* 
	 *   PRIVATE HELPER METHODS BELOW: 
	 */

	private void initUI() {
		this.setSize(640,480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close application when the exit button is pressed
		this.setTitle("CS304 - Project");
		this.setLocationRelativeTo(null); //centralize window
		initMenuBar();		
	}
	
	private void initMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		ButtonGroup group = new ButtonGroup();

		//Add customer mode button:
		menuCustomerMode = new JRadioButtonMenuItem("Customer Mode");
		group.add(menuCustomerMode);
		menuCustomerMode.setMnemonic(KeyEvent.VK_C);
		menuCustomerMode.setToolTipText("Open the view that the online customer would see");
		file.add(menuCustomerMode);

		//Add clerk mode button:
		menuClerkMode = new JRadioButtonMenuItem("Clerk Mode");
		group.add(menuClerkMode);
		menuClerkMode.setMnemonic(KeyEvent.VK_K);
		menuClerkMode.setToolTipText("Open the view that the in store clerk would see");
		file.add(menuClerkMode);

        //Add manager mode button:
        menuManagerMode = new JRadioButtonMenuItem("Manager Mode");
        group.add(menuManagerMode);
        menuManagerMode.setMnemonic(KeyEvent.VK_M);
        menuManagerMode.setToolTipText("Open the view that the store manager would see");
        file.add(menuManagerMode);

        //Add exit button:
        file.addSeparator();
        menuExit = new JMenuItem("Exit");
        menuExit.setMnemonic(KeyEvent.VK_E);
        menuExit.setToolTipText("Exit application");
        file.add(menuExit);
        
        menubar.add(file);
        
        JMenu table = new JMenu("Table");
		file.setMnemonic(KeyEvent.VK_T);

		//Add query items:
		menuItemTable = new JMenuItem("Item");
		table.add(menuItemTable);
		menuLeadSingerTable = new JMenuItem("Lead Singer");
		table.add(menuLeadSingerTable);
		menuHasSongTable = new JMenuItem("Has Song");
		table.add(menuHasSongTable);
		menuCustomerTable = new JMenuItem("Customer");
		table.add(menuCustomerTable);
		menuPurchaseTable = new JMenuItem("Purchase");
		table.add(menuPurchaseTable);
		menuPurchaseItemTable = new JMenuItem("Purchase Item");
		table.add(menuPurchaseItemTable);
		menuReturnTable = new JMenuItem("Return");
		table.add(menuReturnTable);
		menuReturnItemTable = new JMenuItem("Return Item");
		table.add(menuReturnItemTable);
		table.addSeparator();
		menuCustomQuery = new JMenuItem("Run Query");
		table.add(menuCustomQuery);
		menubar.add(table);
		
        setJMenuBar(menubar);
	}
}
