import java.awt.*;
import java.util.*;

public class ScanLineFill {
  private Pixel pixel;
  private DrawLineBresenham dlb;

  public ScanLineFill(int windowWidth, int windowHeight) {
    if (pixel == null) {
      pixel = new Pixel(windowWidth, windowHeight);
    }
    dlb = new DrawLineBresenham(windowWidth, windowHeight);
    pixel.showWindow();
  }

  public void drawPolygon(Point[] vertices) {
    fillPolygon(vertices);
    int n = vertices.length;
    for (int i = 0; i < n; i++) {
      Point p1 = vertices[i];
      Point p2 = vertices[(i + 1) % n];
      dlb.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
  }

  private void fillPolygon(Point[] vertices) {
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Point vertex : vertices) {
      if (vertex.y < minY) {
        minY = vertex.y;
      }
      if (vertex.y > maxY) {
        maxY = vertex.y;
      }
    }

    for (int y = minY + 1; y < maxY; y++) {
      ArrayList<Integer> intersectionPoints = new ArrayList<>();
      for (int i = 0; i < vertices.length; i++) {
        Point p1 = vertices[i];
        Point p2 = vertices[(i + 1) % vertices.length];
        if ((p1.y <= y && p2.y > y) || (p2.y <= y && p1.y > y)) {
          double x = (double) (p1.x + (double) (y - p1.y) / (p2.y - p1.y) * (p2.x - p1.x));
          intersectionPoints.add((int) x);
        }
      }

      Collections.sort(intersectionPoints);
      for (int i = 0; i < intersectionPoints.size(); i += 2) {
        int x1 = intersectionPoints.get(i);
        int x2 = intersectionPoints.get(i + 1);
        for (int x = x1; x <= x2; x++) {
          pixel.putPixel(x, y, Color.RED);
        }
      }
    }
  }

  public static void main(String[] args) {
    ScanLineFill slf = new ScanLineFill(400, 400);
    Point[] vertices = { new Point(200, 50), new Point(250, 150), new Point(350, 150),
        new Point(270, 200), new Point(320, 300),
        new Point(200, 240), new Point(80, 300),
        new Point(130, 200), new Point(50, 150),
        new Point(150, 150) };
    slf.drawPolygon(vertices);
  }
}