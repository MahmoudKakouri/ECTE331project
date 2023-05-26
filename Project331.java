package ECTE331;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Project331 {

	private static final int L = 255;

	public static void histogramEqualization(int[] inpImage, int size, int[] outImage) throws IOException {
		int[] histogram = new int[L];
		int[] cumulativeHist = new int[L];

		for (int i = 0; i < size; i++) {
			histogram[inpImage[i]]++;
		}

		cumulativeHist[0] = histogram[0];
		for (int i = 1; i <= L; i++) {
			cumulativeHist[i] = cumulativeHist[i - 1] + histogram[i];
		}

		for (int i = 0; i < size; i++) {
			outImage[i] = (cumulativeHist[inpImage[i]] * (L)) / size;
		}
	}

	public static void main(String[] args) {
		try {
			// Reading image
			File imageFile = new File("C:\\Users\\vm7mo\\Downloads\\331project\\Rain_Tree.jpg");
			Image image = ImageIO.read(imageFile);

			
			JFrame frame = new JFrame("Original Image");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Creating ImagePanel to display the image
			ImagePanel imagePanel = new ImagePanel(image);

			
			frame.getContentPane().add(imagePanel);

			// Set frame size
			frame.setSize(image.getWidth(null), image.getHeight(null));

			
			frame.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ImagePanel extends JPanel {

		private Image image;

		public ImagePanel(Image image) {
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(image.getWidth(null), image.getHeight(null));
		}
	}
}
