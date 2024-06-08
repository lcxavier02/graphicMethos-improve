import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Pixel extends JFrame {
  private static BufferedImage buffer;
  private Graphics2D graphsPixel;
  private Map<Point, Color> paintedPixels;

  public Pixel(int windowWidth, int windowHeight) {
    buffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
    graphsPixel = buffer.createGraphics();
    graphsPixel.setColor(Color.WHITE);
    graphsPixel.fillRect(0, 0, windowWidth, windowHeight);
    graphsPixel.dispose();
    paintedPixels = new HashMap<>();

    setSize(windowWidth, windowHeight);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    getContentPane().setBackground(Color.WHITE);
  }

  public void showWindow() {
    setVisible(true);
  }

  public void putPixel(int x, int y, Color c) {
    buffer.setRGB(x, y, c.getRGB());
    paintedPixels.put(new Point(x, y), c);
    repaint();
  }

  public Color getPixelColor(int x, int y) {
    System.out.println("Pixel color: " + paintedPixels.get(new Point(x, y)));
    return paintedPixels.get(new Point(x, y));
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(buffer, 0, 0, this);
  }

  public static void main(String[] args) {
    Pixel p = new Pixel(300, 300);
    p.putPixel(200, 150, Color.RED);
    p.getPixelColor(100, 100);
    p.showWindow();
  }
}
