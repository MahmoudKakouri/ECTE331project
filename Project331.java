package ECTE331;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Project331 {
    private static final int L = 255;

    public static void histogramEqualization(int[] inpImage, int size, int[] outImage) throws IOException {
        int[] histogram = new int[L];
        int[] cumulativeHist = new int[L];

        for (int i = 0; i < size; i++) {
            histogram[inpImage[i]]++;
        }

        // second step
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
            //Reading image
            BufferedImage image = ImageIO.read(new File("C:\\Users\\vm7mo\\Downloads\\331project\\Rain_Tree.jpg"));

           //Dsiplaying
            JFrame frame = new JFrame("Image Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, null);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(image.getWidth(), image.getHeight());
                }
            };

          
            frame.getContentPane().add(panel);

            frame.pack();

          
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
