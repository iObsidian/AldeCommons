package alde.commons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import alde.commons.console.Console;
import alde.commons.logger.LoggerPanel;
import alde.commons.util.window.UtilityJFrame;

public class ExampleConsole {

	private JFrame frmConsole;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ExampleConsole window = new ExampleConsole();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExampleConsole() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConsole = new JFrame();
		frmConsole.setTitle("Debugging Console");
		frmConsole.setBounds(100, 100, 715, 355);
		frmConsole.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmConsole.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(new LoggerPanel(), BorderLayout.CENTER);

		frmConsole.getContentPane().add(new Console(), BorderLayout.SOUTH);

		frmConsole.setVisible(true);

		frmConsole.setLocation(0, 0);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frmConsole.setSize(screenSize);

	}

}
