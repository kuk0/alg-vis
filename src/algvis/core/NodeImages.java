package algvis.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NodeImages {
	public static BufferedImage yellow, red, black;

	public static BufferedImage load(String path) {
		java.net.URL imgURL = Buttons.class.getResource(path);
		if (imgURL != null) {
			try {
				return ImageIO.read(imgURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void loadImages() {
		yellow = load("../images/yellow.png");
		red = load("../images/red.png");
		black = load("../images/black.png");
	}
}
