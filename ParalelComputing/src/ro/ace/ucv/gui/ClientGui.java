package ro.ace.ucv.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ro.ace.ucv.Volunteer;
import ro.ace.ucv.Task;

/**
 * Class implementation for the Volunteer user interface.
 * @author gabi
 *
 */
public class ClientGui extends JFrame implements ConnectionListener {

	private JTable table;
	private TaskTableModel tableModel;
	private Volunteer client;


	private void init() {
		setTitle("Computing time");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(800, 500);
		setResizable(false);
		JPanel panou = new JPanel();
		panou.setBackground(new Color(0xB9D3EE));
		panou.setLayout(new FlowLayout(FlowLayout.CENTER, 100,
				this.getHeight() / 2 - 10));
		JLabel label = new JLabel("Conecting...");
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Color.RED);
		panou.add(label);
		this.setContentPane(panou);
		refresh();
	}

	public static void main(String[] args) {

		new ClientGui();
	}
	
	/**
	 * Constructor initialize all the user interface components and creates the Volunteer model.
	 * 
	 */
	public ClientGui() {
		init();
		client = new Volunteer(this);
		client.run();

	}
	
	private void refresh() {
		revalidate();
		repaint();
	}

	private JTable createTable(List<Task> task) {

		String columnNames[] = { "File Name", "Free Space Necesary",
				"CPU TIME", "Free memory neded" };
		tableModel = new TaskTableModel(columnNames);
		table = new JTable();
		table.setModel(tableModel);

		return table;
	}

	@Override
	public void fillTable(List<Task> tasks) {

		final JTable table = createTable(tasks);
		JPanel panou = new JPanel();
		panou.setBackground(new Color(0xB9D3EE));
		panou.add(new JScrollPane(table));

		for (Task task : tasks) {
			tableModel.addTask(task);
		}

		JButton button = new JButton("Start Working");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = (String) tableModel.getValueAt(
						table.getSelectedRow(), 0);
				try {
					client.requestFile(fileName);
					client = new Volunteer(ClientGui.this);
					client.run();
				} catch (IOException e1) {
					errorOccured();
				}
			}
		});

		panou.add(button);
		this.setContentPane(panou);
		refresh();
	}

	@Override
	public void succesfullConnected() {

		JPanel panou = new JPanel();
		panou.setBackground(new Color(0xB9D3EE));
		panou.setLayout(new FlowLayout(FlowLayout.CENTER, 100,
				this.getHeight() / 2 - 10));
		JLabel label = new JLabel("Connected..");
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Color.GREEN);
		panou.add(label);
		this.setContentPane(panou);
		refresh();
	}

	@Override
	public void errorOccured() {
		JPanel panou = new JPanel();
		panou.setBackground(new Color(0xB9D3EE));
		panou.setLayout(new FlowLayout(FlowLayout.CENTER, 100,
				this.getHeight() / 2 - 10));
		JLabel label = new JLabel("Server Not Responding..");
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Color.RED);

		panou.add(label);
		this.setContentPane(panou);
		refresh();
	}
}
