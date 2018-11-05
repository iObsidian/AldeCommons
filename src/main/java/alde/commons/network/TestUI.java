package alde.commons.network;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestUI window = new TestUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 764, 309);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = GetWebsiteWorkerHandler.get().getUI();

		//String URL, String avoid, int maxAttempt, Consumer<List<String>> websiteContentConsumer

		GetWebsiteWorkerHandler.get().addTask(new GetWebsiteTask("google.com", "ajwdawdjaw", 1, new Consumer<List<String>>() {
			@Override
			public void accept(List<String> t) {
				System.out.println("Received answer : ");
			}
		}));

		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

}
