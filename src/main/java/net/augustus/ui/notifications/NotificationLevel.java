package net.augustus.ui.notifications;

import java.awt.Color;

public enum NotificationLevel {
   INFO(Color.GREEN),
   WARNING(Color.YELLOW),
   ERROR(Color.RED);

private final Color color;

   public Color getColor() {
      return this.color;
   }




   private NotificationLevel(Color color2) {
      this.color = color2;
   }


}
