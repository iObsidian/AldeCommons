package alde.commons.util.jtextfield.autocomplete;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoCompleteDocument extends PlainDocument {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Completion service.
	 */
	private CompletionService<?> completionService;

	/**
	 * The document owner.
	 */
	private JTextComponent documentOwner;

	/**
	 * Create a new <code>AutoCompletionDocument</code>.
	 *
	 * @param service       the service to use when searching for completions
	 * @param documentOwner the document owner
	 */
	public AutoCompleteDocument(CompletionService<?> service, JTextComponent documentOwner) {
		this.completionService = service;
		this.documentOwner = documentOwner;
	}

	/**
	 * Look up the completion string.
	 *
	 * @param str the prefix string to complete
	 * @return the completion or <code>null</code> if completion was found.
	 */
	protected String complete(String str) {
		Object o = this.completionService.autoComplete(str);
		return o == null ? null : o.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if ((str == null) || (str.length() == 0)) {
			return;
		}

		String text = this.getText(0, offs); // Current text.
		String completion = this.complete(text + str);
		int length = offs + str.length();
		if ((completion != null) && (text.length() > 0)) {
			str = completion.substring(length - 1);
			super.insertString(offs, str, a);
			this.documentOwner.select(length, this.getLength());
		} else {
			super.insertString(offs, str, a);
		}
	}
}