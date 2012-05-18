package client.gui.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.event.*;

/**
 * LoginPanel is the panel in which users can login to the application
 * @author forssenm
 *
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel implements KeyListener, ActionListener, client.event.EventHandler {

	private JTextField loginNameField;
	private JPasswordField loginPasswordField;
	private JButton loginButton;
	private JButton loginRegisterButton;
	private JLabel errorLabel;
	private String background = P.INSTANCE.getBackgroundImage();
	private float transparency = P.INSTANCE.getTransparency();
	
	private int frameHeight = P.INSTANCE.getFrameHeight();
	private int frameWidth = P.INSTANCE.getFrameWidth();
	private int buttonHeight = P.INSTANCE.getButtonHeight();
	private int buttonWidth = P.INSTANCE.getButtonWidth();
	private int margin = P.INSTANCE.getMarginSize();

	
	/**
	 * Creates the panel
	 */
	public LoginPanel() {
		init();
		client.event.EventBus.register(this);
	}

	@Override
	public void onEvent(Event evt) {
		if (evt.getTag().equals(Event.Tag.LOGIN_FAILED)) {
			errorLabel.setText("Incorrect username or password");
		} else if(evt.getTag().equals(Event.Tag.LOGIN_SUCCESS)) {
			errorLabel.setText("");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			ArrayList<char[]> loginInfo = new ArrayList<char[]>();
			loginInfo.add(loginNameField.getText().toCharArray());
			loginInfo.add(loginPasswordField.getPassword());
			EventBus.publish(new Event(Event.Tag.TRY_LOGIN, loginInfo));
		} else if (e.getSource() == loginRegisterButton) {
			EventBus.publish(new Event(Event.Tag.GO_TO_REGISTER, 1));
		}
	}

	private void init() {
		this.setLayout(null);
		this.setOpaque(false);
		
		JLabel loginNameLabel = new JLabel("User name");
		loginNameLabel.setBounds(447, 274, 108, 14);
		this.add(loginNameLabel);
		loginNameLabel.setFont(P.INSTANCE.getLabelFont());

		loginNameField = new JTextField();
		loginNameField.setBounds(447, 299, 113, 20);
		this.add(loginNameField);
		loginNameField.setToolTipText("Type in your account name here\r\n");
		loginNameField.setColumns(10);
		loginNameLabel.setLabelFor(loginNameField);

		JLabel loginPasswordLabel = new JLabel("Password\r\n");
		loginPasswordLabel.setBounds(447, 330, 108, 14);
		this.add(loginPasswordLabel);
		loginPasswordLabel.setFont(P.INSTANCE.getLabelFont());

		loginPasswordField = new JPasswordField();
		loginPasswordField.setBounds(447, 355, 113, 20);
		loginPasswordField.addKeyListener(this);
		this.add(loginPasswordField);

		loginButton = new JButton("Login");
		loginButton.setBounds(frameWidth - buttonWidth - margin, 
				frameHeight - buttonHeight - 2*margin, buttonWidth, buttonHeight);
		loginButton.addActionListener(this);
		this.add(loginButton);
		loginButton.setFont(P.INSTANCE.getLabelFont());

		JLabel noAccountLabel = new JLabel("Don't have an account?" + "\n" + "");
		noAccountLabel.setBounds(447, 386, 113, 14);
		noAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		this.add(noAccountLabel);

		loginRegisterButton = new JButton("Register");
		loginRegisterButton.setBounds(447, 401, 113, 23);
		loginRegisterButton.addActionListener(this);
		this.add(loginRegisterButton);
		
		errorLabel = new JLabel();
		errorLabel.setBounds(447, 249, 208, 14);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		errorLabel.setForeground(Color.red);
		this.add(errorLabel);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
			ArrayList<char[]> loginInfo = new ArrayList<char[]>();
			loginInfo.add(loginNameField.getText().toCharArray());
			loginInfo.add(loginPasswordField.getPassword());
			EventBus.publish(new Event(Event.Tag.TRY_LOGIN, loginInfo));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void paintComponent(Graphics g) {
		Image im = loadTranslucentImage(background, transparency);
		g.drawImage(im, 0, 0, frameWidth, frameHeight, null);
	}

	/**
	 * Return a translucent bufferedImage with transparency "transperancy"
	 * 
	 * @param url
	 * @param transperancy
	 * @return
	 * @author lisastenberg
	 */
	public static BufferedImage loadTranslucentImage(String url,
			float transparency) {
		BufferedImage loaded;
		try {
			loaded = ImageIO.read(new File(url));
			BufferedImage aimg = new BufferedImage(loaded.getWidth(),
					loaded.getHeight(), BufferedImage.TRANSLUCENT);
			Graphics2D g = aimg.createGraphics();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					transparency));
			g.drawImage(loaded, null, 0, 0);
			g.dispose();
			return aimg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
