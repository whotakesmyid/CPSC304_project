package Customer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/*
 *  This is the main customer view that is displayed in the customer mode.
 *  It nests any other customer subview, such as the register view and buy view.
 *  It is controlled by the Customer Controller.
 */

public class CustomerView extends JPanel {

	private static final long serialVersionUID = 1L; /* Java is asking for this */

	private JButton	regBtn;
	private RegisterView registerPanel;
	private JButton searchBtn;
	private JButton buyBtn;
	private JButton purchasesBtn;
	private BuyView buyView;


	/* Login panel: */
	private JPanel loginPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;

	/* Main panel: */
	private JLabel usernameLb;
	
	/* Search panel: */
	JPanel searchPanel;
	JComboBox<String> categoryField;
	JTextField titleField;
	JTextField singerField;
	JButton runsearchBtn; 
	JButton returnBtn;


	public CustomerView() {

		initLoginPanel();
		initMainPanel();
		initSearchPanel();

		registerPanel = new RegisterView();
		buyView = new BuyView();

	}


	/* It's a good practice to let the view controller handle the actions (MVC): */
	public RegisterView reg(){
		return registerPanel;
	}

	public void updateCartInfo(double totalPrice, int totalItems) {

		if (totalItems > 0) {
			buyView.setCartInfoLbText("You have " + totalItems + " item(s) in your cart. Price: $" 
					+ totalPrice);

		} else {
			buyView.setCartInfoLbText("Your cart is empty. " +
					"To buy an item, you must first search for it.");
		}

	}

	public void displayReg() {
		this.remove(loginPanel);
		this.add(registerPanel);
		this.repaint();
		this.revalidate();
		registerPanel.focusOnNameField();
	}

	public void hideReg() {
		this.remove(registerPanel);
		this.add(loginPanel);
		this.repaint();
		this.revalidate();
		usernameField.requestFocus();
	}

	public void displayMain() {
		this.removeAll();
		startMainPanel();
	}

	public void displaySearch() {
		this.removeAll();
		this.add(searchPanel);
		this.repaint();
		this.revalidate();
	}

	public void hideSearch() {
		this.removeAll();
		startMainPanel();
	}


	public void setUsernameLb(String text) {
		usernameLb.setText(text);
	}

	public void addActionToRegBtn(ActionListener listener) {
		regBtn.addActionListener(listener);
	}

	public void addActionToLoginBtn(ActionListener listener) {
		loginButton.addActionListener(listener);
	}

	public void addActionToBuyBtn(ActionListener listener) {
		buyBtn.addActionListener(listener);
	}

	public void addActionToSearchBtn(ActionListener listener) {
		searchBtn.addActionListener(listener);
	}

	public void addActionToPurchasesBtn(ActionListener listener) {
		purchasesBtn.addActionListener(listener);
	}

	public void addActionToSearchReturnBtn(ActionListener listener) {
		returnBtn.addActionListener(listener);
	}

	public void addActionToRunSearchBtn(ActionListener listener) {
		runsearchBtn.addActionListener(listener);
	}

	public BuyView getBuyView() {
		return buyView;
	}

	public void displayBuyView() {
		this.removeAll();
		this.add(buyView);
		this.repaint();
		this.revalidate();
	}

	public void hideBuyView() {
		this.removeAll();
		startMainPanel();
		this.repaint();
		this.revalidate();
	}

	/** Display a warning popup to the user: */
	public void warningPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	/** Display an info popup to the user: */
	public void infoPopup(String message) {
		JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/** Display a confirmation popup to the user. @return true if confirmed: */
	public boolean confirmationPopup(String message) {
		return (JOptionPane.showConfirmDialog(null, message) == 0);
	}

	public String getUsernameText() {
		return usernameField.getText();
	}

	public String getPasswordText() {
		return new String(passwordField.getPassword());
	}

	private void initLoginPanel() {

		loginPanel = new JPanel();

		JLabel usernameLabel = new JLabel("Name: ");
		JLabel passwordLabel = new JLabel("Password: ");

		usernameField = new JTextField("Rodrigo", 15);
		passwordField = new JPasswordField("latenight", 15);
		passwordField.setEchoChar('*');

		loginButton = new JButton("Log In");
		regBtn = new JButton("Register");

		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		loginPanel.setLayout(gb);
		loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the username label 
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(70, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		loginPanel.add(usernameLabel);

		// place the text field for the username 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(70, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		loginPanel.add(usernameField);

		// place password label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		loginPanel.add(passwordLabel);

		// place the password field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		loginPanel.add(passwordField);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 0, 20);
		gb.setConstraints(regBtn, c);
		loginPanel.add(regBtn);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(-26, 160, 0, 0);
		gb.setConstraints(loginButton, c);
		loginPanel.add(loginButton);

		this.add(loginPanel);
	}

	private void initMainPanel() {
		usernameLb = new JLabel();
		searchBtn = new JButton("Search Items");
		buyBtn = new JButton("Checkout");
		purchasesBtn = new JButton("Purchases");

	}
	
	private void startMainPanel() {
		
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel header = new JPanel();
        header.add(usernameLb);
		header.add(searchBtn);		
		header.add(buyBtn);
		header.add(purchasesBtn);
		this.add(header);
		this.repaint();
		this.revalidate();
	}
	
	public void displayCart(JScrollPane cartPane) {
		JLabel cartInfo = new JLabel("Cart:");
		cartInfo.setPreferredSize(new Dimension(100,20));
		this.add(cartInfo);
		cartPane.setPreferredSize(new Dimension(800,350));
		this.add(cartPane);
		this.repaint();
		this.revalidate();
	}

	private void initSearchPanel() {

		searchPanel = new JPanel();

		JLabel infoLabel = new JLabel(" You can search by any of the three fields below. " +
				"Leave it blank to search for all items: ");
		JLabel categoryLabel = new JLabel(" Category: ");
		
		JLabel titleLabel = new JLabel("Albumn Title: ");
		JLabel singerLabel = new JLabel("Artist Name: ");

		String[] categories = {"All", "Rock", "Pop", "Rap", "Country", "Classical", "New age", "Instrumental"};
		categoryField = new JComboBox<String>(categories);
		
		titleField = new JTextField(20);
		singerField =  new JTextField(20);

		runsearchBtn = new JButton("Search Item");
		returnBtn = new JButton("Return");

		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		searchPanel.setLayout(gb);
		searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		c.gridwidth = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 0, 0);
		gb.setConstraints(infoLabel, c);
		searchPanel.add(infoLabel);

		// place the Category label 
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(100, 50, 5, -30);
		gb.setConstraints(categoryLabel, c);
		searchPanel.add(categoryLabel);

		// place the text field for the category 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(100, -120, 5, 10);
		gb.setConstraints(categoryField, c);
		searchPanel.add(categoryField);

		// place Albumn title label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 50, 5, -30);
		gb.setConstraints(titleLabel, c);
		searchPanel.add(titleLabel);

		// place the title field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 5, 5, 10);
		gb.setConstraints(titleField, c);
		searchPanel.add(titleField);

		// place Singer title label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 50, 5, -30);
		gb.setConstraints(singerLabel, c);
		searchPanel.add(singerLabel);

		// place the singer field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 5, 5, 10);
		gb.setConstraints(singerField, c);
		searchPanel.add(singerField);


		// add return 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(20, -40, 0, 20);
		gb.setConstraints(returnBtn, c);
		searchPanel.add(returnBtn);

		//add run search btn 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(-26, 205, 0, 0);
		gb.setConstraints(runsearchBtn, c);
		searchPanel.add(runsearchBtn);

		//this.add(searchPanel);
	}

}
