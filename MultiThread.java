package Multi1thread;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class PrjectQuestion1Multi {

    private static final int L = 255;
    private static final int NUM_THREADS = 30;

    public static void main(String[] args) {
        try {
        	long startTime = System.currentTimeMillis();

            // Reading original image
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\vm7mo\\Downloads\\331project\\Rain_Tree.jpg"));

            // Converting original image to grayscale
            BufferedImage grayscaleImage = convertToGrayscale(originalImage);

            // Display original image 
            displayImage(originalImage, "Original Image");

            // Display grayscale image 
            displayImage(grayscaleImage, "Grayscale Image");

            // Equalization
            BufferedImage eqImage = histogramEqualizationMultiThread(grayscaleImage);

            // Display equalized image 
            displayImage(eqImage, "Equalized Image MultiThread");
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Execution time: " + executionTime + " milliseconds");
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

    public static BufferedImage histogramEqualizationMultiThread(BufferedImage grayscaleImage) {
        int[] histogram = new int[L + 1];
        int[] cumulativeHist = new int[L + 1];

        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();

        int pixels = width * height;

        Thread[] threads = new Thread[NUM_THREADS];

        // Here we calculate the histogram using multiple threads
        for (int t = 0; t < NUM_THREADS; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                for (int i = threadId; i < height; i += NUM_THREADS) {
                    for (int j = 0; j < width; j++) {
                        int gpixel = grayscaleImage.getRGB(j, i) & 0xFF;
                        synchronized (histogram) {
                            histogram[gpixel]++;
                        }
                    }
                }
            });
            threads[t].start();
        }

        // Here we use t.join to wait
        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        cumulativeHist[0] = histogram[0];
        for (int i = 1; i < L; i++) {
            cumulativeHist[i] = cumulativeHist[i - 1] + histogram[i];
        }

        BufferedImage eqImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        threads = new Thread[NUM_THREADS];

        // Here we do the equalization
        for (int t = 0; t < NUM_THREADS; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                for (int i = threadId; i < height; i += NUM_THREADS) {
                    for (int j = 0; j < width; j++) {
                        int gpixel = grayscaleImage.getRGB(j, i) & 0xFF;
                        int EQgray = (int) (cumulativeHist[gpixel] * (L)) / pixels;
                        int EQRGB = (EQgray << 16) + (EQgray << 8) + EQgray;
                        eqImage.setRGB(j, i, EQRGB);
                    }
                }
            });
            threads[t].start();
        }

        // Waiting
        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return eqImage;
    }
}
