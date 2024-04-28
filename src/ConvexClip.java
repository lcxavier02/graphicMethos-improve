import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ConvexClip extends JFrame {
  private final int WIDTH = 800;
  private final int HEIGHT = 600;
  private BufferedImage buffer;
  private Image image;
  private Graphics graphBuffer;

  private final int LEFT = 150;
  private final int RIGHT = 660;
  private final int TOP = 100;
  private final int BOTTOM = 500;

  public ConvexClip() {
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

  private void drawRectOutline(int x1, int y1, int x2, int y2) {
    drawLine(x1, y1, x2, y1, Color.CYAN);
    drawLine(x2, y1, x2, y2, Color.CYAN);
    drawLine(x2, y2, x1, y2, Color.CYAN);
    drawLine(x1, y2, x1, y1, Color.CYAN);
  }

  public void clipAndDrawConvexRegion(Point[] points, Color color) {
    // Crear una lista para almacenar los puntos recortados
    ArrayList<Point> clippedPoints = new ArrayList<>();

    // Recortar y agregar los puntos de la región convexa que están dentro del
    // rectángulo
    int numPoints = points.length;
    for (int i = 0; i < numPoints; i++) {
      int x1 = points[i].x;
      int y1 = points[i].y;
      int x2 = points[(i + 1) % numPoints].x;
      int y2 = points[(i + 1) % numPoints].y;

      // Verificar si el punto inicial está dentro del rectángulo
      boolean isInsideStart = isInside(x1, y1);
      // Verificar si el punto final está dentro del rectángulo
      boolean isInsideEnd = isInside(x2, y2);

      // Si ambos puntos están dentro del rectángulo, agregar el punto final
      if (isInsideStart && isInsideEnd) {
        clippedPoints.add(new Point(x2, y2));
      } else {
        // Calcular la intersección con los lados del rectángulo y agregarla si es
        // necesario
        int clippedX, clippedY;

        if (x1 < LEFT && x2 >= LEFT && isInsideEnd) {
          double slope = (double) (y2 - y1) / (x2 - x1);
          clippedX = LEFT;
          clippedY = (int) (slope * (LEFT - x1) + y1);
          clippedPoints.add(new Point(clippedX, clippedY));
        }

        if (x1 > RIGHT && x2 <= RIGHT && isInsideEnd) {
          double slope = (double) (y2 - y1) / (x2 - x1);
          clippedX = RIGHT;
          clippedY = (int) (slope * (RIGHT - x1) + y1);
          clippedPoints.add(new Point(clippedX, clippedY));
        }

        if (y1 < TOP && y2 >= TOP && isInsideEnd) {
          double slope = (double) (x2 - x1) / (y2 - y1);
          clippedY = TOP;
          clippedX = (int) (slope * (TOP - y1) + x1);
          clippedPoints.add(new Point(clippedX, clippedY));
        }

        if (y1 > BOTTOM && y2 <= BOTTOM && isInsideEnd) {
          double slope = (double) (x2 - x1) / (y2 - y1);
          clippedY = BOTTOM;
          clippedX = (int) (slope * (BOTTOM - y1) + x1);
          clippedPoints.add(new Point(clippedX, clippedY));
        }

      }

    }

    // Dibujar la región convexa recortada
    int clippedPointsCount = clippedPoints.size();
    for (int i = 0; i < clippedPointsCount; i++) {
      int x1 = clippedPoints.get(i).x;
      int y1 = clippedPoints.get(i).y;
      int x2 = clippedPoints.get((i + 1) % clippedPointsCount).x;
      int y2 = clippedPoints.get((i + 1) % clippedPointsCount).y;

      drawLine(x1, y1, x2, y2, color);
    }
  }

  private boolean isInside(int x, int y) {
    return x >= LEFT && x <= RIGHT && y >= TOP && y <= BOTTOM;
  }

  public static void main(String[] args) {
    ConvexClip convex = new ConvexClip();
    Point[] convexRegion = {
        new Point(300, 400),
        new Point(500, 200),
        new Point(700, 400),
        new Point(650, 500),
        new Point(250, 500)
    };
    Point[] convexRegionBlue = {
        new Point(100, 200),
        new Point(200, 50),
        new Point(400, 150),
        new Point(350, 350),
        new Point(200, 500),
        new Point(50, 350)
    };
    Point[] convexRegionGreen = {
        new Point(600, 400),
        new Point(750, 150),
        new Point(850, 300),
        new Point(750, 600),
        new Point(550, 600),
        new Point(450, 450)
    };

    convex.clipAndDrawConvexRegion(convexRegion, Color.RED);
    convex.clipAndDrawConvexRegion(convexRegionBlue, Color.BLUE);
    convex.clipAndDrawConvexRegion(convexRegionGreen, Color.GREEN);
  }

}
