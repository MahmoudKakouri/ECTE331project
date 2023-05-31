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
            // Reading original image
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\vm7mo\\Downloads\\331project\\Rain_Tree.jpg"));

            // Converting original image to grayscale
            BufferedImage grayscaleImage = convertToGrayscale(originalImage);

            // Display original image in a frame
            displayImage(originalImage, "Original Image");

            // Display grayscale image in a frame
            displayImage(grayscaleImage, "Grayscale Image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage convertToGrayscale(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscaleImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        return grayscaleImage;
    }

    static void displayImage(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImagePanel panel = new ImagePanel(image);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }

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
}
