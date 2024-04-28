import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LineCut extends JFrame {
  private final int WIDTH = 800;
  private final int HEIGHT = 600;
  private BufferedImage buffer;
  private Image image;
  private Graphics graphBuffer;

  private final int LEFT = 100;
  private final int RIGHT = 500;
  private final int TOP = 100;
  private final int BOTTOM = 400;

  public LineCut() {
    setTitle("Rect Line Cut");
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

  @Override
  public void paint(Graphics g) {
    image = createImage(getWidth(), getHeight());
    g.drawImage(buffer, 0, 0, this);
  }

  public void clipAndDrawLine(int x1, int y1, int x2, int y2, Color color) {
    int intersectionX1, intersectionY1, intersectionX2, intersectionY2;

    if (!isInside(x1, y1)) {
      intersectionX1 = x1 + (x2 - x1) * (TOP - y1) / (y2 - y1);
      intersectionY1 = TOP;
    } else {
      intersectionX1 = x1;
      intersectionY1 = y1;
    }

    if (!isInside(x2, y2)) {
      intersectionX2 = x2 + (x1 - x2) * (BOTTOM - y2) / (y1 - y2);
      intersectionY2 = BOTTOM;
    } else {
      intersectionX2 = x2;
      intersectionY2 = y2;
    }

    if (intersectionX1 < LEFT) {
      intersectionX1 = LEFT;
      intersectionY1 = y1 + (y2 - y1) * (LEFT - x1) / (x2 - x1);
    } else if (intersectionX1 > RIGHT) {
      intersectionX1 = RIGHT;
      intersectionY1 = y1 + (y2 - y1) * (RIGHT - x1) / (x2 - x1);
    }

    if (intersectionX2 < LEFT) {
      intersectionX2 = LEFT;
      intersectionY2 = y1 + (y2 - y1) * (LEFT - x1) / (x2 - x1);
    } else if (intersectionX2 > RIGHT) {
      intersectionX2 = RIGHT;
      intersectionY2 = y1 + (y2 - y1) * (RIGHT - x1) / (x2 - x1);
    }

    if (isInside(intersectionX1, intersectionY1) || isInside(intersectionX2, intersectionY2)) {
      drawLine(intersectionX1, intersectionY1, intersectionX2, intersectionY2, color);
    }
  }

  private boolean isInside(int x, int y) {
    return x >= LEFT && x <= RIGHT && y >= TOP && y <= BOTTOM;
  }

  private void drawRectOutline(int x1, int y1, int x2, int y2) {
    drawLine(x1, y1, x2, y1, Color.CYAN);
    drawLine(x2, y1, x2, y2, Color.CYAN);
    drawLine(x2, y2, x1, y2, Color.CYAN);
    drawLine(x1, y2, x1, y1, Color.CYAN);
  }

  public static void main(String[] args) {
    LineCut cut = new LineCut();
    cut.clipAndDrawLine(50, 50, 300, 300, Color.RED);
    cut.clipAndDrawLine(150, 150, 600, 400, Color.BLUE);
    cut.clipAndDrawLine(90, 90, 350, 250, Color.GREEN);
    cut.clipAndDrawLine(300, 50, 300, 600, Color.BLACK);
    cut.clipAndDrawLine(50, 250, 300, 600, Color.MAGENTA);
  }
}
