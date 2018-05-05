package alde.commons.console;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import alde.commons.util.autoCompleteJTextField.UtilityJTextField;
import alde.commons.util.math.LevenshteinDistance;

/**
 * Console Singleton
 * 
 * Offers input for the user to trigger actions (@see ConsoleAction).
 * 
 * Use Console.get().addAction(ConsoleAction) to add actions
 * 
 * @author Alde
 */
@SuppressWarnings("serial")
public class Console extends UtilityJTextField {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(Console.class);

	/** Hint on the JTextField (disappears on input) */
	public static String HINT = "Enter command here";

	/** If a user inputs a wrong command, and another command matches by 3 characters, suggest the command. */
	public static int COMMAND_DISTANCE_MIN = 3;

	/** Singleton instance, use getConsole() to get */
	private static Console console;

	/** List of actions */
	private static final List<ConsoleAction> actions = new ArrayList<>();

	private Console() {
		super(HINT, true);

		setFont(getFont().deriveFont(Font.BOLD));
		setBackground(Color.WHITE);
		setForeground(new Color(29, 29, 29)); // Very dark gray
		setCaretColor(Color.WHITE);

		Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
		Border empty = new EmptyBorder(5, 5, 5, 0);
		CompoundBorder border = new CompoundBorder(line, empty);
		setBorder(border);
	}

	/**
	 * @return Console singleton
	 */
	public static Console get() {

		if (console == null) {
			console = new Console();
			console.addAction(new HelpAction(actions));

			/**
			 * Receive input
			 */
			console.addReceiver(command -> {

				boolean accepted = false;

				for (ConsoleAction t : actions) {
					for (String s : t.getKeywords()) {
						if (command.contains(s)) {
							accepted = true;
							t.accept(command);
							break;
						}
					}
				}

				/**
				 * Use levenshtein to get the closest match to the user command.
				 * 
				 * If the closest match is too far away, do not suggest anything.
				 */
				if (!accepted) {

					log.error("No action found for input '" + command + "'.");

					int closest = Integer.MAX_VALUE;
					String suggestion = "";

					for (ConsoleAction c : actions) {

						for (String keyword : c.getKeywords()) {
							int distance = LevenshteinDistance.computeLevenshteinDistance(keyword, command);

							if (distance < closest) {
								closest = distance;
								suggestion = keyword;
							}
						}

					}

					if (closest <= COMMAND_DISTANCE_MIN) {
						log.error("Did you mean '" + suggestion + "'?");
					}
				}

			});

		}

		return console;
	}

	/**
	 * Use this static method to addAction listeners.
	 * 
	 * Console.addAction(ConsoleAction)
	 * 
	 * For an example of implementation, @see HelpAction
	 */
	public void addAction(ConsoleAction c) {

		Iterator<ConsoleAction> it = actions.iterator();
		while (it.hasNext()) {

			ConsoleAction otherAction = it.next();

			for (String keyword : c.getKeywords()) {

				for (String otherKeyword : otherAction.getKeywords()) {
					if (otherKeyword.equalsIgnoreCase(keyword)) {
						log.error("New keyword '" + c.toString() + "' overrides other action '" + c.toString() + "'.");
						it.remove();
					}
				}
			}
		}

		// Adds the keyWords of the action to the autoCompleteService
		get().getCompletionService().addData(c.getKeywords());
		actions.add(c);

	}

}

/** ConsoleAction that shows a list of all available ConsoleActions */
class HelpAction extends ConsoleAction {

	static org.slf4j.Logger log = LoggerFactory.getLogger(HelpAction.class);

	List<ConsoleAction> consoleActions = new ArrayList<ConsoleAction>();

	public HelpAction(List<ConsoleAction> consoleActions) {
		super();
		this.consoleActions = consoleActions;
	}

	@Override
	public void accept(String command) {
		for (ConsoleAction c : consoleActions) {
			log.info(c.toString());
		}
	}

	@Override
	public String getDescription() {
		return "Shows the help function";
	}

	@Override
	public String[] getKeywords() {
		return new String[] { "help" };
	}

}