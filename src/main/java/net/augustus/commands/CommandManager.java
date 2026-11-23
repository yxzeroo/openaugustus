package net.augustus.commands;

import net.augustus.Augustus;
import net.augustus.commands.impl.AlertCommand;
import net.augustus.commands.impl.AquaCommand;
import net.augustus.commands.impl.ArrayListSuffixCommand;
import net.augustus.commands.impl.AutoRegisterCommand;
import net.augustus.commands.impl.BindCommand;
import net.augustus.commands.impl.BindsCommand;
import net.augustus.commands.impl.ClickerCommand;
import net.augustus.commands.impl.ClientSpooferCommand;
import net.augustus.commands.impl.CrashCommand;
import net.augustus.commands.impl.CustomNameCommand;
import net.augustus.commands.impl.DeathMessageCommand;
import net.augustus.commands.impl.FriendCommand;
import net.augustus.commands.impl.FuckerCommand;
import net.augustus.commands.impl.GcCommand;
import net.augustus.commands.impl.HClipCommand;
import net.augustus.commands.impl.HelpCommand;
import net.augustus.commands.impl.IgnCommand;
import net.augustus.commands.impl.IrcCommand;
import net.augustus.commands.impl.KillMessageCommand;
import net.augustus.commands.impl.MotionModifierCommand;
import net.augustus.commands.impl.MsgCommand;
import net.augustus.commands.impl.PanicCommand;
import net.augustus.commands.impl.PathFinderCommand;
import net.augustus.commands.impl.ReplyCommand;
import net.augustus.commands.impl.ResetBindsCommand;
import net.augustus.commands.impl.ServerIrcCommand;
import net.augustus.commands.impl.SpammerCommand;
import net.augustus.commands.impl.ToggleCommand;
import net.augustus.commands.impl.VClipCommand;
import net.augustus.config.ConfigManager;
import net.augustus.events.EventChat;
import net.augustus.events.EventManager;
import net.augustus.events.listeners.ChatListener;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements ChatListener {
   public final AlertCommand alertCommand;
   private List commands;
   public final AquaCommand aquaCommand;
   public final MsgCommand msgCommand;
   public final ReplyCommand replyCommand;
   public final IrcCommand ircCommand;

   public CommandManager() {
       super();
      this.commands = new ArrayList();
      Command.getControlFlowState();
      EventManager.getEventBus().subscribe(5, this);
      this.commands.add(new BindCommand());
      this.commands.add(new BindsCommand());
      this.commands.add(new HelpCommand());
      this.commands.add(new IgnCommand());
      this.commands.add(new ToggleCommand());
      this.commands.add(new FuckerCommand());
      this.commands.add(new ClickerCommand());
      this.commands.add(new ArrayListSuffixCommand());
      this.commands.add(new KillMessageCommand());
      this.commands.add(new CustomNameCommand());
      this.commands.add(new FriendCommand());
      this.commands.add(new VClipCommand());
      this.commands.add(new HClipCommand());
      this.commands.add(new PathFinderCommand());
      this.commands.add(new ClientSpooferCommand());
      this.commands.add(new SpammerCommand());
      this.commands.add(new MotionModifierCommand());
      this.commands.add(new AutoRegisterCommand());
      this.commands.add(new GcCommand());
      this.commands.add(this.aquaCommand = new AquaCommand());
      this.commands.add(this.ircCommand = new IrcCommand());
      this.commands.add(this.msgCommand = new MsgCommand());
      this.commands.add(this.replyCommand = new ReplyCommand());
      this.commands.add(new CrashCommand());
      this.commands.add(new ServerIrcCommand());
      this.commands.add(new DeathMessageCommand());
      this.commands.add(this.alertCommand = new AlertCommand());
      this.commands.add(new PanicCommand());
      this.commands.add(new ResetBindsCommand());
      Augustus.getInstance().getClass();
   }

   public void setCommands(List list) {
      this.commands = list;
   }

   public void onChat(EventChat event) {
      String message = event.getMessage();
      Command.getControlFlowState();

      for (Command command : (Iterable<Command>)(this.getCommands())) {
         String[] strings = message.split(" ");
         if (strings.length > 0 && strings[0].equalsIgnoreCase(command.getCommand())) {
            command.commandAction(strings, message);
            event.cancel();
            ConfigManager configManager = Augustus.getInstance().getConfigManager();
            configManager.snapshotAll();
         }
      }

      if (message.startsWith(Augustus.getInstance().getModuleManager().augustusServer.commandPrefixes.getSelectedValue()) && message.length() > 1) {
         event.cancel();
      }
   }

   public List getCommands() {
      return this.commands;
   }
}
