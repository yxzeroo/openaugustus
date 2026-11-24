package net.augustus.changelog;

import java.awt.Color;

public enum ChangelogEntryType {
   ADDED(Color.GREEN),
   IMPROVED(Color.YELLOW),
   FIXED(Color.ORANGE),
   REMOVED(Color.RED);

   private final Color color;

   private ChangelogEntryType(Color color2) {
      this.color = color2;
   }

   public Color getColor() {
      return this.color;
   }
}
