package net.augustus.settings.graph;

public interface GraphViewport {
   double getMaxY(long l);

   int getCanvasY(long l);

   int getCanvasWidth();

   int getCanvasHeight();

   double getMinY(long l);

   double getMinX(long l);

   int getCanvasX(long l);

   double getMaxX(int n, short s, char ch);

   int toScreenY(double d);

   int toScreenX(double d);
}
