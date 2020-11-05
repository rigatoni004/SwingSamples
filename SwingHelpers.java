
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingHelpers {

	/** Creates an image label scaled to the given size. */
	public static JLabel createScaledImage (String filename, int width, int height) {
		Image originalImage = new ImageIcon(filename).getImage();
		Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(scaledImage));
	}
}
