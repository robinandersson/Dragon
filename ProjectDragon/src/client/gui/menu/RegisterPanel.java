package client.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The RegisterPanel is the panel where users can type in their details and register for an account.
 * @author forssenm
 *
 */
@SuppressWarnings("serial")
public class RegisterPanel extends JPanel implements ActionListener,
		client.event.EventHandler {

	private JButton registerRegisterButton;
	private JButton registerBackButton;
	private JTextField registerUserNameField;
	private JTextField registerFirstNameField;
	private JTextField registerLastNameField;
	private JPasswordField registerPassword;
	private JPasswordField registerPasswordAgainField;
	
	private int frameHeight = P.INSTANCE.getFrameHeight();
	private int frameWidth = P.INSTANCE.getFrameWidth();
	private int margin = P.INSTANCE.getMarginSize();
	private int buttonHeight = P.INSTANCE.getButtonHeight();
	private int buttonWidth = P.INSTANCE.getButtonWidth();

	/**
	 * Creates the application
	 */
	public RegisterPanel() {
		init();
		client.event.EventBus.register(this);
	}

	@Override
	public void onEvent(client.event.Event evt) {
		if (evt.getTag().equals(client.event.Event.Tag.REGISTER_FAILED)) {
			JOptionPane.showMessageDialog(null, "Incorrect values");
			// TODO Better solution is required to show what value was "bad"
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == registerRegisterButton) {
			ArrayList<char[]> registerInfo = new ArrayList<char[]>();
			registerInfo.add(registerUserNameField.getText().toCharArray());
			registerInfo.add(registerFirstNameField.getText().toCharArray());
			registerInfo.add(registerLastNameField.getText().toCharArray());
			registerInfo.add(registerPassword.getPassword());
			registerInfo.add(registerPasswordAgainField.getPassword());
			client.event.EventBus.publish(new client.event.Event(client.event.Event.Tag.TRY_REGISTER, registerInfo));
		} else if (e.getSource() == registerBackButton) {
			client.event.EventBus.publish(new client.event.Event(client.event.Event.Tag.REGISTER_BACK, 1));
		}
	}

	private void init() {
		// JPanel registerPanel = new JPanel();
		// frame.getContentPane().add(registerPanel);
		this.setLayout(null);
		this.setBackground(P.INSTANCE.getBackground());

		JLabel registerFirstNameLabel = new JLabel("First name\r\n");
		registerFirstNameLabel.setBounds(447, 172, 110, 14);
		registerFirstNameLabel.setFont(P.INSTANCE.getLabelFont());
		this.add(registerFirstNameLabel);

		registerFirstNameField = new JTextField();
		registerFirstNameField.setBounds(447, 197, 110, 20);
		this.add(registerFirstNameField);
		registerFirstNameField.setColumns(10);

		JLabel registerLastNameLabel = new JLabel("Last name\r\n");
		registerLastNameLabel.setBounds(447, 228, 110, 14);
		registerLastNameLabel.setFont(P.INSTANCE.getLabelFont());
		this.add(registerLastNameLabel);

		registerLastNameField = new JTextField();
		registerLastNameField.setBounds(447, 253, 110, 20);
		this.add(registerLastNameField);
		registerLastNameField.setColumns(10);

		JLabel registerUserNameLabel = new JLabel("User name\r\n");
		registerUserNameLabel.setBounds(447, 284, 110, 14);
		registerUserNameLabel.setFont(P.INSTANCE.getLabelFont());
		this.add(registerUserNameLabel);

		registerUserNameField = new JTextField();
		registerUserNameField.setBounds(447, 309, 110, 20);
		this.add(registerUserNameField);
		registerUserNameField.setColumns(10);

		JLabel registerPasswordLabel = new JLabel("Password");
		registerPasswordLabel.setBounds(447, 340, 110, 14);
		registerPasswordLabel.setFont(P.INSTANCE.getLabelFont());
		this.add(registerPasswordLabel);

		registerPassword = new JPasswordField();
		registerPassword.setBounds(447, 365, 110, 20);
		this.add(registerPassword);

		JLabel registerPasswordAgainLabel = new JLabel("Password again");
		registerPasswordAgainLabel.setBounds(447, 396, 110, 20);
		registerPasswordAgainLabel.setFont(P.INSTANCE.getLabelFont());
		this.add(registerPasswordAgainLabel);

		registerPasswordAgainField = new JPasswordField();
		registerPasswordAgainField.setBounds(447, 427, 110, 20);
		this.add(registerPasswordAgainField);

		registerRegisterButton = new JButton("Register");
		registerRegisterButton.setFont(P.INSTANCE.getLabelFont());
		registerRegisterButton.setBounds(frameWidth - buttonWidth - margin, 
				frameHeight - buttonHeight - 2*margin, buttonWidth, buttonHeight);
		registerRegisterButton.addActionListener(this);
		this.add(registerRegisterButton);

		registerBackButton = new JButton("Back");
		registerBackButton.setFont(P.INSTANCE.getLabelFont());
		registerBackButton.setBounds(margin, frameHeight - buttonHeight 
				- 2*margin, buttonWidth, buttonHeight);
		registerBackButton.addActionListener(this);
		this.add(registerBackButton);
	}

}
