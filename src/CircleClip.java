import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CircleClip extends JFrame {
  private final int WIDTH = 800;
  private final int HEIGHT = 600;
  private BufferedImage buffer;
  private Image image;
  private Graphics graphBuffer;

  private final int LEFT = 100;
  private final int RIGHT = 500;
  private final int TOP = 100;
  private final int BOTTOM = 400;

  public CircleClip() {
    setTitle("Circle Clip");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    if (buffer == null) {
      buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
    graphBuffer = buffer.createGraphics();
    graphBuffer.setColor(Color.WHITE);
    graphBuffer.fillRect(0, 0, WIDTH, HEIGHT);
    graphBuffer.dispose();

    drawRectOutline(LEFT, TOP, RIGHT, BOTTOM);

    setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    image = createImage(getWidth(), getHeight());
    g.drawImage(buffer, 0, 0, this);
  }

  public void putPixel(int x, int y, Color color) {
    if (x >= 0 && x < WIDTH && y >= 0 && y < buffer.getHeight()) {
      buffer.setRGB(x, y, color.getRGB());
    }
  }

  public void drawLine(int x1, int y1, int x2, int y2, Color color) {
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);

    int xi = (x2 > x1) ? 1 : -1;
    int yi = (y2 > y1) ? 1 : -1;

    int A = 2 * dy;
    int B = 2 * dy - 2 * dx;
    int C = 2 * dx;
    int D = 2 * dx - 2 * dy;

    int x = x1;
    int y = y1;

    int pk;

    putPixel(x, y, color);

    if (dx > dy) {
      pk = 2 * dy - dx;
      while (x != x2) {
        if (pk > 0) {
          x = x + xi;
          y = y + yi;
          pk = pk + B;
        } else {
          x = x + xi;
          pk = pk + A;
        }
        putPixel(x, y, color);
      }
    } else {
      pk = 2 * dx - dy;
      while (y != y2) {
        if (pk > 0) {
          x = x + xi;
          y = y + yi;
          pk = pk + D;
        } else {
          y = y + yi;
          pk = pk + C;
        }
        putPixel(x, y, color);
      }
    }
  }

  public void drawCircle(int xCenter, int yCenter, int radius, Color color) {
    int x = radius;
    int y = 0;
    int dx = 1;
    int dy = 1;
    int err = dx - (radius << 1);

    while (x >= y) {
      putPixel(xCenter + x, yCenter + y, color);
      putPixel(xCenter - x, yCenter + y, color);
      putPixel(xCenter - x, yCenter - y, color);
      putPixel(xCenter + x, yCenter - y, color);
      putPixel(xCenter + y, yCenter + x, color);
      putPixel(xCenter - y, yCenter + x, color);
      putPixel(xCenter - y, yCenter - x, color);
      putPixel(xCenter + y, yCenter - x, color);

      if (err <= 0) {
        y++;
        err += dy;
        dy += 2;
      }

      if (err > 0) {
        x--;
        dx += 2;
        err += dx - (radius << 1);
      }
    }
  }

  public void clipAndDrawCircle(int xCenter, int yCenter, int radius, Color color) {
    int xMin = Math.max(LEFT, xCenter - radius);
    int xMax = Math.min(RIGHT, xCenter + radius);
    int yMin = Math.max(TOP, yCenter - radius);
    int yMax = Math.min(BOTTOM, yCenter + radius);

    for (int x = xMin; x <= xMax; x++) {
      for (int y = yMin; y <= yMax; y++) {
        double distance = Math.sqrt((x - xCenter) * (x - xCenter) + (y - yCenter) * (y - yCenter));
        if (Math.abs(distance - radius) < 1.0) {
          putPixel(x, y, color);
        }
      }
    }
  }

  private void drawRectOutline(int x1, int y1, int x2, int y2) {
    drawLine(x1, y1, x2, y1, Color.CYAN);
    drawLine(x2, y1, x2, y2, Color.CYAN);
    drawLine(x2, y2, x1, y2, Color.CYAN);
    drawLine(x1, y2, x1, y1, Color.CYAN);
  }

  public static void main(String[] args) {
    CircleClip circle = new CircleClip();
    circle.clipAndDrawCircle(400, 300, 200, Color.RED);
    circle.clipAndDrawCircle(250, 250, 100, Color.BLUE);
    circle.clipAndDrawCircle(600, 150, 120, Color.GREEN);
    circle.clipAndDrawCircle(100, 100, 120, Color.BLACK);
    circle.clipAndDrawCircle(100, 500, 150, Color.MAGENTA);
  }

}
