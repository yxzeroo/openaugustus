package net.augustus.cosmetics.ui;


import net.augustus.cosmetics.CosmeticsMenuRenderer;
public interface CosmeticsMenuHook {
   default void onKeyTyped(char ch, int n) {
   }

   default void onInit(CosmeticsMenuRenderer cosmeticsMenuRenderer) {
   }

   default boolean onMouseClicked(CosmeticsMenuRenderer cosmeticsMenuRenderer, int n, int n2, int n3) {
      return false;
   }

   default void onRender(Object obj, int n, int n2, boolean flag) {
   }
}
