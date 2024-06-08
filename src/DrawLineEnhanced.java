import java.awt.*;

public class DrawLineEnhanced {
  private Pixel pixel;

  public DrawLineEnhanced(int windowWidth, int windowHeight) {
    if (pixel == null) {
      pixel = new Pixel(windowWidth, windowHeight);
    }
    pixel.showWindow();
  }

  public void drawLine(int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
      int startY = Math.min(y1, y2);
      int endY = Math.max(y1, y2);
      for (int y = startY; y <= endY; y++) {
        pixel.putPixel(x1, y, Color.BLACK);
      }
    } else if (y1 == y2) {
      int startX = Math.min(x1, x2);
      int endX = Math.max(x1, x2);
      for (int x = startX; x <= endX; x++) {
        pixel.putPixel(x, y1, Color.BLACK);
      }
    } else {
      double m = (double) (y2 - y1) / (x2 - x1);
      double b = y1 - (m * x1);
      int startX = Math.min(x1, x2);
      int endX = Math.max(x1, x2);
      for (int x = startX; x <= endX; x++) {
        int y = (int) (m * x + b);
        pixel.putPixel(x, y, Color.BLACK);
      }
    }
  }

  public static void main(String[] args) {
    DrawLineEnhanced dl = new DrawLineEnhanced(300, 300);
    dl.drawLine(70, 35, 180, 165);
  }
}
