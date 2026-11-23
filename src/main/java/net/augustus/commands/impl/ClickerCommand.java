package net.augustus.commands.impl;



import net.augustus.commands.Command;
import net.augustus.utils.input.ClickPatternRecorder;
public class ClickerCommand extends Command {
   public static final ClickPatternRecorder CLICK_PATTERN_RECORDER = new ClickPatternRecorder();

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Start or stop the click recording)";
      this.sendChat(str);
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         if (strings[1].equalsIgnoreCase("start")) {
            if (CLICK_PATTERN_RECORDER.startRecording()) {
               String str2 = "Started recording... Please hold sneak key while recording";
               this.sendChat(str2);
            } else {
               String str3 = "You are already recording";
               this.sendChat(str3);
            }

            return;
         }

         if (strings[1].equalsIgnoreCase("stop")) {
            if (strings.length == 3) {
               String str4 = strings[2];
               if (CLICK_PATTERN_RECORDER.stopAndSave(str4)) {
                  String str5 = "Saved recording successfully to " + strings[2] + ".json!";
                  this.sendChat(str5);
               } else {
                  String str6 = "Something went wrong while recording/saving. Please try again!";
                  this.sendChat(str6);
               }
            } else {
               String str7 = "§7" + this.getCommand() + " [§cstop§7] [§2name§7]";
               String str8 = str7;
               this.sendChat(str8);
            }

            return;
         }

         if (strings[1].equalsIgnoreCase("abort")) {
            CLICK_PATTERN_RECORDER.cancel();
            String str9 = "Recording successfully canceled.";
            this.sendChat(str9);
            return;
         }
      }

      String str10 = "§7" + this.getCommand() + " [§cstart/stop/abort§7]";
      this.sendChat(str10);
   }

   public ClickerCommand() {
      super("clicker");
   }

}
