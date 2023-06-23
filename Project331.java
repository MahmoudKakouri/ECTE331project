package ECTE331;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Project331 {

	private static final int L = 255;


	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
			// Reading original image
			BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\vm7mo\\Downloads\\331project\\Rain_Tree.jpg"));

			// Converting original image to grayscale
			BufferedImage grayscaleImage = convertToGrayscale(originalImage);

			BufferedImage EQimg = histogramEqualization(grayscaleImage);

			// Display original image in a frame
			displayImage(originalImage, "Original Image");

			// Display grayscale image in a frame
			displayImage(grayscaleImage, "Grayscale Image");
			//Display EQ image in a fram
			displayImage(EQimg, "Equalized Image Single Thread");
			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;
			System.out.println("Execution time: " + executionTime + " milliseconds");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	// Function for grayscale conversion
	public static BufferedImage convertToGrayscale(BufferedImage originalImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayscaleImage.getGraphics();
		g.drawImage(originalImage, 0, 0, null);
		g.dispose();

		return grayscaleImage;
	}
	// Function to display images
	static void displayImage(BufferedImage image, String title) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImagePanel panel = new ImagePanel(image);
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
	}
// Function for the Image panel
	public static class ImagePanel extends JPanel {

		private BufferedImage image;

		public ImagePanel(BufferedImage image) {
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(image.getWidth(), image.getHeight());
		}
	}
	//Function to calculate the equalization histogram
	public static BufferedImage histogramEqualization( BufferedImage grayscaleImage) throws IOException{
		int[] histogram = new int[L+1];
		int[] cumulativeHist = new int[L+1];

		int width = grayscaleImage.getWidth();
		int height = grayscaleImage.getHeight();

		int pixels = width*height;

		for(int i= 0; i<height;i++) {
			for(int j=0; j<width;j++) {
				int gpixel =grayscaleImage.getRGB(j,i) & 0xFF;
				//System.out.println(gpixel);
				histogram[gpixel] = histogram[gpixel] + 1;
			}
		}   


		cumulativeHist[0] = histogram[0];
		for (int i = 1; i < L; i++) {
			cumulativeHist[i] = cumulativeHist[i - 1] + histogram[i];
		}
		BufferedImage EQimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int gpixel =grayscaleImage.getRGB(i,j) & 0xFF;
				int EQgray = (int) (cumulativeHist[gpixel] * (L)) / pixels;
				int EQRGB= (EQgray<<16) + (EQgray<<8) + EQgray;
				EQimg.setRGB(i, j, EQRGB);
			}
		}
		return EQimg;
	}
}
