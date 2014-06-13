import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * This is the main frame of the app (that supports the panels inside).
 */

public class MainView extends JFrame {
	
	/* Items in the menu bar: */
	JMenuItem menuCustomerMode;
	JMenuItem menuClerkMode;
	JMenuItem menuManagerMode;
	JMenuItem menuExit;
	
	/* View below the menu bar: */
	JPanel view;

	public MainView() {
		initUI();
	}
	
	public void changeView(JPanel panel) {
		if (view != null) 
			this.remove(view);
		view = panel;
		this.add(view); //add panel to frame
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
        setJMenuBar(menubar);
	}
}
