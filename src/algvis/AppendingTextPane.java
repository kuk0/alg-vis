package algvis;

import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

public class AppendingTextPane extends JTextPane {
	private static final long serialVersionUID = 8208504383603950388L;

	public AppendingTextPane() {
		super();
	}

	public AppendingTextPane(StyledDocument doc) {
		super(doc);
	}

	// Appends text to the document and ensure that it is visible
	public void appendText(String text) {
		try {
			Document doc = getDocument();

			// Move the insertion point to the end
			setCaretPosition(doc.getLength());

			// Insert the text
			replaceSelection(text);

			// Convert the new end location
			// to view co-ordinates
			Rectangle r = modelToView(doc.getLength());

			// Finally, scroll so that the new text is visible
			if (r != null) {
				scrollRectToVisible(r);
			}
		} catch (BadLocationException e) {
			System.out.println("Failed to append text: " + e);
		}
	}
}
