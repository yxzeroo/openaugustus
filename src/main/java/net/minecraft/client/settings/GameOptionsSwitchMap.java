package net.minecraft.client.settings;


public class GameOptionsSwitchMap {
   public static final GameOptionsSwitchMap INSTANCE = new GameOptionsSwitchMap();
   public final int[] OPTION_ORDINAL_MAP = new int[GameSettings.Options.values().length];

   public GameOptionsSwitchMap() {
      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.FBO_ENABLE.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.CHAT_COLOR.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.CHAT_LINKS.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 9;
      } catch (NoSuchFieldError e9) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 10;
      } catch (NoSuchFieldError e10) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.USE_VBO.ordinal()] = 11;
      } catch (NoSuchFieldError e11) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.TOUCHSCREEN.ordinal()] = 12;
      } catch (NoSuchFieldError e12) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 13;
      } catch (NoSuchFieldError e13) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 14;
      } catch (NoSuchFieldError e14) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.BLOCK_ALTERNATIVES.ordinal()] = 15;
      } catch (NoSuchFieldError e15) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.REDUCED_DEBUG_INFO.ordinal()] = 16;
      } catch (NoSuchFieldError e16) {
      }

      try {
         this.OPTION_ORDINAL_MAP[GameSettings.Options.ENTITY_SHADOWS.ordinal()] = 17;
      } catch (NoSuchFieldError e17) {
      }
   }
}
