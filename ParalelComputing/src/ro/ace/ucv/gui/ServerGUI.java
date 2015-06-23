package ro.ace.ucv.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class implementation for the server user interface.
 * 
 * @author gabila
 *
 */
public class ServerGUI extends JFrame {

	private JTextArea consola;
	private JScrollPane sp;

	/**
	 * Init all the user interface components.
	 */
	private void init() {
		setTitle("Server");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(500, 500);
		setResizable(false);
		JPanel panou = new JPanel();
		panou.setBackground(new Color(0xB9D3EE));
		consola = new JTextArea(20, 40);
		consola.setEditable(false);
		System.out.println("e");
		consola.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 10));
		sp = new JScrollPane(consola);
		JButton clearButton = new JButton("Empty Log");
		clearButton.setForeground(Color.DARK_GRAY);
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				consola.setText("");
			}
		});
		panou.add(sp);
		panou.add(clearButton);

		add(panou);
		refresh();
	}

	/**
	 * Refreshing all the user interface components.
	 */
	private void refresh() {
		revalidate();
		repaint();
	}

	/**
	 * Constructor. init all the user interface components.
	 */
	public ServerGUI() {
		init();
	}

	/**
	 * Puts a message on the text area.
	 * @param consoleMessage
	 */
	public void putMessage(String consoleMessage) {

		consola.setForeground(Color.red);
		consola.append(consoleMessage + "\n");
		consola.setCaretPosition(consola.getDocument().getLength());
	}
}
