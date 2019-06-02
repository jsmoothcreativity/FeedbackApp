package feedback.faces.survey.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import feedback.faces.rest.client.Answer;
import feedback.faces.rest.client.Consumer;

public class GUI extends JFrame {
	
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This Instance of the Feedback Faces Graphic User Interface
	 */
	private static GUI gui = null;
	/**
	 * The Content Pane for this GUI
	 */
	private static JPanel contentPane;
	/**
	 * Email Text
	 */
	private static JTextField txtEmail;
	/**
	 * Comment Text
	 */
	private static JTextField txtComments;
	/**
	 * All of the Buttons for this Graphic User Interface
	 */
	private static ArrayList<JButton> buttons = new ArrayList<JButton>();
	/**
	 * All of the Icons
	 */
	private static ArrayList<Icon> icons = new ArrayList<Icon>();
	/**
	 * The Response Value for the Current Button
	 */
	private static String response = null;
	/**
	 * ID of the Question Associated with this Answer
	 */
	private static String questionID = null;
	/**
	 * URL of the Feedback Faces REST Service to Target
	 */
	private static final String url = "http://localhost:8080/answers";
	/**
	 * Number of Faces
	 */
	private static final int NUMBER_OF_FACES = 5;
	/**
	 * Default Text Value for the Email Field
	 */
	private static final String DEFAULT_EMAIL_TEXT = "(optional)";
	/**
	 * Default Comment Value for the Comment Field
	 */
	private static final String DEFAULT_COMMENT_TEXT = "(optional)";
	
	/**
	 * @return Instance of the Feedback Faces Graphic User Interface (GUI)
	 */
	public static GUI getInstance() {
		if (gui == null) {
			gui = new GUI();
			gui.initialize();
			return gui;
		} else {
			return gui;
		}
	}
	
	/**
	 * Initializes the Graphic User Interface
	 */
	private void initialize() {
		
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setBounds(100, 100, 1500, 1000);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0,0));
		gui.setContentPane(contentPane);
		
		icons.add(new ImageIcon(getClass().getClassLoader().getResource("face1.png")));
		icons.add(new ImageIcon(getClass().getClassLoader().getResource("face2.png")));
		icons.add(new ImageIcon(getClass().getClassLoader().getResource("face3.png")));
		icons.add(new ImageIcon(getClass().getClassLoader().getResource("face4.png")));
		icons.add(new ImageIcon(getClass().getClassLoader().getResource("face5.png")));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		for (int i = 0; i < NUMBER_OF_FACES; i++) {
			response = Integer.toString(i + 1);
			JButton button = new JButton("face" + Integer.toString(i+1), icons.get(i));
			button.addActionListener(new FaceButtonListener());
			buttons.add(button);
		}
		
		for (JButton button : buttons) {
			button.setBorder(null);
			panel.add(button);
		}
		
		JPanel textPanel = new JPanel();
		contentPane.add(textPanel, BorderLayout.NORTH);
		
		JLabel emailLabel = new JLabel("Email:");
		textPanel.add(emailLabel);
		
		txtEmail = new JTextField();
		txtEmail.setText("(optional)");
		txtEmail.setColumns(10);
		textPanel.add(txtEmail);
		
		JLabel commentLabel = new JLabel("Comments:");
		textPanel.add(commentLabel);
		
		txtComments = new JTextField();
		txtComments.setText("(optional)");
		txtComments.setColumns(10);
		textPanel.add(txtComments);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questionID = JOptionPane.showInputDialog("Please Enter the ID of Your Question:");
			}
		});
		
		textPanel.add(settingsButton);
		
	}
	
	private void sendToApi(String response) {
		if (questionID == null) {
			JOptionPane.showMessageDialog(getParent(), "Please Enter a Valid Question ID...");
			return;
		}
		Answer answer = new Answer();
		answer.setId(null);
		answer.setQuestionId(questionID);
		answer.setResponse(response);
		Consumer consumer = new Consumer();
		consumer.sendPost(answer, url);
	}
	
	/**
	 * Exists Only to Prevent Instantiation
	 */
	private GUI() {
		// Intentionally Empty
	}
	
	/**
	 * Action Listener for Face Buttons
	 */
	public class FaceButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendToApi(response);
			txtEmail.setText(DEFAULT_EMAIL_TEXT);
			txtComments.setText(DEFAULT_COMMENT_TEXT);
		}
	}
	
}
