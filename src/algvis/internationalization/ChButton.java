package algvis.internationalization;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JButton;

public class ChButton extends JButton {
	private static final long serialVersionUID = 6239957285446549335L;

	public ChButton(String text) {
		super(text);
	}

	public void refresh() {
		FontMetrics metrics = getFontMetrics(getFont());
		int width = metrics.stringWidth(getText());
		int height = metrics.getHeight();
		Dimension newDimension = new Dimension(width + 40, height + 10);
		setPreferredSize(newDimension);
		setBounds(new Rectangle(getLocation(), getPreferredSize()));
	}
}
