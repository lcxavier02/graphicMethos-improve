import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Stack;

public class FloodFill extends JFrame {

  private final int WIDTH = 800;
  private final int HEIGHT = 600;
  private BufferedImage canvas;
  private Image image;
  private Graphics graphBuffer;

  public FloodFill() {
    setTitle("Drawing Canvas");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    if (canvas == null) {
      canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    Graphics2D g = canvas.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    g.dispose();

    setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    image = createImage(getWidth(), getHeight());
    graphBuffer = image.getGraphics();
    g.drawImage(canvas, 0, 0, this);
  }

  public void putPixel(int x, int y, Color color) {
    if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
      canvas.setRGB(x, y, color.getRGB());
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

  public void drawRectangle(int x, int y, int width, int height, Color color) {
    drawLine(x, y, x + width, y, color);
    drawLine(x + width, y, x + width, y + height, color);
    drawLine(x + width, y + height, x, y + height, color);
    drawLine(x, y + height, x, y, color);

    int xMid = x + width / 2;
    int yMid = y + height / 2;

    floodFill(xMid, yMid, Color.WHITE, color);
  }

  public void drawPolygon(List<Integer> xPoints, List<Integer> yPoints, Color color) {
    int nPoints = xPoints.size();
    for (int i = 0; i < nPoints; i++) {
      int x1 = xPoints.get(i);
      int y1 = yPoints.get(i);
      int x2 = xPoints.get((i + 1) % nPoints);
      int y2 = yPoints.get((i + 1) % nPoints);
      drawLine(x1, y1, x2, y2, color);
    }
    int xMid = (int) xPoints.stream().mapToInt(Integer::intValue).average().orElse(0);
    int yMid = (int) yPoints.stream().mapToInt(Integer::intValue).average().orElse(0);
    floodFill(xMid, yMid, Color.WHITE, color);
  }

  public void floodFill(int x, int y, Color targetColor, Color replacementColor) {
    if (targetColor.equals(replacementColor)) {
      return;
    }

    Stack<Point> stack = new Stack<>();
    stack.push(new Point(x, y));

    while (!stack.isEmpty()) {
      Point point = stack.pop();
      int px = point.x;
      int py = point.y;

      if (px < 0 || px >= WIDTH || py < 0 || py >= HEIGHT) {
        continue;
      }

      if (canvas.getRGB(px, py) != targetColor.getRGB()) {
        continue;
      }

      canvas.setRGB(px, py, replacementColor.getRGB());

      stack.push(new Point(px + 1, py));
      stack.push(new Point(px - 1, py));
      stack.push(new Point(px, py + 1));
      stack.push(new Point(px, py - 1));
    }
  }

  public static void main(String[] args) {
    FloodFill floodFill = new FloodFill();
    List<Integer> xPoints = List.of(400, 500, 600, 550);
    List<Integer> yPoints = List.of(300, 250, 300, 350);
    floodFill.drawPolygon(xPoints, yPoints, Color.RED);

    floodFill.drawRectangle(200, 200, 100, 200, Color.BLUE);
  }
}
