package net.augustus.clickgui.graph;

import net.augustus.render.RenderUtil;
import net.augustus.settings.graph.GraphCurveRenderer;
import net.augustus.settings.graph.GraphPoint;
import net.augustus.settings.graph.GraphViewport;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LinearGraphRenderer implements GraphCurveRenderer {
   private float lineWidth;

   public void renderCurve(List list, GraphViewport graphViewport) {
      if (list != null && list.size() >= 2) {
         ArrayList list2 = new ArrayList(list);
         list2.sort(Comparator.comparingDouble(GraphPoint::getX));

         for (int i = 0; i < list2.size() - 1; i++) {
            GraphPoint graphPoint = (GraphPoint)list2.get(i);
            GraphPoint graphPoint2 = (GraphPoint)list2.get(i + 1);
            double x = graphPoint.getX();
            int n;
            double d = x;
            int n2 = graphViewport.toScreenX(d);
            double y = graphPoint.getY();
            int n3 = graphViewport.toScreenY(y);
            double x2 = graphPoint2.getX();
            double d2 = x2;
            n = graphViewport.toScreenX(d2);
            double y2 = graphPoint2.getY();
            RenderUtil.drawLine3(n2, n3, n, graphViewport.toScreenY(y2), this.lineWidth, Color.WHITE);
         }
      }
   }

   public LinearGraphRenderer(float f) {
      this.lineWidth = f;
   }

   public LinearGraphRenderer() {
      this.lineWidth = 2.0F;
   }
}
